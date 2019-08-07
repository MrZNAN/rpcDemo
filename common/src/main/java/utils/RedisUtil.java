package utils;

import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class RedisUtil {

    private static  Jedis jedis = null;
    static {
        jedis = new Jedis("127.0.0.1", 6379);
    }


    public static void addHash(String pKey, String key, Serializable value) throws Exception{

        // 写入一个hash
        Map<byte[], byte[]> hashMap = new HashMap<byte[], byte[]>(16);

        byte[] bytes = new byte[1024000];
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        ois.readObject();

        hashMap.put(key.getBytes(), bytes);
        jedis.hmset(pKey.getBytes(), hashMap);

    }

    public static void addKeyValue( String key, String value) throws Exception{
        // 创建一个redis连接(也可以创建池)
        // 写入一个字符串;
        jedis.set(key, value);
    }

    public static String getValueBykey( String key) throws Exception{
        // 创建一个redis连接(也可以创建池)
        // 写入一个字符串;
        return jedis.get(key);
    }

}
