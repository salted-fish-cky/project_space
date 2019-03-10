package com.cky.sparkproject.spark.ad;

import com.cky.sparkproject.conf.ConfigurationManager;
import com.cky.sparkproject.constants.Constants;
import com.cky.sparkproject.dao.*;
import com.cky.sparkproject.domain.*;
import com.cky.sparkproject.utils.DateUtils;
import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import scala.Tuple2;

import java.util.*;

/**
 * 广告点击流量实时统计spark作业
 */
public class AdClickRealTimeStatSpark {

    public static void main(String[] args) throws InterruptedException {
        //构建sparkStreaming上下文
        SparkConf conf = new SparkConf()
                .setMaster("local[2]")
                .setAppName("AdClickRealTimeStatSpark");
        //设置处理数据间隔时间
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));

        jssc.checkpoint("hdfs://number3:9000/checkpoint");

        //创建针对Kafka数据来源的输入DStream

        //构建Kafka参数map,放置的是你要连接的kafka集群的地址
        Map<String,String> kafkaParams = new HashMap<String, String>();
        kafkaParams.put(Constants.KAFKA_METADATA_BROKER_LIST,
                ConfigurationManager.getProperty(Constants.KAFKA_METADATA_BROKER_LIST));

        //构建topic set
        String kafkaTopics = ConfigurationManager.getProperty(Constants.KAFKA_TOPICS);
        String[] kafkaTopicsSplited = kafkaTopics.split(",");

        Set<String> topics = new HashSet<String>();
        for (String topic : kafkaTopicsSplited){
            topics.add(topic);
        }
        //基于kafka direct api模式，构建出了针对kafka集群中指定topics的输入DStream
        //第一个值没有特殊意义，第二个值包含了kafka topic的实时数据
        JavaPairInputDStream<String, String> adRealTimeLogDStream = KafkaUtils.createDirectStream(
                jssc,
                String.class,
                String.class,
                StringDecoder.class,
                StringDecoder.class, kafkaParams, topics);


        //刚刚接收到原始的用户点击行为日志之后，
        //根据mysql中的动态黑名单，进行实名的黑名单过滤
        JavaPairDStream<String, String> filteredAdRealTimeLogDStream =
                filterByBlackList(adRealTimeLogDStream);

        //计算出每5个秒内的数据中，每天每个广告的点击量
        //数据格式 ：timestamp province city userid adid
        //生成动态黑名单
        generateDynamicBlackList(filteredAdRealTimeLogDStream);

        //业务功能一: 计算广告点击流量实时统计结果（yyyyMMdd_province_city_adId,clickCount）
        JavaPairDStream<String, Long> adRealTimeStatDStream =
                calculateRealTimeStat(filteredAdRealTimeLogDStream);

        //业务功能二:实时统计每天每个省份top3热门广告
        calculateProvinceTop3Ad(adRealTimeStatDStream,conf);

        //业务功能三 :实时统计每天每个个广告在最近一小时的滑动窗口内的点击趋势
        calculateAdClickCountByWindow(adRealTimeLogDStream);


        jssc.start();
        jssc.awaitTermination();
    }





    /**
     * //刚刚接收到原始的用户点击行为日志之后，
     //根据mysql中的动态黑名单，进行实名的黑名单过滤
     * @param adRealTimeLogDStream
     * @return
     */
    private static JavaPairDStream<String, String> filterByBlackList(
            JavaPairInputDStream<String, String> adRealTimeLogDStream){
        JavaPairDStream<String, String> filteredAdRealTimeLogDStream =
                adRealTimeLogDStream.transformToPair(
                    new Function<JavaPairRDD<String, String>, JavaPairRDD<String, String>>() {
                        @Override
                        public JavaPairRDD<String, String>
                        call(JavaPairRDD<String, String> rdd) throws Exception {

                            //从msyql中查询所有黑名单用户，将其转换成一个rdd
                            IAdBlackListDao adBlackListDao = DAOFactory.getAdBlackListDao();
                            List<AdBlacklist> adBlacklists = adBlackListDao.findAll();
                            List<Tuple2<Long,Boolean>> tuples = new ArrayList<Tuple2<Long, Boolean>>();
                            for (AdBlacklist adBlacklist : adBlacklists){
                                tuples.add(new Tuple2<Long, Boolean>(adBlacklist.getUserId(),true));
                            }

                            JavaSparkContext sc = new JavaSparkContext(rdd.context());
                            JavaPairRDD<Long, Boolean> blackListRDD = sc.parallelizePairs(tuples);

                            //将原始数据rdd映射成<userid,str1|str2>
                            JavaPairRDD<Long, Tuple2<String, String>> mappedRDD =
                                    rdd.mapToPair(new PairFunction<Tuple2<String, String>, Long, Tuple2<String, String>>() {
                                        @Override
                                        public Tuple2<Long, Tuple2<String, String>>
                                        call(Tuple2<String, String> tuple) throws Exception {
                                            //从tuple中获取到每一条原始的实时日志
                                            String log = tuple._2;

                                            String[] logSplited = log.split(" ");
                                            //图区出日期(yyMMdd)-userid-adid

                                            long userId = Long.valueOf(logSplited[3]);

                                            return new Tuple2<Long, Tuple2<String, String>>(userId, tuple);
                                        }
                                    });

                            //将原始日志数据rdd与黑名单rdd，进行左外连接
                            JavaPairRDD<Long, Tuple2<Tuple2<String, String>, Optional<Boolean>>> joinRDD =
                                    mappedRDD.leftOuterJoin(blackListRDD);

                            JavaPairRDD<Long, Tuple2<Tuple2<String, String>, Optional<Boolean>>> filteredRDD =
                                    joinRDD.filter(
                                            new Function<Tuple2<Long, Tuple2<Tuple2<String, String>,
                                                    Optional<Boolean>>>, Boolean>() {
                                                @Override
                                                public Boolean call(Tuple2<Long, Tuple2<Tuple2<String, String>,
                                                        Optional<Boolean>>> tuple) throws Exception {

                                                    Optional<Boolean> optional = tuple._2._2;
                                                    //如果这个值存在，那么是join到了
                                                    if(optional.isPresent() && optional.get()){
                                                        return false;
                                                    }
                                                    return true;
                                                }
                                            });

                            JavaPairRDD<String, String> filterByBlackListRDD = filteredRDD.mapToPair(
                                    new PairFunction<Tuple2<Long, Tuple2<Tuple2<String, String>, Optional<Boolean>>>, String, String>() {
                                        @Override
                                        public Tuple2<String, String> call(Tuple2<Long,
                                                Tuple2<Tuple2<String, String>, Optional<Boolean>>> tuple) throws Exception {

                                            return tuple._2._1;
                                        }
                                    });
                            return filterByBlackListRDD;
                        }
                    });
        
        return filteredAdRealTimeLogDStream;
    }

    /**
     * 生成动态黑名单
     * @param filteredAdRealTimeLogDStream
     */
    private static void generateDynamicBlackList(
            JavaPairDStream<String, String> filteredAdRealTimeLogDStream){
        //通过对原始数据的处理，把数据拼接成("dateKey_userId_adId",1)这种格式
        JavaPairDStream<String, Long> dailyUserAdClickDStream = filteredAdRealTimeLogDStream.mapToPair(
                new PairFunction<Tuple2<String, String>, String, Long>() {
                    @Override
                    public Tuple2<String, Long> call(Tuple2<String, String> tuple) throws Exception {
                        //从tuple中获取到每一条原始的实时日志
                        String log = tuple._2;

                        String[] logSplited = log.split(" ");
                        //图区出日期(yyMMdd)-userid-adid
                        String timeStamp = logSplited[0];
                        Date date = new Date(Long.valueOf(timeStamp));
                        String dateKey = DateUtils.formatDateKey(date);
                        long userId =  Long.valueOf(logSplited[3]);
                        Long adId = Long.valueOf(logSplited[4]);

                        String key = dateKey + "_" +userId + "_" +adId;
                        return new Tuple2<String, Long>(key,1l);
                    }
                });

        //针对处理后的日志格式，执行reduceByKey算子即可
        //（每个batch中）每天每个用户对每个广告的点击量
        JavaPairDStream<String, Long> dailyUserAdClickCountDStream =
                dailyUserAdClickDStream.reduceByKey(new Function2<Long, Long, Long>() {
                    @Override
                    public Long call(Long v1, Long v2) throws Exception {
                        return v1 + v2;
                    }
                });
        //dailyUserAdClickCountDStream 每个5s中，每个用户对各广告的点击量
        dailyUserAdClickCountDStream.foreachRDD(new VoidFunction<JavaPairRDD<String, Long>>() {
            @Override
            public void call(JavaPairRDD<String, Long> rdd) throws Exception {
                rdd.foreachPartition(new VoidFunction<Iterator<Tuple2<String, Long>>>() {
                    @Override
                    public void call
                            (Iterator<Tuple2<String, Long>> iterator) throws Exception {
                        //对每个分区创建一次连接对象

                        List<AdUserClickCount> adUserClickCounts = new ArrayList<AdUserClickCount>();
                        while (iterator.hasNext()){
                            Tuple2<String, Long> tuple = iterator.next();
                            String[] keySplited = tuple._1.split("_");
                            String date = DateUtils.formatDate(DateUtils.parseDateKey(keySplited[0]));
                            Long userId = Long.valueOf(keySplited[1]);
                            Long adId = Long.valueOf(keySplited[2]);
                            Long clickCount = tuple._2;

                            AdUserClickCount adUserClickCount = new AdUserClickCount();
                            adUserClickCount.setAdId(adId);
                            adUserClickCount.setClickCount(clickCount);
                            adUserClickCount.setDate(date);
                            adUserClickCount.setUserId(userId);

                            adUserClickCounts.add(adUserClickCount);

                        }
                        IAdUserClickCountDAO adUserClickCountDAO = DAOFactory.getAdUserClickCountDAO();
                        adUserClickCountDAO.updateBatch(adUserClickCounts);
                    }
                });
            }
        });

        //增加动态黑名单

        /**
         * 过滤点击数大于100的用户，设置为黑名单
         */
        JavaPairDStream<String, Long> blackListDStream = dailyUserAdClickCountDStream.filter(new Function<Tuple2<String, Long>, Boolean>() {
            @Override
            public Boolean call(Tuple2<String, Long> tuple) throws Exception {
                String key = tuple._1;
                String[] keySplited = key.split("_");
                System.out.println(DateUtils.parseDateKey(keySplited[0]));
                String date = DateUtils.formatDate(DateUtils.parseDateKey(keySplited[0]));
                long userId = Long.valueOf(keySplited[1]);
                long adId = Long.valueOf(keySplited[2]);

                IAdUserClickCountDAO adUserClickCountDAO = DAOFactory.getAdUserClickCountDAO();
                int clickCount = adUserClickCountDAO.findByMultiKey(date, userId, adId);
                if (clickCount > 100) {
                    return true;
                }
                return false;
            }
        });
        //通过对dstream执行操作，对其中的rdd中的userId进行全局去重
        JavaDStream<Long> blacklistUserIdDStream = blackListDStream.map(new Function<Tuple2<String, Long>, Long>() {
            @Override
            public Long call(Tuple2<String, Long> tuple) throws Exception {
                String key = tuple._1;
                String[] keySplited = key.split("_");
                Long userId = Long.valueOf(keySplited[1]);
                return userId;
            }
        });

        //进行去重
        JavaDStream<Long> distinctBlackListUserIdDStream = blacklistUserIdDStream.transform(new Function<JavaRDD<Long>, JavaRDD<Long>>() {
            @Override
            public JavaRDD<Long> call(JavaRDD<Long> rdd) throws Exception {
                return rdd.distinct();
            }
        });

        distinctBlackListUserIdDStream.foreachRDD(new VoidFunction<JavaRDD<Long>>() {
            @Override
            public void call(JavaRDD<Long> rdd) throws Exception {
                rdd.foreachPartition(new VoidFunction<Iterator<Long>>() {
                    @Override
                    public void call(Iterator<Long> iterator) throws Exception {
                        List<AdBlacklist> list = new ArrayList<AdBlacklist>();
                        while (iterator.hasNext()){
                            Long userId = iterator.next();

                            AdBlacklist adBlacklist = new AdBlacklist();
                            adBlacklist.setUserId(userId);
                            list.add(adBlacklist);
                        }

                        IAdBlackListDao adBlackListDao = DAOFactory.getAdBlackListDao();
                        adBlackListDao.insertBatch(list);
                    }
                });
            }
        });
    }

    /**
     * 计算每天各省各城市各广告的点击量
     * @param filteredAdRealTimeLogDStream
     * @return
     */
    private static JavaPairDStream<String,Long> calculateRealTimeStat(
            JavaPairDStream<String, String> filteredAdRealTimeLogDStream){
        //计算每天各省各城市各广告的点击量
        JavaPairDStream<String, Long> mappedDStream = filteredAdRealTimeLogDStream.mapToPair(
                new PairFunction<Tuple2<String, String>, String, Long>() {
                    @Override
                    public Tuple2<String, Long> call(Tuple2<String, String> tuple) throws Exception {
                        String log = tuple._2;
                        String[] logSplited = log.split(" ");

                        String timeStamp = logSplited[0];
                        Date date = new Date(Long.valueOf(timeStamp));
                        String dateKey = DateUtils.formatDateKey(date);
                        String province = logSplited[1];
                        String city = logSplited[2];
                        long adId = Long.valueOf(logSplited[4]);

                        String key = dateKey + "_" + province + "_" + city + "_" + adId ;
                        return new Tuple2<String, Long>(key,1l);
                    }
                });

        //updateStateByKey的使用
        //在这个dstream中，就相当于，有每个batch rdd累加的各个key
        JavaPairDStream<String, Long> aggregatedDStream = mappedDStream.updateStateByKey(
                new Function2<List<Long>, Optional<Long>, Optional<Long>>() {
                    @Override
                    public Optional<Long> call(List<Long> values, Optional<Long> optional) throws Exception {

                        long clickCount = 0l;
                        //如果之前是存在这个状态的，那么就以之前的状态作为起点，进行值得累加
                        if(optional.isPresent()){
                            clickCount = optional.get();
                        }

                        //values 代表了batch rdd中，每个key对应的所有值
                        for (Long value : values){
                            clickCount += value;
                        }
                        return Optional.of(clickCount);
                    }
                });

        //将计算出来的最新结果，同步一份到mysql中，以便j2ee系统的使用
        aggregatedDStream.foreachRDD(new VoidFunction<JavaPairRDD<String, Long>>() {
            @Override
            public void call(JavaPairRDD<String, Long> rdd) throws Exception {
                rdd.foreachPartition(new VoidFunction<Iterator<Tuple2<String, Long>>>() {
                    @Override
                    public void call(Iterator<Tuple2<String, Long>> iterator) throws Exception {
                        List<AdStat> list = new ArrayList<AdStat>();

                        while (iterator.hasNext()){
                            Tuple2<String, Long> tuple = iterator.next();
                            String key = tuple._1;
                            Long clickCount = tuple._2;
                            String[] keySplited = key.split("_");
                            String date = DateUtils.formatDate(DateUtils.parseDateKey(keySplited[0]));
                            String province = keySplited[1];
                            String city = keySplited[2];
                            long adId = Long.valueOf(keySplited[3]);

                            AdStat adStat = new AdStat();
                            adStat.setAdId(adId);
                            adStat.setCity(city);
                            adStat.setClickCount(clickCount);
                            adStat.setDate(date);
                            adStat.setProvice(province);
                            list.add(adStat);
                        }

                        IAdStatDAO adStatDAO = DAOFactory.getAdStatDAO();
                        adStatDAO.updateBatch(list);
                    }
                });
            }
        });

        return aggregatedDStream;
    }

    /**
     * 计算每天各省份的top3热门广告
     */
    private static void calculateProvinceTop3Ad(
            JavaPairDStream<String, Long> adRealTimeStatDStream, final SparkConf conf){

        JavaDStream<Row> rowsDStream = adRealTimeStatDStream.transform(new Function<JavaPairRDD<String, Long>, JavaRDD<Row>>() {
            @Override
            public JavaRDD<Row> call(JavaPairRDD<String, Long> rdd) throws Exception {

                JavaPairRDD<String, Long> mapedRDD =
                        rdd.mapToPair(new PairFunction<Tuple2<String, Long>, String, Long>() {
                            @Override
                            public Tuple2<String, Long> call(Tuple2<String, Long> tuple) throws Exception {

                                String[] keySplited = tuple._1.split("_");
                                String date = keySplited[0];
                                String province = keySplited[1];
                                long adId = Long.valueOf(keySplited[3]);
                                long clickCount = tuple._2;

                                String key = date + "_" + province + "_" + adId;

                                return new Tuple2<String, Long>(key, clickCount);
                            }
                        });
                //计算出每天各省份的点击量
                JavaPairRDD<String, Long> dailyAdClickCountByProvinceRDD = mapedRDD.reduceByKey(new Function2<Long, Long, Long>() {
                    @Override
                    public Long call(Long v1, Long v2) throws Exception {
                        return v1 + v2;
                    }
                });

                JavaRDD<Row> rowRDD =
                        dailyAdClickCountByProvinceRDD.map(new Function<Tuple2<String, Long>, Row>() {
                            @Override
                            public Row call(Tuple2<String, Long> tuple) throws Exception {

                                String[] keySplited = tuple._1.split("_");
                                String date = DateUtils.formatDate(DateUtils.parseDateKey(keySplited[0]));
                                String province = keySplited[1];
                                Long adId = Long.valueOf(keySplited[2]);
                                long clickCount = tuple._2;


                                return RowFactory.create(date, province, adId, clickCount);
                            }
                        });

                StructType schema = DataTypes.createStructType(Arrays.asList(
                        DataTypes.createStructField("date", DataTypes.StringType, true),
                        DataTypes.createStructField("province", DataTypes.StringType, true),
                        DataTypes.createStructField("ad_id", DataTypes.LongType, true),
                        DataTypes.createStructField("click_count", DataTypes.LongType, true)
                ));
                //sparkSession创建sqlContext
                SparkSession sqlContext = SparkSession.builder().
                        config(conf).sparkContext(rdd.context()).getOrCreate();
//                HiveContext sqlContext = new HiveContext(rdd.context());

                Dataset<Row> dataFrame = sqlContext.createDataFrame(rowRDD, schema);
                dataFrame.registerTempTable("tmp_dayly_ad_click_count_by_province");

                //使用Spark SQL，配合开窗函数，统计出各省份top3热门的广告
                String sql =
                        "select " +
                                "date," +
                                "province," +
                                "ad_id," +
                                "click_count " +
                                "from ( " +
                                "select " +
                                "date," +
                                "province," +
                                "ad_id," +
                                "click_count," +
                                "ROW_NUMBER() OVER(PARTITION BY province order by click_count desc) rank " +
                                "from tmp_dayly_ad_click_count_by_province " +
                                ") t " +
                                "where rank>=3";
                Dataset<Row> df = sqlContext.sql(sql);
                return df.javaRDD();
            }
        });

        //插入到数据库中
        rowsDStream.foreachRDD(new VoidFunction<JavaRDD<Row>>() {
            @Override
            public void call(JavaRDD<Row> rdd) throws Exception {
                rdd.foreachPartition(new VoidFunction<Iterator<Row>>() {
                    @Override
                    public void call(Iterator<Row> iterator) throws Exception {
                        List<AdProvinceTop3> list = new ArrayList<AdProvinceTop3>();
                        while (iterator.hasNext()){
                            Row row = iterator.next();
                            AdProvinceTop3 adProvinceTop3 = new AdProvinceTop3();
                            adProvinceTop3.setDate(row.getString(0));
                            adProvinceTop3.setProvince(row.getString(1));
                            adProvinceTop3.setAdId(row.getLong(2));
                            adProvinceTop3.setClickCount(row.getLong(3));

                            list.add(adProvinceTop3);
                        }

                        IAdProvinceTop3DAO adProvinceTop3DAO = DAOFactory.getAdProvinceTop3DAO();
                        adProvinceTop3DAO.updateBatch(list);
                    }
                });
            }
        });
    }

    /**
     * 计算最近1小时滑动窗口内的广告点击趋势
     * @param adRealTimeLogDStream
     */
    private static void calculateAdClickCountByWindow(
            JavaPairInputDStream<String, String> adRealTimeLogDStream){

        JavaPairDStream<String, Long> pairDStream = adRealTimeLogDStream.mapToPair(
                new PairFunction<Tuple2<String, String>, String, Long>() {
                    @Override
                    public Tuple2<String, Long> call(Tuple2<String, String> tuple) throws Exception {
                        String[] logSplited = tuple._2.split(" ");
                        String timeMinute = DateUtils.formatTimeMinute(
                                new Date(Long.valueOf(logSplited[0])));
                        long adId = Long.valueOf(logSplited[4]);

                        return new Tuple2<String, Long>(timeMinute+"_"+adId,1l);
                    }
                });

        //过来的每个batch rdd，都会被映射成<yyyyMMddHHMM_adId,1L>
        //每次出来一个新的batch，都要获取最近一小时内的所有的batch

        JavaPairDStream<String, Long> aggrRDD =
        pairDStream.reduceByKeyAndWindow(new Function2<Long, Long, Long>() {
            @Override
            public Long call(Long v1, Long v2) throws Exception {
                return v1 + v2;
            }
        }, Durations.minutes(60), Durations.seconds(10));
        //每次都可以拿到，最近一小时内，各分钟的点击量

        aggrRDD.foreachRDD(new VoidFunction<JavaPairRDD<String, Long>>() {
            @Override
            public void call(JavaPairRDD<String, Long> rdd) throws Exception {
                rdd.foreachPartition(new VoidFunction<Iterator<Tuple2<String, Long>>>() {
                    @Override
                    public void call(Iterator<Tuple2<String, Long>> iterator) throws Exception {
                        List<AdClickTrend> list = new ArrayList<AdClickTrend>();

                        while (iterator.hasNext()){
                            Tuple2<String, Long> tuple = iterator.next();
                            String[] keySplited = tuple._1.split("_");
                            String dateMinute = keySplited[0];
                            long adId = Long.valueOf(keySplited[1]);
                            long clickCount = tuple._2;
                            String date = DateUtils.formatDate(DateUtils.parseDateKey(dateMinute.substring(0,8)));
                            String hour = dateMinute.substring(8,10);
                            String minute = dateMinute.substring(10);

                            AdClickTrend adClickTrend = new AdClickTrend();
                            adClickTrend.setAdId(adId);
                            adClickTrend.setClickCount(clickCount);
                            adClickTrend.setDate(date);
                            adClickTrend.setMinute(minute);
                            adClickTrend.setHour(hour);

                            list.add(adClickTrend);
                        }

                        IAdClickTrendDAO iAdClickTrendDAO = DAOFactory.getIAdClickTrendDAO();
                        iAdClickTrendDAO.updateBatch(list);
                    }
                });
            }
        });

    }
}
