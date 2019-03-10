

import redis.clients.jedis.Jedis;

public class RedisMain {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("number1",6379);
        String response = jedis.ping();
        System.out.println(response);
    }

}
