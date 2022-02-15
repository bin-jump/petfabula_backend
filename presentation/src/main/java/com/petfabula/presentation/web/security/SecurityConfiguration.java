package com.petfabula.presentation.web.security;

import com.petfabula.domain.aggregate.identity.service.PasswordEncoderService;
import com.petfabula.presentation.web.config.CorsProps;
import com.petfabula.presentation.web.config.RegexCorsConfiguration;
import com.petfabula.presentation.web.security.authencate.*;
import com.petfabula.presentation.web.security.authencate.apple.AppleTokenAuthenticationProvider;
import com.petfabula.presentation.web.security.authencate.apple.AppleTokenRegisterProvider;
import com.petfabula.presentation.web.security.authencate.emailcode.EmailCodeAuthenticationProvider;
import com.petfabula.presentation.web.security.authencate.emailcode.EmailCodeRegisterAuthenticationProvider;
import com.petfabula.presentation.web.security.authencate.emailpass.EmailPasswordAuthenticationProvider;
import com.petfabula.presentation.web.security.authencate.oauth.OauthLoginProvider;
import com.petfabula.presentation.web.security.authencate.oauth.OauthRegisterProvider;
import com.petfabula.presentation.web.security.filter.*;
import com.petfabula.presentation.web.security.handler.LogoutProcessSuccessHandler;
import com.petfabula.presentation.web.security.handler.UnauthenticatedRequestHandler;
import com.petfabula.presentation.web.security.handler.UnauthorizedRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

//    @Override
//    protected void configure(AuthenticationManagerBuilder authManager) {
//        // empty body for preventing default password generation
//    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors()
                .and()
                .addFilter(concurrentSessionFilter())
                .addFilterBefore(oauthRegisterFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(oauthLoginFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(emailCodeRegisterAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(emailCodeAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(emailPasswordAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(appleAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(appleRegisterFilter(),
                        UsernamePasswordAuthenticationFilter.class)

                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/api/identity/**").permitAll()
                .antMatchers("/api/auth/**").permitAll()
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
                .antMatchers(HttpMethod.GET,"/api/participator/blockeds")
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
//
//        httpSecurity.sessionManagement()
//                .maximumSessions(1)
//                .maxSessionsPreventsLogin(false)
//        .sessionRegistry(sessionRegistry());

    }

    @Autowired
    private FindByIndexNameSessionRepository sessionRepository;

    @Autowired
    private EmailCodeAuthenticationProvider emailCodeAuthenticationProvider;

    @Autowired
    private OauthRegisterProvider oauthRegisterProvider;

    @Autowired
    private OauthLoginProvider oauthLoginProvider;

    @Autowired
    private EmailCodeRegisterAuthenticationProvider emailCodeRegisterAuthenticationProvider;

    @Autowired
    private EmailPasswordAuthenticationProvider emailPasswordAuthenticationProvider;

    @Autowired
    private AppleTokenAuthenticationProvider appleTokenAuthenticationProvider;

    @Autowired
    private AppleTokenRegisterProvider appleTokenRegisterProvider;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private CorsProps corsProps;

    public EmailCodeAuthenticationFilter emailCodeAuthenticationFilter() throws Exception {
        EmailCodeAuthenticationFilter filter = new EmailCodeAuthenticationFilter();
        configAuthFilter(filter);
        return filter;
    }

    public EmailCodeRegisterAndAuthenticationFilter emailCodeRegisterAuthenticationFilter() throws Exception {
        EmailCodeRegisterAndAuthenticationFilter filter = new EmailCodeRegisterAndAuthenticationFilter();
        configAuthFilter(filter);
        return filter;
    }

    public EmailPasswordAuthenticationFilter emailPasswordAuthenticationFilter() throws Exception {
        EmailPasswordAuthenticationFilter filter = new EmailPasswordAuthenticationFilter();
        configAuthFilter(filter);
        return filter;
    }

    public OauthRegisterFilter oauthRegisterFilter() throws Exception {
        OauthRegisterFilter filter = new OauthRegisterFilter();
        configAuthFilter(filter);
        return filter;
    }

    public OauthLoginFilter oauthLoginFilter() throws Exception {
        OauthLoginFilter filter = new OauthLoginFilter();
        configAuthFilter(filter);
        return filter;
    }

    public AppleLoginFilter appleAuthenticationFilter() throws Exception {
        AppleLoginFilter filter = new AppleLoginFilter();
        configAuthFilter(filter);
        return filter;
    }

    public AppleRegisterFilter appleRegisterFilter() throws Exception {
        AppleRegisterFilter filter = new AppleRegisterFilter();
        configAuthFilter(filter);
        return filter;
    }

    private void configAuthFilter(AbstractAuthenticationProcessingFilter filter) throws Exception {
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationFailureHandler(loginFailureHandler);
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        filter.setSessionAuthenticationStrategy(authStrategy());
    }

    private ConcurrentSessionControlAuthenticationStrategy authStrategy() {
        ConcurrentSessionControlAuthenticationStrategy result = new ConcurrentSessionControlAuthenticationStrategy(
                this.sessionRegistry());
        result.setMaximumSessions(2);
        result.setExceptionIfMaximumExceeded(false);
        return result;
    }

    @Bean
    protected ConcurrentSessionFilter concurrentSessionFilter(){
        return new ConcurrentSessionFilter(sessionRegistry(), new CustomSessionInformationExpiredStrategy());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
       authenticationManagerBuilder
                .authenticationProvider(emailCodeAuthenticationProvider)
                .authenticationProvider(oauthRegisterProvider)
                .authenticationProvider(oauthLoginProvider)
                .authenticationProvider(emailCodeRegisterAuthenticationProvider)
                .authenticationProvider(emailPasswordAuthenticationProvider)
                .authenticationProvider(appleTokenAuthenticationProvider)
                .authenticationProvider(appleTokenRegisterProvider);
    }

    @Bean
    public SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry(this.sessionRepository);
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

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        final CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://127.0.0.1:3000"));
//        configuration.setAllowCredentials(true);
//        configuration.setAllowedMethods(Arrays.asList("HEAD","GET", "POST", "PUT", "DELETE", "PATCH"));
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        RegexCorsConfiguration configuration = new RegexCorsConfiguration();
        configuration.setAllowCredentials(true);

//        List<String> allowed = new ArrayList<>();
//        if (corsProps.getAllowedOrigin() != null) {
//            allowed.add(corsProps.getAllowedOrigin());
//        }
//        configuration.setAllowedOriginRegex(allowed);
        configuration.setAllowedOriginRegex(corsProps.getAllowedOrigins());
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList("HEAD","GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderService.newInstance().getInternalEncoder();
    }

}
