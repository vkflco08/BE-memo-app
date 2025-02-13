package com.memo.memo

import com.memo.memo.common.auditing.SpringSecurityAuditorAware
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableCaching
class MemoApplication {
    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return SpringSecurityAuditorAware()
    }
}

fun main(args: Array<String>) {
    runApplication<MemoApplication>(*args)
}
