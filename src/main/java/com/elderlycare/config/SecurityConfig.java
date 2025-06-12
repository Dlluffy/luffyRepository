package com.elderlycare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

//  @Bean
//  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    http
//      .authorizeHttpRequests(auth -> auth
//        // 放行 Elders 模块的测试页
//        .requestMatchers("/elders/test", "/elders/test/**").permitAll()
//        // 你其他静态资源也可以放行
//        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
//        // 剩下的都要登录
//        .anyRequest().authenticated()
//      )
//      // 如果有人未登录去其他页面，还是走默认登录表单
//      .formLogin(form -> form
//        .loginPage("/").permitAll()
//      )
//      .logout(logout -> logout.permitAll());
//
//    return http.build();
//  }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//                .csrf(csrf -> csrf.disable()) // 关闭 CSRF（开发时可用）
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // ✅ 放行所有请求
                )
                .formLogin(form -> form
                        .disable() // ✅ 禁用默认登录页（可选）
                )
                .logout(logout -> logout.disable()); // ✅ 禁用登出功能（可选）

        return http.build();
    }
}
