package com.example.demo.Token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration        //配置类注解
@EnableWebSecurity    //开启WebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  //开启方法级的注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //引入UserServiceDetail
    @Autowired
    UserServiceDetail userServiceDetail;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //  允许所有人访问 '/oauth' 以下的目录
        http.authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                .anyRequest().authenticated();

        http.csrf().disable()      //关闭  csrf
                //.and()
                .authorizeRequests()
                .antMatchers("/**").authenticated()    //其他目录需要认证
                .and()
                .httpBasic();                          //开启基本http验证
    }

    // 把 PasswordEncoder 放到  Spring 容器中
    // Springboot2 貌似必须把这个配置到 Spring 容器中，不然会报错
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceDetail).passwordEncoder(passwordEncoder());
    }


    //把 AuthenticationManager 配置到 Spring 容器中，配置Oauth2 的时候会用到
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}