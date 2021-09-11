package com.petfabula.presentation.web.security;

import com.petfabula.domain.aggregate.identity.service.PasswordEncoderService;
import com.petfabula.presentation.web.security.filter.UnauthenticatedRequestHandler;
import com.petfabula.presentation.web.security.filter.UnauthorizedRequestHandler;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder authManager) {
        // empty body for preventing default password generation
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/api/identity/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/identity/oauth-redirect").permitAll()
                .antMatchers(HttpMethod.POST,"/api/identity/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/user/check-confirm-email",
                        "/api/user/confirm-email", "/api/user")
                .permitAll()

                .antMatchers(HttpMethod.GET,"/api/user/info")
                .authenticated()
                .antMatchers(HttpMethod.GET,"/api/post/posts")
                .authenticated()
                .antMatchers(HttpMethod.GET,"/api/question/questions", "/api/question/answers")
                .authenticated()
                .antMatchers(HttpMethod.GET,"/api/participator/profile", "/api/participator/my*")
                .authenticated()
                .antMatchers(HttpMethod.GET,"/api/notification/**")
                .authenticated()
                .antMatchers(HttpMethod.GET,"/api/account/**")
                .authenticated()

                .antMatchers(HttpMethod.GET,"/api/**")
                .permitAll()
                .antMatchers("/api/**")
                .authenticated()

                .and()
                .formLogin()
                .disable()
                .logout()
                .logoutUrl("/api/identity/logout")
                .addLogoutHandler(new SecurityContextLogoutHandler())
                .logoutSuccessHandler(logoutSuccessHandler())
                .and()
//                .addFilterBefore(jwtAuthorizationFilter(), BasicAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(unauthenticatedRequestHandler())
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .csrf()
                .disable();

        httpSecurity.sessionManagement().maximumSessions(1);

    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher()
    {
        return new HttpSessionEventPublisher();
    }

//    @Bean
//    LogoutHandler logoutHandler() {
//        return new LogoutProcessHandler();
//    }

    @Bean
    LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutProcessSuccessHandler();
    }

    @Bean
    UnauthenticatedRequestHandler unauthenticatedRequestHandler() {
        return new UnauthenticatedRequestHandler();
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return new UnauthorizedRequestHandler();
    }

//    @Bean
//    JwtAuthorizationFilter jwtAuthorizationFilter() {
//        return new JwtAuthorizationFilter();
//    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://127.0.0.1:3000"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList("HEAD","GET", "POST", "PUT", "DELETE", "PATCH"));
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderService.newInstance().getInternalEncoder();
    }

}
