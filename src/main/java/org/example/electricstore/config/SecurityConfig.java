package org.example.electricstore.config;

import org.example.electricstore.service.impl.UserInforDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserInforDetailService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/login", "/error", "/register", "/", "/logoutSuccessful","/images/**").permitAll()
                                .requestMatchers("/static/**", "/css/**", "/js/**", "/img/**", "/api/**").permitAll()
                                .requestMatchers("/ShopPhone/css/**", "/ShopPhone/js/**", "/ShopPhone/img/**", "/ShopPhone/static/**").permitAll()
                                .requestMatchers("/Admin", "/Admin/transactions/**", "/Admin/payment/**").hasAnyRole("ADMIN", "WAREHOUSE", "BUSINESS", "SALES")
                                .requestMatchers("/Admin/report/**").hasAnyRole("ADMIN", "BUSINESS")
                                .requestMatchers("/Admin/order/**", "/sales/report/**", "/Admin/statistical/**").hasAnyRole("ADMIN", "SALES", "BUSINESS")
                                .requestMatchers("/Admin/customers/**").hasAnyRole("ADMIN", "SALES", "BUSINESS")
                                .requestMatchers("/Admin/suppliers-manager/**").hasAnyRole("ADMIN", "BUSINESS", "WAREHOUSE")
                                .requestMatchers("/Admin/ware-houses/**").hasAnyRole("ADMIN", "WAREHOUSE")
                                .requestMatchers("/Admin/employee-manager/**").hasRole("ADMIN")
                                .requestMatchers("/Admin/category-manager/**", "/Admin/brand-manager/**", "/Admin/product-manager/**").hasAnyRole("ADMIN", "SALES", "BUSINESS")
                                .requestMatchers("/Admin/brand-manager/add", "/Admin/brand-manager/edit", "/Admin/brand-manager/delete")
                                .hasRole("ADMIN")
                                .requestMatchers("/Admin/category-manager/add", "/Admin/category-manager/edit", "/Admin/category-manager/delete")
                                .hasRole("ADMIN")
                                .requestMatchers("/Admin/product-manager/add", "/Admin/product-manager/edit/**", "/Admin/product-manager/delete")
                                .hasRole("ADMIN")
                                .requestMatchers("/account", "/account/update", "/account/change-password").authenticated()
                                .requestMatchers("/Admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin((formLogin) ->
                        formLogin
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .loginPage("/login")
                                .failureUrl("/login?error=true")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/home", true)) // Giữ nguyên default success URL
                .logout((logout) ->
                        logout
                                .deleteCookies("remove")
                                .invalidateHttpSession(false)
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login"))
                .rememberMe((remember) ->
                        remember
                                .rememberMeParameter("remember-me")
                                .tokenValiditySeconds(60 * 60 * 24 * 365)) // Token hợp lệ 1 năm
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling
                                .accessDeniedPage("/403"));

        return http.build();
    }
}