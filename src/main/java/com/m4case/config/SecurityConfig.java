package com.m4case.config;

import com.m4case.service.IMyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private IMyUserService myUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/").authenticated()
                .and().authorizeRequests().antMatchers("/u/**").hasAnyAuthority("ROLE_COACH", "ROLE_ADMIN","ROLE_PLAYER")
                .and().authorizeRequests().antMatchers("/c/**").hasAnyAuthority("ROLE_COACH","ROLE_ADMIN","ROLE_PLAYER")
                .and().authorizeRequests().antMatchers("/p/**").hasAnyAuthority("ROLE_COACH", "ROLE_ADMIN","ROLE_PLAYER")
                .and().authorizeRequests().antMatchers("/a/**").hasAuthority("ROLE_ADMIN")
                .and()
                .formLogin().loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(new SuccessHandler())
                .and().logout().logoutSuccessUrl("/login");
        http.csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}
