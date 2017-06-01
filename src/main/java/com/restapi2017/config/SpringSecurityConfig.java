package com.restapi2017.config;

import com.restapi2017.exceptions.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/swagger.json")
                .permitAll().and()

                .authorizeRequests()
                .antMatchers(HttpMethod.DELETE)
                .hasRole("ADMIN").and()

                .authorizeRequests()
                .antMatchers(HttpMethod.POST)
                .hasRole("ADMIN").and()

                .authorizeRequests()
                .antMatchers(HttpMethod.PUT)
                .hasRole("ADMIN");

        http.httpBasic();

        http.csrf().disable();

    }

    @Bean
    public CustomAuthenticationEntryPoint getBasicAuthEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Autowired(required = false)
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER").and()
                .withUser("admin").password("password").roles("ADMIN");

    }

}
