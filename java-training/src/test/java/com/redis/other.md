#### StringRedisTemplate  RedisTemplate 区别
>
> 两者的关系是StringRedisTemplate继承RedisTemplate。
> 
> 两者的数据是不共通的；也就是说StringRedisTemplate只能管理StringRedisTemplate里面的数据，RedisTemplate只能管理RedisTemplate中的数据。
> 
> 其实他们两者之间的区别主要在于他们使用的序列化类:
> 　　　　RedisTemplate使用的是JdkSerializationRedisSerializer    存入数据会将数据先序列化成字节数组然后在存入Redis数据库。 
> 
> 　　 　  StringRedisTemplate使用的是StringRedisSerializer
> 
> 使用时注意事项：
> 　　　当你的redis数据库里面本来存的是字符串数据或者你要存取的数据就是字符串类型数据的时候，那么你就使用StringRedisTemplate即可。
> 　　　但是如果你的数据是复杂的对象类型，而取出的时候又不想做任何的数据转换，直接从Redis里面取出一个对象，那么使用RedisTemplate是更好的选择。
> RedisTemplate使用时常见问题：
> 　　　　redisTemplate 中存取数据都是字节数组。当redis中存入的数据是可读形式而非字节数组时，使用redisTemplate取值的时候会无法获取导出数据，获得的值为null
。可以使用 StringRedisTemplate 试试。




##### Spring boot多数据源配置,配置一个1号库，一个4号库
- 添加依赖
```$xslt
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```
- 修改application.properties配置文件
```$xslt
#1号库
spring.redis.redis-onedb.database=0
spring.redis.redis-onedb.hostName=192.168.90.42
spring.redis.redis-onedb.port=9110
spring.redis.redis-onedb.timeout=5000
#4号库
spring.redis.redis-fourdb.database=4
spring.redis.redis-fourdb.hostName=192.168.90.42
spring.redis.redis-fourdb.port=9110
spring.redis.redis-fourdb.timeout=5000
```
- 创建RedisConfig.java文件
```$xslt
@Configuration
public class RedisConfig {
@Bean
@ConfigurationProperties(prefix = "spring.redis.lettuce.pool")
@Scope(value = "prototype")
public GenericObjectPoolConfig redisPool(){
    return new GenericObjectPoolConfig();
}

@Bean
@ConfigurationProperties(prefix = "spring.redis.redis-fourdb")
public RedisStandaloneConfiguration redisConfigA(){
    return new RedisStandaloneConfiguration();
}

@Bean
@ConfigurationProperties(prefix = "spring.redis.redis-onedb")
public RedisStandaloneConfiguration redisConfigB(){
    return new RedisStandaloneConfiguration();
}

@Primary
@Bean
public LettuceConnectionFactory factoryA(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisConfigA){
    LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
            .poolConfig(config).commandTimeout(Duration.ofMillis(config.getMaxWaitMillis())).build();
    return new LettuceConnectionFactory(redisConfigA, clientConfiguration);
}

@Bean
public LettuceConnectionFactory factoryB(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisConfigB){
    LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
            .poolConfig(config).commandTimeout(Duration.ofMillis(config.getMaxWaitMillis())).build();
    return new LettuceConnectionFactory(redisConfigB, clientConfiguration);
}


@Bean(name = "fourRedis")
public StringRedisTemplate redisBusTemplate(@Qualifier("factoryA") LettuceConnectionFactory factoryA){
    StringRedisTemplate template = getRedisTemplate();
    template.setConnectionFactory(factoryA);
    return template;
}

@Bean(name = "oneRedis")
public StringRedisTemplate redisLoginTemplate(@Qualifier("factoryB")LettuceConnectionFactory factoryB){
    StringRedisTemplate template = getRedisTemplate();
    template.setConnectionFactory(factoryB);
    return template;
}

private StringRedisTemplate getRedisTemplate(){
    StringRedisTemplate template = new StringRedisTemplate();
    template.setValueSerializer(new GenericFastJsonRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());
    return template;
}
}
```
- 使用
```$xslt
@Resource(name = "oneRedis")
private StringRedisTemplate oneRedis;

@Resource(name = "fourRedis")
private StringRedisTemplate fourRedis;
```