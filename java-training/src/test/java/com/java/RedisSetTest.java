package com.java;


import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = SampleApplication.class)
@Transactional
public class RedisSetTest {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void demo1() {
        //集合中添加元素，返回添加个数
        redisTemplate.opsForSet().add("games", "鬼泣", "古墓丽影", "仙剑奇侠传", "LOL", "DOTA自走棋");
        printSet("games");
    }

    @Test
    public void demo2() {
        //从集合中删除指定元素
        redisTemplate.opsForSet().remove("games", "鬼泣");
        printSet("games");
    }

    @Test
    public void demo3() {
        //从集合中随机删除一个元素，并返回该元素
        String item = redisTemplate.opsForSet().pop("games");
        System.out.println(item);
        printSet("games");
    }

    @Test
    public void demo4() {
        //将集合1中的指定元素移到集合2
        redisTemplate.opsForSet().move("games", "仙剑奇侠传", "chinese-games");
        //打印集合1
        printSet("games");
        //打印集合2
        printSet("chinese-games");
    }


    @Test
    public void demo5() {
        //获得集合大小
        Long size = redisTemplate.opsForSet().size("games");
        printSet("games");
        System.out.println(size);
    }

    @Test
    public void demo6() {
        //判断集合中是否存在某元素
        Boolean ifExist = redisTemplate.opsForSet().isMember("games", "LOL");
        System.out.println(ifExist);
    }

    @Test
    public void demo7() {
        //添加集合a
        redisTemplate.opsForSet().add("set-a", "a", "b", "c", "d");
        //添加集合b
        redisTemplate.opsForSet().add("set-b", "a", "b");
        //求交集
        Set<String> intersection = redisTemplate.opsForSet().intersect("set-a", "set-b");
        //也可以和多个key对应的集合求交集                  Set<V> intersect(K key, K otherKey);
        System.out.println(intersection);
    }

    @Test
    public void demo8() {
        //添加集合a
        redisTemplate.opsForSet().add("set-a", "a", "b", "c", "d");
        //添加集合b
        redisTemplate.opsForSet().add("set-b", "a", "b");
        //求交集并放入集合c
        redisTemplate.opsForSet().intersectAndStore("set-a", "set-b", "set-c");
        //也可以和多个key对应的集合求交集                 Long intersectAndStore(K key, Collection<K> otherKeys, K destKey);
        //打印集合c
        printSet("set-c");
    }

    @Test
    public void demo9() {
        //添加集合m
        redisTemplate.opsForSet().add("set-m", "a", "b", "c", "d");
        //添加集合n
        redisTemplate.opsForSet().add("set-n", "c", "d", "e", "f");
        //求并集
        Set<String> union = redisTemplate.opsForSet().union("set-m", "set-n");
        System.out.println(union);

        //其他操作与intersect类似 如和多个key求并集，求并集并放入集合等
    }

    @Test
    public void demo10() {
        //添加集合d
        redisTemplate.opsForSet().add("set-d", "a", "b", "c", "d");
        //添加集合e
        redisTemplate.opsForSet().add("set-e", "c", "d", "e", "f");
        //添加集合f
        redisTemplate.opsForSet().add("set-f", "e", "f", "g", "h");

        //求差集(属于d不属于e和f)
        Set<String> difference = redisTemplate.opsForSet().difference("set-d", Arrays.asList("set-e", "set-f"));
        System.out.println(difference);
        //其他操作与交集并集类似
    }

    @Test
    public void demo11() {
        //随机获取集合中的一个元素
        System.out.println(redisTemplate.opsForSet().randomMember("games"));

        //随机获取集合中指定个数的元素（有可能重复）
        System.out.println(redisTemplate.opsForSet().randomMembers("games", 10));

        //随机获取集合中指定个数的元素（不重复）
        System.out.println(redisTemplate.opsForSet().distinctRandomMembers("games", 10));


    }

    /**
     * 批处理 smembers
     */
    @Test
    public void demo12() {
        RedisConnection conn = redisTemplate.getConnectionFactory().getConnection();
        String key = "test:set:keys";
        redisTemplate.delete(key);
        conn.del(redisTemplate.getStringSerializer().serialize(key));

        long bTime1 = System.currentTimeMillis();
        redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            for (int i = 0; i < 10000; i++) {
                connection.set(redisTemplate.getStringSerializer().serialize(key)
                        , redisTemplate.getStringSerializer().serialize("abc" + i));
            }
            return null;
        }, new StringRedisSerializer());
        long eTime1 = System.currentTimeMillis();
        System.out.println("共耗时：" + (eTime1 - bTime1));

        long bTime2 = System.currentTimeMillis();
//        Set<String> a =  redisTemplate.opsForSet().members(key);
        List<Object> a = redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                for (int i = 0; i < 10000; i++) {
                    connection.get(redisTemplate.getStringSerializer().serialize(key));
                }
                return null;
            }
        }, new StringRedisSerializer());
        long eTime2 = System.currentTimeMillis();
        System.out.println("共耗时：" + (eTime2 - bTime2));
        System.out.println("size::" + a.size());

        long bTime3 = System.currentTimeMillis();
//        Set<String> a =  redisTemplate.opsForSet().members(key);
        List<Object> aa = redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                try {
                    for (int i = 0; i < 10000; i++) {
                        connection.get(redisTemplate.getStringSerializer().serialize(key));
                    }
                } finally {
                    connection.closePipeline();
                }
                return null;
            }
        }, new StringRedisSerializer());
        long eTime3 = System.currentTimeMillis();
        System.out.println("共耗时：" + (eTime3 - bTime3));
        System.out.println("size::" + aa.size());


//        System.out.println("size::"+conn.hLen(redisTemplate.getStringSerializer().serialize(key)));

//        redisTemplate.opsForSet().remove(key, "abc*");
//        System.out.println("size::"+redisTemplate.opsForSet().size(key));
//        redisTemplate.delete(key);
        conn.del(redisTemplate.getStringSerializer().serialize(key));
    }

    /**
     * 批处理 smembers  lambda 版本
     */
    @Test
    public void demo13() {
        RedisConnection conn = redisTemplate.getConnectionFactory().getConnection();
        String key = "test:set:keys";
        conn.del(redisTemplate.getStringSerializer().serialize(key));

        long bTime1 = System.currentTimeMillis();
        redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            for (int i = 0; i < 10000; i++) {
                connection.sAdd(redisTemplate.getStringSerializer().serialize(key)
                        , redisTemplate.getStringSerializer().serialize("abc"));
            }
            return null;
        });
        long eTime1 = System.currentTimeMillis();
        System.out.println("共耗时：" + (eTime1 - bTime1));

//        long bTime2 = System.currentTimeMillis();
//        List<Object> a = redisTemplate.executePipelined((RedisCallback<String>) connection -> {
//            for(int i =0;i<10000;i++){
//                connection.sPop(redisTemplate.getStringSerializer().serialize(key));
//            }
//            return null;
//        },new StringRedisSerializer());
//        long eTime2 = System.currentTimeMillis();
//        System.out.println("共耗时："+(eTime2-bTime2));
//        System.out.println("size::"+a.size());

        long bTime3 = System.currentTimeMillis();
        List<Object> aa = redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            connection.openPipeline();
            connection.sMembers(redisTemplate.getStringSerializer().serialize(key));
            return null;
        }, new StringRedisSerializer());
        long eTime3 = System.currentTimeMillis();
        System.out.println("共耗时：" + (eTime3 - bTime3));
        System.out.println("size::" + aa.size());

        conn.del(redisTemplate.getStringSerializer().serialize(key));
    }

    /**
     * 打印集合
     *
     * @param key
     * @date 2019年2月12日
     * @author baipengfei
     */
    private void printSet(String key) {
        //获取所有成员
        Set<String> members = redisTemplate.opsForSet().members(key);
        System.out.println(members);
    }


    //封装为方法
    public Object sSet(String key, final List<String> list) {
        // 获取key编码方式
        final StringRedisSerializer stringRedisSerializer = (StringRedisSerializer) redisTemplate.getKeySerializer();
        //获取值编码方式
        final RedisSerializer<String> valueSerializer = (RedisSerializer<String>) redisTemplate.getValueSerializer();
        //获取key对应的byte[]
        final byte[] rawKey = stringRedisSerializer.serialize(key);
        redisTemplate.executePipelined(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                for (String str : list) {
                    byte[] rawStr = valueSerializer.serialize(str);
                    //在set中添加数据
                    connection.sAdd(rawKey, rawStr);
                }
                connection.closePipeline();
                return null;
            }
        });
        return null;
    }


}
