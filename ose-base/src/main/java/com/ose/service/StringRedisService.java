package com.ose.service;

import com.ose.util.LongUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public abstract class StringRedisService extends BaseService {

    private final static Logger logger = LoggerFactory.getLogger(StringRedisService.class);

    // Redis 模板
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 构造方法。
     *
     * @param stringRedisTemplate Redis 模板
     */
    public StringRedisService(
        StringRedisTemplate stringRedisTemplate
    ) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 设置缓存的值。
     *
     * @param key   KEY
     * @param value 值
     */
    protected void setRedisKey(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置缓存的值。
     *
     * @param key     KEY
     * @param value   值
     * @param seconds TTL 秒
     */
    protected void setRedisKey(String key, String value, int seconds) {
        stringRedisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    /**
     * 取得缓存的值。
     *
     * @param key KEY
     * @return 值
     */
    protected String getRedisKey(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 取得缓存的值。
     *
     * @param key     KEY
     * @param seconds 延长 TTL 秒
     * @return 值
     */
    protected String getRedisKey(String key, int seconds) {

        String value = getRedisKey(key);

        if (value != null) {
            stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
        }

        return value;
    }

    /**
     * 删除缓存的值。
     *
     * @param key KEY
     */
    protected void deleteRedisKey(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 判断是否存在缓存的值。
     *
     * @param key KEY
     * @return 是否存在
     */
    protected Boolean hasRedisKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }


    /**
     * 设置Hash的属性
     * @param key
     * @param field
     * @param value
     * @return
     */
    public boolean hset(String key, String field, String value){
        //If the field already exists, and the HSET just produced an update of the value, 0 is returned,
        //otherwise if a new field is created 1 is returned.
        stringRedisTemplate.opsForHash().put(key, field, value);
        return true;
    }

    /**
     * 设置Hash的属性
     * @param key
     * @param field
     * @param value
     * @return
     */
    public boolean hset(String key, String field, String value, int seconds){
        stringRedisTemplate.opsForHash().put(key, field, value);
        stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
        return true;
    }

    /**
     * 批量设置Hash的属性
     * @param key
     * @param fields
     * @param values
     * @return
     */
    public boolean hmset(String key, String[] fields, String[] values){
        Map<String, String> hash = new HashMap<String, String>();
        for (int i=0; i<fields.length; i++) {
            hash.put(fields[i], values[i]);
        }
        stringRedisTemplate.opsForHash().putAll(key, hash);
        return true;
    }

    /**
     * 批量设置Hash的属性
     * @param key
     * @param map Map<String, String>
     * @return
     */
    public boolean hmset(String key, Map<String, String> map){
        stringRedisTemplate.opsForHash().putAll(key, map);

        return true;
    }

    /**
     * 仅当field不存在时设置值，成功返回true
     * @param key
     * @param field
     * @param value
     * @return
     */
    public boolean hsetNX(String key, String field, String value){
        //If the field already exists, 0 is returned,
        //otherwise if a new field is created 1 is returned.
        return stringRedisTemplate.opsForHash().putIfAbsent(key, field, value);

    }

    /**
     * 获取属性的值
     * @param key
     * @param field
     * @return
     */
    public String hget(String key, String field){

        return (String)stringRedisTemplate.opsForHash().get(key, field);

    }

    /**
     * 获取属性的值
     * @param key
     * @param field
     * @return
     */

    public Long hgetLong(String key, String field){

        return LongUtils.parseLong((String)stringRedisTemplate.opsForHash().get(key, field));

    }

    /**
     * 批量获取属性的值
     * @param key
     * @param fields String...
     * @return
     */
    public List<Object> hmget(String key, String... fields){

        List<Object> values = stringRedisTemplate.opsForHash().multiGet(key, Collections.singletonList(fields));
        return values;
    }

    /**
     * 获取在哈希表中指定 key 的所有字段和值
     * @param key
     * @return Map<String, String>
     */
    public Map<Object, Object> hgetAll(String key){

        Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(key);
        return map;
    }

    /**
     * 删除hash的属性
     * @param key
     * @param fields
     * @return
     */
    public boolean hdel(String key, String... fields){

        stringRedisTemplate.opsForHash().delete(key, fields);
        //System.out.println("statusCode="+statusCode);
        return true;
    }

    /**
     * 查看哈希表 key 中，指定的字段是否存在。
     * @param key
     * @param field
     * @return
     */
    public boolean hexists(String key, String field){

        return stringRedisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment 。
     * @param key
     * @param field
     * @param increment 正负数、0、正整数
     * @return
     */
    public long hincrBy(String key, String field, long increment){
        return stringRedisTemplate.opsForHash().increment(key, field, increment);
    }

    /**
     * 为哈希表 key 中的指定字段的浮点数值加上增量 increment 。(注：如果field不存在时，会设置新的值)
     * @param key
     * @param field
     * @param increment，可以为负数、正数、0
     * @return
     */
    public Double hincrByFloat(String key, String field, double increment){
        return stringRedisTemplate.opsForHash().increment(key, field, increment);
    }

    /**
     * 获取所有哈希表中的字段
     * @param key
     * @return Set<String>
     */
    public Set<Object> hkeys(String key){
        return stringRedisTemplate.opsForHash().keys(key);
    }

    /**
     * 获取哈希表中所有值
     * @param key
     * @return List<String>
     */
    public List<Object> hvals(String key){
        return stringRedisTemplate.opsForHash().values(key);
    }

    /**
     * 获取哈希表中字段的数量，当key不存在时，返回0
     * @param key
     * @return
     */
    public  Long hlen(String key){
        return stringRedisTemplate.opsForHash().size(key);
    }

    /**
     * 迭代哈希表中的键值对。
     * @param key
     * @param cursor
     * @return ScanResult<Entry<String, String>>
     */
    public Cursor<Map.Entry<Object, Object>> hscan(String key, ScanOptions cursor){
        Cursor<Map.Entry<Object, Object>> scanResult = stringRedisTemplate.opsForHash().scan(key, cursor);
        //System.out.println(scanResult.getResult());
        return scanResult;
    }


    /**
     * 向集合添加一个或多个成员，返回添加成功的数量
     * @param key
     * @param members
     * @return Long
     */
    public  Long sadds(String key, String... members){
        return stringRedisTemplate.opsForSet().add(key, members);
    }

    /**
     * 向集合添加一个或多个成员，返回添加成功的数量
     * @param key
     * @param member
     * @return Long
     */
    public  Long sadd(String key, String member){
        return stringRedisTemplate.opsForSet().add(key, member);
    }

    /**
     * 获取集合的成员数
     * @param key
     * @return
     */
    public  Long scard(String key){
        return stringRedisTemplate.opsForSet().size(key);
    }

    /**
     * 返回集合中的所有成员
     * @param key
     * @return Set<String>
     */
    public  Set<String> smembers(String key){
        return stringRedisTemplate.opsForSet().members(key);
    }

    /**
     * 判断 member 元素是否是集合 key 的成员，在集合中返回True
     * @param key
     * @param member
     * @return Boolean
     */
    public  Boolean sIsMember(String key, String member){
        return stringRedisTemplate.opsForSet().isMember(key, member);
    }

    /**
     * 返回给定所有集合的差集（获取第一个key中与其它key不相同的值，当只有一个key时，就返回这个key的所有值）
     * @param key1 key2
     * @return Set<String>
     */
    public  Set<String> sdiff(String key1, String key2){
        return stringRedisTemplate.opsForSet().difference(key1, key2);
    }

    /**
     * 返回给定所有集合的差集并存储在 targetKey中，类似sdiff，只是该方法把返回的差集保存到targetKey中
     * <li>当有差集时，返回true</li>
     * <li>当没有差集时，返回false</li>
     * @param targetKey
     * @param key1
     * @return
     */
    public  Long sdiffStore(String targetKey, String key1){
        Long statusCode = stringRedisTemplate.opsForSet().differenceAndStore(targetKey, key1, targetKey);
        return statusCode;
    }

    /**
     * 返回给定所有集合的交集（获取第一个key中与其它key相同的值，要求所有key都要有相同的值，如果没有相同，返回Null。当只有一个key时，就返回这个key的所有值）
     * @param key1
     * @return Set<String>
     */
    public  Set<String> sinter(String key1, String key2){
        Set<String> values = stringRedisTemplate.opsForSet().intersect(key1, key2);

        return values;
    }

    /**
     * 返回给定所有集合的交集并存储在 targetKey中，类似sinter
     * @param targetKey
     * @param key1
     * @return boolean
     */
    public  Long sinterStore(String targetKey, String key1){
        Long statusCode = stringRedisTemplate.opsForSet().intersectAndStore(targetKey, key1, targetKey);

        return statusCode;
    }

    /**
     * 将 member 元素从 sourceKey 集合移动到 targetKey 集合
     * <li>成功返回true</li>
     * <li>当member不存在于sourceKey时，返回false</li>
     * <li>当sourceKey不存在时，也返回false</li>
     * @param sourceKey
     * @param targetKey
     * @param member
     * @return boolean
     */
    public  boolean smove(String sourceKey, String targetKey, String member){
        return stringRedisTemplate.opsForSet().move(sourceKey, targetKey, member);

    }

    /**
     * 移除并返回集合中的一个随机元素
     * <li>当set为空或者不存在时，返回Null</li>
     * @param key
     * @return String
     */
    public  String spop(String key){
        String value = stringRedisTemplate.opsForSet().pop(key);

        return value;
    }

    /**
     * 返回集合中一个或多个随机数
     * <li>当count大于set的长度时，set所有值返回，不会抛错。</li>
     * <li>当count等于0时，返回[]</li>
     * <li>当count小于0时，也能返回。如-1返回一个，-2返回两个</li>
     * @param key
     * @param count
     * @return List<String>
     */
    public  List<String> srandMember(String key, int count){
        List<String> values = stringRedisTemplate.opsForSet().randomMembers(key, count);

        return values;
    }

    /**
     * 移除集合中一个或多个成员
     * @param key
     * @param members
     * @return
     */
    public  boolean srem(String key, String... members){
        //Integer reply, specifically: 1 if the new element was removed
        //0 if the new element was not a member of the set
        Long value = stringRedisTemplate.opsForSet().remove(key, members);

        if(value > 0){
            return true;
        }
        return false;
    }

    /**
     * 返回所有给定集合的并集，相同的只会返回一个
     * @param key1
     * @return
     */
    public  Set<String> sunion(String key1, String key2){
        Set<String> values = stringRedisTemplate.opsForSet().union(key1, key2);

        return values;
    }

    /**
     * 所有给定集合的并集存储在targetKey集合中
     * <li>注：合并时，只会把keys中的集合返回，不包括targetKey本身</li>
     * <li>如果targetKey本身是有值的，合并后原来的值是没有的，因为把keys的集合重新赋值给targetKey</li>
     * <li>要想保留targetKey本身的值，keys要包含原来的targetKey</li>
     * @param targetKey
     * @param key1
     * @return
     */
    public  boolean sunionStore(String targetKey, String key1){
        //返回合并后的长度
        Long statusCode = stringRedisTemplate.opsForSet().unionAndStore(targetKey, key1, targetKey);
//        System.out.println("statusCode="+statusCode);

        if(statusCode > 0){
            return true;
        }
        return false;
    }

    /** ---------------------------------- redis消息队列 ---------------------------------- */
    /**
     * 存值
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean lpush(String key, String value) {
        try {
            stringRedisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 取值 - <rpop：非阻塞式>
     * @param key 键
     * @return
     */
    public String rpop(String key) {
        try {
            return stringRedisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 取值 - <brpop：阻塞式> - 推荐使用
     * @param key 键
     * @param timeout 超时时间
     * @param timeUnit 给定单元粒度的时间段
     * TimeUnit.DAYS //天
     * TimeUnit.HOURS //小时
     * TimeUnit.MINUTES //分钟
     * TimeUnit.SECONDS //秒
     * TimeUnit.MILLISECONDS //毫秒
     * @return
     */
    public String brpop(String key, long timeout, TimeUnit timeUnit) {
        try {
            return stringRedisTemplate.opsForList().rightPop(key, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查看值
     * @param key 键
     * @param start 开始
     * @param end 结束 0 到 -1代表所有值
     * @return
     */
    public List<String> lrange(String key, long start, long end) {
        try {
            return stringRedisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public byte[] serialize(Object object) {

        ObjectOutputStream oos = null;


        ByteArrayOutputStream baos = null;

        try {
            //序列化  

            baos = new ByteArrayOutputStream();

            oos = new ObjectOutputStream(baos);

            oos.writeObject(object);

            byte[] bytes = baos.toByteArray();

            return bytes;

        } catch (Exception e) {

        }

        return null;

    }

    //反序列化

    public static Object unserialize(byte[] bytes) {

        ByteArrayInputStream bais = null;

        try {

            //反序列化  

            bais = new ByteArrayInputStream(bytes);

            ObjectInputStream ois = new ObjectInputStream(bais);

            return ois.readObject();

        } catch (Exception e) {

        }

        return null;

    }


}
