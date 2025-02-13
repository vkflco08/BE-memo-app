// package com.memo.memo.redis
//
// import org.springframework.cache.CacheManager
// import org.springframework.cache.annotation.EnableCaching
// import org.springframework.context.annotation.Bean
// import org.springframework.context.annotation.Configuration
// import org.springframework.data.redis.cache.RedisCacheConfiguration
// import org.springframework.data.redis.cache.RedisCacheManager
// import org.springframework.data.redis.connection.RedisConnectionFactory
// import org.springframework.data.redis.core.RedisTemplate
// import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
// import org.springframework.data.redis.serializer.StringRedisSerializer
// import java.time.Duration
//
// @Configuration
// @EnableCaching
// class RedisConfig {
//    @Bean
//    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory?): RedisTemplate<String, Any> {
//        val template = RedisTemplate<String, Any>()
//        template.connectionFactory = redisConnectionFactory
//        template.keySerializer = StringRedisSerializer()
//        template.valueSerializer = GenericJackson2JsonRedisSerializer()
//        return template
//    }
//
//    @Bean
//    fun cacheManager(connectionFactory: RedisConnectionFactory?): CacheManager {
//        val redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//            .entryTtl(Duration.ofMinutes(30))  // 캐시 항목 만료 시간 설정 (예: 30분)
//
//        return RedisCacheManager.builder(connectionFactory!!)
//            .cacheDefaults(redisCacheConfiguration)
//            .build()
//    }
// }
