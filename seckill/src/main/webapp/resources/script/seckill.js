//存放主要交互逻辑JS代码
//javascript模块化  分包   json对象表示方式

/**
 *存放主要交互逻辑JS代码
 *javascript模块化
 */

var seckill = {
    //封装秒杀相关ajax的url
    URL: {

        now:function () {
            return '/seckill/time/now';
        },
        exposer:function (seckillId) {
            return '/seckill/'+seckillId+'/exposer';
        },
        execution:function (seckillId,md5) {
            return '/seckill/'+seckillId+'/'+md5+'/execution';
        }

    },

    handleSeckillkill:function (seckillId,node) {
      //处理秒杀逻辑

        console.log(seckillId)
        node.html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        console.log(seckill.URL.exposer(seckillId))
        $.post(seckill.URL.exposer(seckillId),{},function (result) {
            //在回调函数中，执行交互流程
            if(result && result['success']){
                var exposer = result['data'];
                if(exposer['exposed']){
                    //开启秒杀
                    //获取秒杀地址
                    var md5 = exposer['md5']
                    var killUrl = seckill.URL.execution(seckillId,md5);
                    console.log("killUrl"+killUrl);
                    $('#killBtn').one('click',function () {
                        //
                        //1.先禁用按钮
                        console.log(1111)
                        $(this).addClass('disabled');
                        //2.发送秒杀的请求执行秒杀
                        $.post(killUrl,{},function (result) {
                            if(result && result['success']){
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //3:显示秒杀结果
                                node.html('<span class="label label-success">'+stateInfo+'</span>');
                            }
                        });

                        node.show();

                    });

                }else{
                    //未开启秒杀
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    //重新进行计算计时逻辑
                    seckill.countDown(seckillId,now,start,end);
                }
            }else{
                console.log('result:'+result);
            }
        })
    },

    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },

    countDown:function (seckillId,nowTime,startTime,endTime) {
        var seckillBox = $('#seckill-box');
        //时间的判断
        if(nowTime < endTime && nowTime>startTime){

            //秒杀开始
            seckill.handleSeckillkill(seckillId,seckillBox);


        }else if(nowTime>endTime){

            //seckill over
            seckillBox.html('秒杀结束');


        }else {
            //秒杀未开始
            var killTime = new Date(startTime+1000);
            seckillBox.countdown(killTime,function (event) {
                var format = event.strftime('秒杀倒计时:%D天 %H时 %M分 %S秒');
                //var format = "秒杀倒计数开始！！！！";
                seckillBox.html(format);
            }).on('finish.countdown',function () {
                //时间完成后回调事件  获取秒杀地址，控制显示逻辑，执行秒杀
                seckill.handleSeckillkill(seckillId,seckillBox);
            });
        }
    },

    detail: {
        //详情页初始化
        init: function (params) {
            //手机验证和登录，计时交互
            //规划我们的交互流程
            //在cookie中查找手机号
            var killPhone = $.cookie('killPhone');


            if (!seckill.validatePhone(killPhone)) {
                //绑定PHONE 控制输出
                var killPhoneModal = $('#killPhoneModal');
                //控制输出
                killPhoneModal.modal({

                    show: true, //显示弹出层
                    backdrop: 'static',//禁止位置关
                    keyboard: false//关闭键盘事件
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    if (seckill.validatePhone(inputPhone)) {
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误！</label>').show(300);
                    }
                });
            }else{
                var startTime = params['startTime'];
                var endTime = params['endTime'];
                var seckillId = params['seckillId'];
                $.get(seckill.URL.now(),{},function (result) {
                    if(result && result['success']){
                        var nowTime = result['data'];
                        //时间判断，计时服务
                        seckill.countDown(seckillId,nowTime,startTime,endTime);
                    }else{
                        console.log('result:'+result);
                    }
                });

            }


        }
    }


}