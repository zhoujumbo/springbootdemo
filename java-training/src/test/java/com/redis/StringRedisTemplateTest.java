package com.redis;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName StringRedisTemplateTest
 * @Description TODO
 * @Author jb.zhou
 * @Date 2020/5/12
 * @Version 1.0
 */
public class StringRedisTemplateTest {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;


    /**
     * redisTemplate.opsForValue();　　//操作字符串
     * redisTemplate.opsForHash();　　 //操作hash
     * redisTemplate.opsForList();　　 //操作list
     * redisTemplate.opsForSet();　　  //操作set
     * redisTemplate.opsForZSet();　 　//操作有序set
     */

    /**
     *
     */
    @Test
    public void test01() {

        stringRedisTemplate.getExpire("test"); //根据key获取过期时间

        stringRedisTemplate.getExpire("test",TimeUnit.SECONDS);//根据key获取过期时间并换算成指定单位

        stringRedisTemplate.boundValueOps("test").increment(-1);//val做-1操作

        stringRedisTemplate.boundValueOps("test").increment(1);//val +1

        stringRedisTemplate.delete("test");//根据key删除缓存

        stringRedisTemplate.hasKey("546545");//检查key是否存在，返回boolean值

        stringRedisTemplate.expire("red_123",1000 , TimeUnit.MILLISECONDS);//设置过期时间

        Long expire = stringRedisTemplate.boundHashOps("red_123").getExpire();//redis有效时间："+expire+"S" 判断有效时间是否大于0
    }

    /**
     * opsForValue
     */
    @Test
    public void test02() {

        stringRedisTemplate.opsForValue().set("test", "100",60*10, TimeUnit.SECONDS);//向redis里存入数据和设置缓存时间


        stringRedisTemplate.opsForValue().get("test"); //根据key获取缓存中的val
    }

    /**
     * opsForSet
     */
    @Test
    public void test03() {

        stringRedisTemplate.opsForSet().add("red_123", "1","2","3");//向指定key中存放set集合

        stringRedisTemplate.opsForSet().isMember("red_123", "1");//根据key查看集合中是否存在指定数据

        stringRedisTemplate.opsForSet().members("red_123");//根据key获取set集合
    }

    /**
     * opsForList
     */
    @Test
    public void test04() {
        //将数据添加到key对应的现有数据的左边
        Long redisList = stringRedisTemplate.opsForList().leftPush("redisList", "3");
        //
        Long size = stringRedisTemplate.opsForList().size("redisList");
        //从左往右遍历
        String leftPop = stringRedisTemplate.opsForList().leftPop("redisList");
        //从右往左遍历
        String rightPop = stringRedisTemplate.opsForList().rightPop("redisList");
        //查询全部元素
        List<String> range = stringRedisTemplate.opsForList().range("redisList", 0, -1);
        //查询前三个元素
        List<String> range1 = stringRedisTemplate.opsForList().range("redisList", 0, 3);
        //从左往右删除list中元素A  (1:从左往右 -1:从右往左 0:删除全部)
        Long remove = stringRedisTemplate.opsForList().remove("key", 1, "A");
    }

    /**
     * opsForHash
     */
    @Test
    public void test05() {
        //判断key对应的map中是否存在hash
        Boolean aBoolean = stringRedisTemplate.opsForHash().hasKey("hash", "hash1");
        //往key对应的map中新增(key1,value1)
        stringRedisTemplate.opsForHash().put("hash", "hash1", "value1");
        //获取key对应的map中hash1的值
        Object o = stringRedisTemplate.opsForHash().get("hash", "hash1");
        //删除key对应的map中多个子hash(可变参数)
        Long delete = stringRedisTemplate.opsForHash().delete("hash", "key1", "key2", "key3");
        //获取hash对应的map
        Map<Object, Object> hash = stringRedisTemplate.opsForHash().entries("hash");
        //获取hash对应的map中全部子hash集合
        Set<Object> hash1 = stringRedisTemplate.opsForHash().keys("hash");
        //获取hash对应的map中全部value集合
        List<Object> hash2 = stringRedisTemplate.opsForHash().values("hash");
    }

    /**
     * opsForZSet
     */
    @Test
    public void test06() {

        // add(K key, V value, double score)  添加元素到变量中同时指定元素的分值。
        redisTemplate.opsForZSet().add("zSetValue","A",1);
        redisTemplate.opsForZSet().add("zSetValue","B",3);
        redisTemplate.opsForZSet().add("zSetValue","C",2);
        redisTemplate.opsForZSet().add("zSetValue","D",5);

        // range(K key, long start, long end)   获取变量指定区间的元素。
        Set zSetValue = redisTemplate.opsForZSet().range("zSetValue",0,-1);
        System.out.println("通过range(K key, long start, long end)方法获取指定区间的元素:" + zSetValue);

        // rangeByLex(K key, RedisZSetCommands.Range range) 用于获取满足非score的排序取值。这个排序只有在有相同分数的情况下才能使用，如果有不同的分数则返回值不确定。
        RedisZSetCommands.Range range = new RedisZSetCommands.Range();
//range.gt("A");
        range.lt("D");
        zSetValue = redisTemplate.opsForZSet().rangeByLex("zSetValue", range);
        System.out.println("通过rangeByLex(K key, RedisZSetCommands.Range range)方法获取满足非score的排序取值元素:" + zSetValue);

        // rangeByLex(K key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit)
        //           用于获取满足非score的设置下标开始的长度排序取值。
        RedisZSetCommands.Limit limit = new RedisZSetCommands.Limit();
        limit.count(2);
        //起始下标为0
        limit.offset(1);
        zSetValue = redisTemplate.opsForZSet().rangeByLex("zSetValue", range,limit);
        System.out.println("通过rangeByLex(K key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit)方法获取满足非score的排序取值元素:" + zSetValue);

        // add(K key, Set<ZSetOperations.TypedTuple<V>> tuples)
        //   通过TypedTuple方式新增数据。
        ZSetOperations.TypedTuple<Object> typedTuple1 = new DefaultTypedTuple<Object>("E",6.0);
        ZSetOperations.TypedTuple<Object> typedTuple2 = new DefaultTypedTuple<Object>("F",7.0);
        ZSetOperations.TypedTuple<Object> typedTuple3 = new DefaultTypedTuple<Object>("G",5.0);
        Set<ZSetOperations.TypedTuple<Object>> typedTupleSet = new HashSet<ZSetOperations.TypedTuple<Object>>();
        typedTupleSet.add(typedTuple1);
        typedTupleSet.add(typedTuple2);
        typedTupleSet.add(typedTuple3);
        redisTemplate.opsForZSet().add("typedTupleSet",typedTupleSet);
        zSetValue = redisTemplate.opsForZSet().range("typedTupleSet",0,-1);
        System.out.println("通过add(K key, Set<ZSetOperations.TypedTuple<V>> tuples)方法添加元素:" + zSetValue);

        // rangeByScore(K key, double min, double max)
        //    根据设置的score获取区间值。
        zSetValue = redisTemplate.opsForZSet().rangeByScore("zSetValue",1,2);
        System.out.println("通过rangeByScore(K key, double min, double max)方法根据设置的score获取区间值:" + zSetValue);

        //


    }

    /**
     * StringRedisTemplate实现事务
     * 注意：StringRedisTemplate开启事务之后，不释放连接。如果我们使用Spring事务管理不存在这个问题
     */
    @Test
    public void test07() {
        stringRedisTemplate.setEnableTransactionSupport(true);
        try {
            stringRedisTemplate.multi();//开启事务
            stringRedisTemplate.opsForValue().increment("count", 1);
            stringRedisTemplate.opsForValue().increment("count1", 2);
            //提交
            stringRedisTemplate.exec();
        }catch (Exception e){
            //开启回滚
            stringRedisTemplate.discard();
        }
    }

    /**
     * StringRedisTemplate实现乐观锁
     */
    @Test
    public void test08() {
        int id = 1;
        redisTemplate.watch("key"); // 1
        redisTemplate.multi();
        redisTemplate.boundValueOps("key").set(""+id);
        List<Object> list= redisTemplate.exec();
        System.out.println(list);
        if(list != null ){
            //操作成功
            System.out.println(id+"操作成功");
        }else{
            //操作失败
            System.out.println(id+"操作失败");
        }
    }

    /**
     * StringRedisTemplate实现分布式锁
     */
    @Test
    public void test09() {
//        String lockKey = "key";
//        String lockValue = lockKey+System.currentTimeMillis();
//        // value需要记住用于解锁
//        while (true){
//            Boolean ifPresent = stringRedisTemplate
//                    .opsForValue().setIfAbsent("redis-lock:" + lockKey, lockValue, 3, TimeUnit.SECONDS);
//
//            if (ifPresent){
//                break;
//            }
//        }
//        //解锁
//        boolean result = false;
//        // value需要记住用于解锁
//        stringRedisTemplate.watch("redis-lock:" + lockKey);
//        String value = stringRedisTemplate.opsForValue().get("redis-lock:" + lockKey);
//        if (null == value){
//            result = true;
//        }else if (value.equals(lockValue)) {
//            stringRedisTemplate.delete("redis-lock:" + lockKey);
//            result = true;
//        }
//        stringRedisTemplate.unwatch();
    }

    /**
     * Redis缓存击穿、穿透和雪崩
     * 缓存击穿，是指一个key非常热点，在不停的扛着大并发，大并发集中对这一个点进
     * 行访问，当这个key在失效的瞬间，持续的大并发就穿破缓存，直接请求数据库，就
     * 像在一个屏障上凿开了一个洞
     *
     * 缓存穿透，是指查询一个数据库一定不存在的数据。正常的使用缓存流程大致是，
     * 数据查询先进行缓存查询，如果key不存在或者key已经过期，再对数据库进行查
     * 询，并把查询到的对象，放进缓存。如果数据库查询对象为空，则不放进缓存。
     * 解决办法是即使查出的对象为空，也放入缓存时间设短一点。
     *
     * 缓存雪崩，是指在某一个时间段，缓存集中过期失效。
     */


}
