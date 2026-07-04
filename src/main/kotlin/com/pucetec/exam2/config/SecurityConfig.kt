package com.pucetec.exam2.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .csrf { it.disable() }

            .authorizeHttpRequests {

                it
                    .requestMatchers(
                        "/",
                        "/error",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                    ).permitAll()

                    .requestMatchers("/api/spaces/available").permitAll()

                    .anyRequest().authenticated()

            }

            .formLogin { it.disable() }

            .httpBasic { it.disable() }

            .oauth2ResourceServer { it.jwt { } }

        return http.build()
    }
}
