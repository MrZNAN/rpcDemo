package utils;

import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.*;

public class RedisUtil {

    private static  Jedis jedis = null;
    static {
        jedis = new Jedis("127.0.0.1", 6379);
    }


    public static void addHash(String pKey, String key, String value) throws Exception{

        Set<String> hkeys = jedis.hkeys(pKey);
        if(hkeys == null || hkeys.size() == 0){
            Map<String, String> hashMap = new HashMap<String, String>(16);
            hashMap.put(key,value);
            jedis.hmset(pKey,hashMap);
        }else{
            jedis.hset(pKey , key, value);
        }

    }

    public static List<String>  getAllKeys(String partten){
        Set<String> keys = jedis.keys(partten);
        List<String> list = new ArrayList<>();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()){
            try {
                list.add(getValueBykey(iterator.next()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static Map<String, String> getAllServes(String pKey) throws Exception{
        return jedis.hgetAll(pKey);
    }

    public static void addKeyValue( String key, String value) throws Exception{
        // 创建一个redis连接(也可以创建池)
        // 写入一个字符串;
        jedis.set(key, value);
        jedis.expire(key,3);
    }

    public static String getValueBykey( String key) throws Exception{
        // 创建一个redis连接(也可以创建池)
        // 写入一个字符串;
        return jedis.get(key);
    }



}
