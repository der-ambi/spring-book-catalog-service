package de.bashburg.springbook.catalogservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun filterChain(http: HttpSecurity) =
        http
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/actuator/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/", "/books/**").permitAll()
                    .anyRequest().hasRole("employee")
            }
            .oauth2ResourceServer { oAuth2ResourceServerConfigurer -> oAuth2ResourceServerConfigurer.jwt(Customizer.withDefaults()) }
            .sessionManagement { sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .csrf { csrf -> csrf.disable() }
            .build()

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val jwtAuthenticationConverter = JwtAuthenticationConverter()
        jwtAuthenticationConverter.run {
            val jwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()
            jwtGrantedAuthoritiesConverter.run {
                setAuthorityPrefix("ROLE_")
                setAuthoritiesClaimName("roles")
            }

            setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter)
        }
        return jwtAuthenticationConverter
    }
}