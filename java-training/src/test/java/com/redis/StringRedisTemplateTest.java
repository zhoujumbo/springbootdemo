package com.redis;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;

import java.util.*;
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
     * redisTemplate.opsForZSet();　 　//操作有序zset
     */

    /**
     *
     */
    @Test
    public void test01() {

        stringRedisTemplate.getExpire("test"); //根据key获取过期时间

        stringRedisTemplate.getExpire("test", TimeUnit.SECONDS);//根据key获取过期时间并换算成指定单位

        stringRedisTemplate.boundValueOps("test").increment(-1);//val做-1操作

        stringRedisTemplate.boundValueOps("test").increment(1);//val +1

        stringRedisTemplate.delete("test");//根据key删除缓存

        stringRedisTemplate.hasKey("546545");//检查key是否存在，返回boolean值

        stringRedisTemplate.expire("red_123", 1000, TimeUnit.MILLISECONDS);//设置过期时间

        Long expire = stringRedisTemplate.boundHashOps("red_123").getExpire();//redis有效时间："+expire+"S" 判断有效时间是否大于0
    }

    /**
     * opsForValue
     */
    @Test
    public void test02() {

        /** set(K key, V value)  新增一个字符串类型的值，key是键，value是值 **/
        stringRedisTemplate.opsForValue().set("test", "100");

        /**  set(K key, V value, long timeout, TimeUnit unit)  设置变量值的过期时间 **/
        stringRedisTemplate.opsForValue().set("test", "100", 60 * 10, TimeUnit.SECONDS);/** 向redis里存入数据和设置缓存时间 **/

        /**  get(Object key)  获取key键对应的值 **/
        stringRedisTemplate.opsForValue().get("test"); /** 根据key获取缓存中的val **/

        /**  append(K key, String value)  在原有的值基础上新增字符串到末尾 **/
        stringRedisTemplate.opsForValue().append("key", "appendValue");

        /**  get(K key, long start, long end) 截取key键对应值得字符串，从开始下标位置开始到结束下标的位置(包含结束下标)的字符串 **/
        stringRedisTemplate.opsForValue().get("key", 0, 3);

        /**  getAndSet(K key, V value)  获取原来key键对应的值并重新赋新值 **/
        stringRedisTemplate.opsForValue().getAndSet("key", "ccc");

        /**  setBit(K key, long offset, boolean value)  key键对应的值value对应的ascii码,在offset的位置(从左向右数)变为value **/
        stringRedisTemplate.opsForValue().setBit("key", 1, false);

        /**  getBit(K key, long offset) 判断指定的位置ASCII码的bit位是否为1 **/
        boolean bitBoolean = stringRedisTemplate.opsForValue().getBit("key", 1);

        /**  size(K key) 获取指定字符串的长度 **/
        Long stringValueLength = stringRedisTemplate.opsForValue().size("key");

        /**  increment(K key, double delta) 以增量的方式将double值存储在变量中 **/
        double stringValueDouble = stringRedisTemplate.opsForValue().increment("doubleKey", 5);

        /**  increment(K key, long delta)  以增量的方式将long值存储在变量中 **/
        double stringValueLong = stringRedisTemplate.opsForValue().increment("longKey", 6);

        /**  setIfAbsent(K key, V value)  如果键不存在则新增,存在则不改变已经有的值 **/
        boolean absentBoolean = stringRedisTemplate.opsForValue().setIfAbsent("absentKey", "fff");

        /**  set(K key, V value, long offset)  覆盖从指定位置开始的值 **/
        stringRedisTemplate.opsForValue().set("absentKey", "dd", 1);

        /**  multiSet(Map<? extends K,? extends V> map)  设置map集合到redis **/
        Map valueMap = new HashMap();
        valueMap.put("valueMap1", "map1");
        valueMap.put("valueMap2", "map2");
        valueMap.put("valueMap3", "map3");
        stringRedisTemplate.opsForValue().multiSet(valueMap);

        /**  multiGet(Collection<K> keys)   根据集合取出对应的value值 **/
        List paraList = new ArrayList();
        paraList.add("valueMap1");
        paraList.add("valueMap2");
        paraList.add("valueMap3");
        List<String> valueList = stringRedisTemplate.opsForValue().multiGet(paraList);

        /**  multiSetIfAbsent(Map<? extends K,? extends V> map)    如果对应的map集合名称不存在，则添加；如果存在则不做修改 **/
        stringRedisTemplate.opsForValue().multiSetIfAbsent(valueMap);

    }

    /**
     * opsForSet
     * https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/SetOperations
     * .html#randomMember-K-
     */
    @Test
    public void test03() {

        /**  add(K key, V... values)  向变量中批量添加值 **/
        stringRedisTemplate.opsForSet().add("red_123", "1", "2", "3");//向指定key中存放set集合

        /**  isMember(K key, Object o) 检查给定的元素是否在变量中 **/
        boolean isMember = stringRedisTemplate.opsForSet().isMember("red_123", "1");/** 根据key查看集合中是否存在指定数据 **/

        /**  members(K key) 获取变量中的值 **/
        Set set = stringRedisTemplate.opsForSet().members("red_123");/** 根据key获取set集合 **/

        /**  size(K key) 获取变量中值的长度 **/
        long size = stringRedisTemplate.opsForSet().size("key");

        /**  randomMember(K key)  随机获取变量中的元素 **/
        Object randomMember = redisTemplate.opsForSet().randomMember("setValue");

        /**  randomMembers(K key, long count)  随机获取变量中指定个数的元素 **/
        List randomMembers = redisTemplate.opsForSet().randomMembers("setValue", 2);

        /**  move(K key, V value, K destKey) 转移变量的元素值到目的变量 **/
        boolean isMove = redisTemplate.opsForSet().move("setValue", "A", "destSetValue");

        /**  pop(K key) 弹出变量中的元素 **/
        Object popValue = redisTemplate.opsForSet().pop("setValue");

        /**  remove(K key, Object... values) 批量移除变量中的元素 **/
        long removeCount = redisTemplate.opsForSet().remove("setValue", "E", "F", "G");

        /**  scan(K key, ScanOptions options) 匹配获取键值对，ScanOptions.NONE为获取全部键值对； **/
        /**  ScanOptions.scanOptions().match("C").build()匹配获取键位map1的键值对,不能模糊匹配 **/
        /** Cursor<Object> cursor = redisTemplate.opsForSet().scan("setValue", ScanOptions.NONE); **/
        Cursor<Object> cursor = redisTemplate.opsForSet().scan("setValue", ScanOptions.scanOptions().match("C").build());
        while (cursor.hasNext()) {
            Object object = cursor.next();
            System.out.println("通过scan(K key, ScanOptions options)方法获取匹配的值:" + object);
        }

        /**  difference(K key, Collection<K> otherKeys) 通过集合求差值 **/
        List list = new ArrayList();
        list.add("destSetValue");
        Set differenceSet = redisTemplate.opsForSet().difference("setValue", list);
        System.out.println("通过difference(K key, Collection<K> otherKeys)方法获取变量中与给定集合中变量不一样的值:" + differenceSet);

        /**  difference(K key, K otherKey)  通过给定的key求2个set变量的差值 **/
        differenceSet = redisTemplate.opsForSet().difference("setValue", "destSetValue");
        System.out.println("通过difference(K key, Collection<K> otherKeys)方法获取变量中与给定变量不一样的值:" + differenceSet);

        /**  differenceAndStore(K key, K otherKey, K destKey)  将求出来的差值元素保存 **/
        redisTemplate.opsForSet().differenceAndStore("setValue", "destSetValue", "storeSetValue");
        set = redisTemplate.opsForSet().members("storeSetValue");
        System.out.println("通过differenceAndStore(K key, K otherKey, K destKey)方法将求出来的差值元素保存:" + set);

        /**  differenceAndStore(K key, Collection<K> otherKeys, K destKey) **/
        /**     将求出来的差值元素保存 **/
        redisTemplate.opsForSet().differenceAndStore("setValue", list, "storeSetValue");
        set = redisTemplate.opsForSet().members("storeSetValue");
        System.out.println("通过differenceAndStore(K key, Collection<K> otherKeys, K destKey)方法将求出来的差值元素保存:" + set);

        /**  distinctRandomMembers(K key, long count) 获取去重的随机元素 **/
        set = redisTemplate.opsForSet().distinctRandomMembers("setValue", 2);
        System.out.println("通过distinctRandomMembers(K key, long count)方法获取去重的随机元素:" + set);

        /**  intersect(K key, K otherKey)  获取2个变量中的交集 **/
        set = redisTemplate.opsForSet().intersect("setValue", "destSetValue");
        System.out.println("通过intersect(K key, K otherKey)方法获取交集元素:" + set);

        /**  intersect(K key, Collection<K> otherKeys) **/
        /**     获取多个变量之间的交集 **/
        set = redisTemplate.opsForSet().intersect("setValue", list);
        System.out.println("通过intersect(K key, Collection<K> otherKeys)方法获取交集元素:" + set);

        /**  intersectAndStore(K key, K otherKey, K destKey) **/
        /**      获取2个变量交集后保存到最后一个参数上 **/
        redisTemplate.opsForSet().intersectAndStore("setValue", "destSetValue", "intersectValue");
        set = redisTemplate.opsForSet().members("intersectValue");
        System.out.println("通过intersectAndStore(K key, K otherKey, K destKey)方法将求出来的交集元素保存:" + set);

        /**  intersectAndStore(K key, Collection<K> otherKeys, K destKey) **/
        /**      获取多个变量的交集并保存到最后一个参数上 **/
        redisTemplate.opsForSet().intersectAndStore("setValue", list, "intersectListValue");
        set = redisTemplate.opsForSet().members("intersectListValue");
        System.out.println("通过intersectAndStore(K key, Collection<K> otherKeys, K destKey)方法将求出来的交集元素保存:" + set);


    }

    /**
     * opsForList
     */
    @Test
    public void test04() {

        /**  set(K key, long index, V value) 在集合的指定位置插入元素,如果指定位置已有元素，则覆盖，没有则新增，超过集合下标+n则会报错  **/
        redisTemplate.opsForList().set("presentList", 3, "15");

        /**  leftPush(K key, V value)  将数据添加到key对应的现有数据的左边  **/
        Long redisList = stringRedisTemplate.opsForList().leftPush("redisList", "3");

        /**  index(K key, long index) 获取集合指定位置的值  **/
        String listValue = redisTemplate.opsForList().index("list", 1) + "";

        /**  range(K key, long start, long end) 查询全部元素  **/
        List<String> range = stringRedisTemplate.opsForList().range("redisList", 0, -1);
        /**  查询前三个元素  **/
        List<String> range1 = stringRedisTemplate.opsForList().range("redisList", 0, 3);

        /**  leftPush(K key, V pivot, V value) 把最后一个参数值放到指定集合的第一个出现中间参数的前面，如果中间参数值存在的话  **/
        redisTemplate.opsForList().leftPush("list", "a", "n");
        List list = redisTemplate.opsForList().range("list", 0, -1);
        System.out.println("通过leftPush(K key, V pivot, V value)方法把值放到指定参数值前面:" + list);

        /**  leftPushAll(K key, V... values)  向左边批量添加参数元素  **/
        redisTemplate.opsForList().leftPushAll("list", "w", "x", "y");

        /**  leftPushAll(K key, Collection<V> values) 以集合的方式向左边批量添加元素  **/
        List newList = new ArrayList();
        newList.add("o");
        newList.add("p");
        newList.add("q");
        redisTemplate.opsForList().leftPushAll("list", newList);

        /**  leftPushIfPresent(K key, V value) 如果存在集合则添加元素  **/
        redisTemplate.opsForList().leftPushIfPresent("presentList", "o");

        /**  rightPush(K key, V value) 向集合最右边添加元素  **/
        redisTemplate.opsForList().rightPush("list", "w");

        /**  rightPush(K key, V pivot, V value) 向集合中第一次出现第二个参数变量元素的右边添加第三个参数变量的元素值  **/
        redisTemplate.opsForList().rightPush("list", "w", "r");

        /**  rightPushAll(K key, V... values) 向右边批量添加元素  **/
        redisTemplate.opsForList().rightPushAll("list", "j", "k");

        /**  rightPushAll(K key, Collection<V> values)   以集合方式向右边添加元素  **/
        redisTemplate.opsForList().rightPushAll("list", newList);

        /**  rightPushIfPresent(K key, V value) 向已存在的集合中添加元素  **/
        redisTemplate.opsForList().rightPushIfPresent("presentList", "d");

        /**  size(K key) 获取集合长度  **/
        Long size = stringRedisTemplate.opsForList().size("redisList");

        /**  leftPop(K key) 移除集合中的左边第一个元素  **/
        String leftPop = stringRedisTemplate.opsForList().leftPop("redisList");

        /**  leftPop(K key, long timeout, TimeUnit unit)   移除集合中左边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出  **/
        Object popValue = redisTemplate.opsForList().leftPop("presentList", 1, TimeUnit.SECONDS);

        /**  rightPop(K key) 移除集合中右边的元素  **/
        String rightPop = stringRedisTemplate.opsForList().rightPop("redisList");

        /**  rightPop(K key, long timeout, TimeUnit unit)  移除集合中右边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出  **/
        redisTemplate.opsForList().rightPop("presentList", 1, TimeUnit.SECONDS);

        /**  rightPopAndLeftPush(K sourceKey, K destinationKey) 移除集合中右边的元素，同时在左边加入一个元素  **/
        redisTemplate.opsForList().rightPopAndLeftPush("list", "12");

        /**  rightPopAndLeftPush(K sourceKey, K destinationKey, long timeout, TimeUnit unit)  **/
        /** 移除集合中右边的元素在等待的时间里，同时在左边添加元素，如果超过等待的时间仍没有元素则退出
         */
        redisTemplate.opsForList().rightPopAndLeftPush("presentList", "13", 1, TimeUnit.SECONDS);

        /**  remove(K key, long count, Object value)  从左往右删除list中元素A  (1:从左往右 -1:从右往左 0:删除全部)  **/
        /**  从存储在键中的列表中删除等于值的元素的第一个计数事件。count> 0：删除等于从左到右移动的值的第一个元素；  **/
        /**  count< 0：删除等于从右到左移动的值的第一个元素；count = 0：删除等于value的所有元素  **/
        Long remove = stringRedisTemplate.opsForList().remove("key", 1, "A");

        /**  trim(K key, long start, long end)  截取集合元素长度，保留长度内的数据  **/
        redisTemplate.opsForList().trim("list", 0, 5);
    }

    /**
     * opsForHash
     */
    @Test
    public void test05() {

        // put(H key, HK hashKey, HV value)  新增hashMap值
        redisTemplate.opsForHash().put("hashValue", "map1", "map1-1");

        // values(H key) 获取hash对应的map中全部value集合
        List<Object> hash2 = stringRedisTemplate.opsForHash().values("hash");

        // entries(H key)  获取变量中的键值对
        Map<Object, Object> hash = stringRedisTemplate.opsForHash().entries("hash");

        // get(H key, Object hashKey) 获取key对应的map中hash1的值
        Object o = stringRedisTemplate.opsForHash().get("hash", "hash1");

        // hasKey(H key, Object hashKey)  判断key对应的map中是否存在hash
        Boolean aBoolean = stringRedisTemplate.opsForHash().hasKey("hash", "hash1");

        // keys(H key)   获取变量中的键
        Set<Object> hash1 = stringRedisTemplate.opsForHash().keys("hash");

        // size(H key)  获取变量的长度
        long hashLength = redisTemplate.opsForHash().size("hashValue");

        // increment(H key, HK hashKey, double delta) 使变量中的键以double值的大小进行自增长
        double hashIncDouble = redisTemplate.opsForHash().increment("hashInc", "map1", 3);

        // increment(H key, HK hashKey, long delta) 使变量中的键以long值的大小进行自增长
        long hashIncLong = redisTemplate.opsForHash().increment("hashInc", "map2", 6);

        // multiGet(H key, Collection<HK> hashKeys) 以集合的方式获取变量中的值
        List<Object> list = new ArrayList<Object>();
        list.add("map1");
        list.add("map2");
        List mapValueList = redisTemplate.opsForHash().multiGet("hashValue", list);
        System.out.println("通过multiGet(H key, Collection<HK> hashKeys)方法以集合的方式获取变量中的值:" + mapValueList);

        // putAll(H key, Map<? extends HK,? extends HV> m)  以map集合的形式添加键值对
        Map newMap = new HashMap();
        newMap.put("map3", "map3-3");
        newMap.put("map5", "map5-5");
        redisTemplate.opsForHash().putAll("hashValue", newMap);
        Map map = redisTemplate.opsForHash().entries("hashValue");
        System.out.println("通过putAll(H key, Map<? extends HK,? extends HV> m)方法以map集合的形式添加键值对:" + map);

        // putIfAbsent(H key, HK hashKey, HV value)
        // 如果变量值存在，在变量中可以添加不存在的的键值对，如果变量不存在，则新增一个变量，同时将键值对添加到该变量
        redisTemplate.opsForHash().putIfAbsent("hashValue", "map6", "map6-6");
        map = redisTemplate.opsForHash().entries("hashValue");
        System.out.println("通过putIfAbsent(H key, HK hashKey, HV value)方法添加不存在于变量中的键值对:" + map);

        // scan(H key, ScanOptions options)
        // 匹配获取键值对，ScanOptions.NONE为获取全部键对，ScanOptions.scanOptions().match("map1").build()     匹配获取键位map1的键值对,不能模糊匹配
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan("hashValue", ScanOptions.scanOptions().match("map1").build());
        //Cursor<Map.Entry<Object,Object>> cursor = redisTemplate.opsForHash().scan("hashValue",ScanOptions.NONE);
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            System.out.println("通过scan(H key, ScanOptions options)方法获取匹配键值对:" + entry.getKey() + "---->" + entry.getValue());
        }

        // delete(H key, Object... hashKeys) 删除变量中的键值对，可以传入多个参数，删除多个键值对
        Long delete = stringRedisTemplate.opsForHash().delete("hash", "key1", "key2", "key3");
    }

    /**
     * opsForZSet
     */
    @Test
    public void test06() {

        // add(K key, V value, double score)  添加元素到变量中同时指定元素的分值。
        redisTemplate.opsForZSet().add("zSetValue", "A", 1);
        redisTemplate.opsForZSet().add("zSetValue", "B", 3);
        redisTemplate.opsForZSet().add("zSetValue", "C", 2);
        redisTemplate.opsForZSet().add("zSetValue", "D", 5);

        // range(K key, long start, long end)   获取变量指定区间的元素。
        Set zSetValue = redisTemplate.opsForZSet().range("zSetValue", 0, -1);
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
        zSetValue = redisTemplate.opsForZSet().rangeByLex("zSetValue", range, limit);
        System.out.println("通过rangeByLex(K key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit)方法获取满足非score的排序取值元素:" + zSetValue);

        // add(K key, Set<ZSetOperations.TypedTuple<V>> tuples)
        //   通过TypedTuple方式新增数据。
        ZSetOperations.TypedTuple<Object> typedTuple1 = new DefaultTypedTuple<Object>("E", 6.0);
        ZSetOperations.TypedTuple<Object> typedTuple2 = new DefaultTypedTuple<Object>("F", 7.0);
        ZSetOperations.TypedTuple<Object> typedTuple3 = new DefaultTypedTuple<Object>("G", 5.0);
        Set<ZSetOperations.TypedTuple<Object>> typedTupleSet = new HashSet<ZSetOperations.TypedTuple<Object>>();
        typedTupleSet.add(typedTuple1);
        typedTupleSet.add(typedTuple2);
        typedTupleSet.add(typedTuple3);
        redisTemplate.opsForZSet().add("typedTupleSet", typedTupleSet);
        zSetValue = redisTemplate.opsForZSet().range("typedTupleSet", 0, -1);
        System.out.println("通过add(K key, Set<ZSetOperations.TypedTuple<V>> tuples)方法添加元素:" + zSetValue);

        // rangeByScore(K key, double min, double max)
        //    根据设置的score获取区间值。
        zSetValue = redisTemplate.opsForZSet().rangeByScore("zSetValue", 1, 2);
        System.out.println("通过rangeByScore(K key, double min, double max)方法根据设置的score获取区间值:" + zSetValue);

        // rangeByScore(K key, double min, double max,long offset, long count) 根据设置的score获取区间值从给定下标和给定长度获取最终值
        zSetValue = redisTemplate.opsForZSet().rangeByScore("zSetValue", 1, 5, 1, 3);
        System.out.println("通过rangeByScore(K key, double min, double max, long offset, long count)方法根据设置的score获取区间值:" + zSetValue);

        // rangeWithScores(K key, long start, long end)  获取RedisZSetCommands.Tuples的区间值
        Set<ZSetOperations.TypedTuple<Object>> typedTupleSet1 = redisTemplate.opsForZSet().rangeWithScores(
                "typedTupleSet", 1, 3);

        // rangeByScoreWithScores(K key, double min, double max)
        //   获取RedisZSetCommands.Tuples的区间值通过分值
        Set<ZSetOperations.TypedTuple<Object>> typedTupleSet2 = redisTemplate.opsForZSet().rangeByScoreWithScores(
                "typedTupleSet", 5, 8);

        // rangeByScoreWithScores(K key, double min, double max, long offset, long count)
        // 获取RedisZSetCommands.Tuples的区间值从给定下标和给定长度获取最终值通过分值
        Set<ZSetOperations.TypedTuple<Object>> typedTupleSet3 = redisTemplate.opsForZSet().rangeByScoreWithScores(
                "typedTupleSet", 5, 8, 1, 1);

        // count(K key, double min, double max) 获取区间值的个数
        long count = redisTemplate.opsForZSet().count("zSetValue", 1, 5);
        System.out.println("通过count(K key, double min, double max)方法获取区间值的个数:" + count);

        // rank(K key, Object o) 获取变量中元素的索引,下标开始位置为0
        long index = redisTemplate.opsForZSet().rank("zSetValue", "B");

        // scan(K key, ScanOptions options)
        // 匹配获取键值对，ScanOptions.NONE为获取全部键值对；
        // ScanOptions.scanOptions().match("C").build()匹配获取键位map1的键值对,不能模糊匹配
        Cursor<ZSetOperations.TypedTuple<Object>> cursor = redisTemplate.opsForZSet().scan("zSetValue", ScanOptions.NONE);
        while (cursor.hasNext()) {
            ZSetOperations.TypedTuple<Object> typedTuple = cursor.next();
            System.out.println("通过scan(K key, ScanOptions options)方法获取匹配元素:" + typedTuple.getValue() + "--->" + typedTuple.getScore());
        }

        // score(K key, Object o)  获取元素的分值
        double score = redisTemplate.opsForZSet().score("zSetValue", "B");

        // zCard(K key) 获取变量中元素的个数
        long zCard = redisTemplate.opsForZSet().zCard("zSetValue");

        // incrementScore(K key, V value, double delta)  修改变量中的元素的分值
        double incrementScore = redisTemplate.opsForZSet().incrementScore("zSetValue", "C", 5);
        System.out.print("通过incrementScore(K key, V value, double delta)方法修改变量中的元素的分值:" + incrementScore);
        score = redisTemplate.opsForZSet().score("zSetValue", "C");
        System.out.print(",修改后获取元素的分值:" + score);
        zSetValue = redisTemplate.opsForZSet().range("zSetValue", 0, -1);
        System.out.println("，修改后排序的元素:" + zSetValue);

        // reverseRange(K key, long start, long end) 索引倒序排列指定区间元素
        zSetValue = redisTemplate.opsForZSet().reverseRange("zSetValue", 0, -1);
        System.out.println("通过reverseRange(K key, long start, long end)方法倒序排列元素:" + zSetValue);

        // reverseRangeByScore(K key, double min, double max)  倒序排列指定分值区间元素
        zSetValue = redisTemplate.opsForZSet().reverseRangeByScore("zSetValue", 1, 5);
        System.out.println("通过reverseRangeByScore(K key, double min, double max)方法倒序排列指定分值区间元素:" + zSetValue);

        // reverseRangeByScore(K key, double min, double max, long offset, long count)    倒序排列从给定下标和给定长度分值区间元素
        zSetValue = redisTemplate.opsForZSet().reverseRangeByScore("zSetValue", 1, 5, 1, 2);
        System.out.println("通过reverseRangeByScore(K key, double min, double max, long offset, long count)方法倒序排列从给定下标和给定长度分值区间元素:" + zSetValue);

        // reverseRangeByScoreWithScores(K key, double min, double max) 倒序排序获取RedisZSetCommands.Tuples的分值区间值
        Set<ZSetOperations.TypedTuple<Object>> typedTupleSet4 =
                redisTemplate.opsForZSet().reverseRangeByScoreWithScores("zSetValue", 1, 5);

        // reverseRangeByScoreWithScores(K key, double min, double max, long offset, long count)
        // 倒序排序获取RedisZSetCommands.Tuples的从给定下标和给定长度分值区间值
        Set<ZSetOperations.TypedTuple<Object>> typedTupleSet5 =
                redisTemplate.opsForZSet().reverseRangeByScoreWithScores("zSetValue", 1, 5, 1, 2);

        // reverseRangeWithScores(K key, long start, long end) 索引倒序排列区间值
        Set<ZSetOperations.TypedTuple<Object>> typedTupleSet6 = redisTemplate.opsForZSet().reverseRangeWithScores(
                "zSetValue", 1, 5);

        // reverseRank(K key, Object o)   获取倒序排列的索引值
        long reverseRank = redisTemplate.opsForZSet().reverseRank("zSetValue", "B");

        // intersectAndStore(K key, K otherKey, K destKey)  获取2个变量的交集存放到第3个变量里面
        redisTemplate.opsForZSet().intersectAndStore("zSetValue", "typedTupleSet", "intersectSet");
        zSetValue = redisTemplate.opsForZSet().range("intersectSet", 0, -1);
        System.out.println("通过intersectAndStore(K key, K otherKey, K destKey)方法获取2个变量的交集存放到第3个变量里面:" + zSetValue);

        // intersectAndStore(K key, Collection<K> otherKeys, K destKey)
        //   获取多个变量的交集存放到第3个变量里面
        List list = new ArrayList();
        list.add("typedTupleSet");
        redisTemplate.opsForZSet().intersectAndStore("zSetValue", list, "intersectListSet");
        zSetValue = redisTemplate.opsForZSet().range("intersectListSet", 0, -1);
        System.out.println("通过intersectAndStore(K key, Collection<K> otherKeys, K destKey)方法获取多个变量的交集存放到第3个变量里面:" + zSetValue);

        // unionAndStore(K key, K otherKey, K destKey)  获取2个变量的合集存放到第3个变量里面
        redisTemplate.opsForZSet().unionAndStore("zSetValue", "typedTupleSet", "unionSet");
        zSetValue = redisTemplate.opsForZSet().range("unionSet", 0, -1);
        System.out.println("通过unionAndStore(K key, K otherKey, K destKey)方法获取2个变量的交集存放到第3个变量里面:" + zSetValue);

        // unionAndStore(K key, Collection<K> otherKeys, K destKey) 获取多个变量的合集存放到第3个变量里面
        redisTemplate.opsForZSet().unionAndStore("zSetValue", list, "unionListSet");
        zSetValue = redisTemplate.opsForZSet().range("unionListSet", 0, -1);
        System.out.println("通过unionAndStore(K key, Collection<K> otherKeys, K destKey)方法获取多个变量的交集存放到第3个变量里面:" + zSetValue);

        // remove(K key, Object... values)  批量移除元素根据元素值
        long removeCount = redisTemplate.opsForZSet().remove("unionListSet", "A", "B");
        zSetValue = redisTemplate.opsForZSet().range("unionListSet", 0, -1);
        System.out.print("通过remove(K key, Object... values)方法移除元素的个数:" + removeCount);
        System.out.println(",移除后剩余的元素:" + zSetValue);

        // removeRangeByScore(K key, double min, double max)
        //   根据分值移除区间元素
        removeCount = redisTemplate.opsForZSet().removeRangeByScore("unionListSet", 3, 5);
        zSetValue = redisTemplate.opsForZSet().range("unionListSet", 0, -1);
        System.out.print("通过removeRangeByScore(K key, double min, double max)方法移除元素的个数:" + removeCount);
        System.out.println(",移除后剩余的元素:" + zSetValue);

        // removeRange(K key, long start, long end)
        //         根据索引值移除区间元素
        removeCount = redisTemplate.opsForZSet().removeRange("unionListSet", 3, 5);
        zSetValue = redisTemplate.opsForZSet().range("unionListSet", 0, -1);
        System.out.print("通过removeRange(K key, long start, long end)方法移除元素的个数:" + removeCount);
        System.out.println(",移除后剩余的元素:" + zSetValue);
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
        } catch (Exception e) {
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
        redisTemplate.boundValueOps("key").set("" + id);
        List<Object> list = redisTemplate.exec();
        System.out.println(list);
        if (list != null) {
            //操作成功
            System.out.println(id + "操作成功");
        } else {
            //操作失败
            System.out.println(id + "操作失败");
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
