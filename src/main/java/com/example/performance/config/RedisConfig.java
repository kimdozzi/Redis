package com.example.performance.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
// 어노테이션을 통해 캐싱 기능 사용 등록
@EnableCaching
public class RedisConfig {
    CacheManager cacheManager;
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    /*
     * redisRepository()
     * 더 추상화된 레벨의 컴포넌트로, Redis 데이터베이스에 대한 공통 작업을 더 편리하게 처리할 수 있는
     * 고수준의 메서드를 제공한다. Spring Data JPA의 JpaRepository와 유사한 형태로 작성되며,
     * 엔티티 CRUD와 같은 고수준의 작업을 지원한다. 개발자가 인터페이스를 정의하고 해당 인터페이스를 구현하는
     * Redis 저장소 클래스를 생성하여, 직접 Redis 작업을 구현할 필요 없이 Spring Data Redis의 기능을
     * 활용할 수 있다.
     * */

    /*
     * redisTemplate()
     * Spring data redis는 RedisTemplate, RedisRepository 두 가지 방식으로 접근할 수 있다.
     * 여기서는 RedisTemplate를 이용해 Redis 서버에 접근한다. Redis 데이터베이스와 상호작용하는 데 필요한
     * 설정을 구성한다. Redis에 문자열 키와 json 형식의 값으로 데이터를 저장하고 검색할 수 있다.
     *
     * 객체들을 자동으로 직렬화/역직렬화하며 binary data를 redis에 저장한다. 각 데이터 유형에 대해
     * CRUD가 가능하며, 저수준의 api로 직접적인 redis db 작업을 수행하는 데 사용된다.
     * */
//    @Bean
//    public RedisTemplate<?, ?> redisTemplate() {
//        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory());
//        return redisTemplate;
//    }

    /*
     * redisCacheManager : Redis Cache 적용을 위해 설정한다.
     * */
    @Bean
    public CacheManager redisCacheManager() {
        RedisCacheManager.RedisCacheManagerBuilder builder =
                RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory());

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig() // 기본 RedisCacheConfiguration을 가져온 후, key와 value의 직렬화 방식을 설정한다.
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new StringRedisSerializer())) // StringRedisSerializer로 문자열로 된 키를 직렬화하고,
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer())) // GenericJackson2JsonRedisSerializer를 사용하여 일반적인 객체를 JSON형식으로 직렬화한다.
                .disableCachingNullValues() // 데이터가 NULL일 경우 캐싱하지 않음
                .entryTtl(Duration.ofMinutes(30L)); // 유효기간 설정 (type : Long)

        builder.cacheDefaults(redisCacheConfiguration);

        return builder.build();
    }

}
