package antifraud.secutiry;


import antifraud.model.user.UserRole;
import antifraud.model.user.UserRoleType;
import antifraud.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@EnableWebSecurity
public class WebSecurityConfigurerImpl extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(getEncoder());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(getEntryPoint()) // Handles auth error
                .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and()
                .authorizeRequests() // manage access
                .antMatchers(HttpMethod.POST, "/api/auth/user").permitAll()
                .mvcMatchers(HttpMethod.DELETE, "/api/auth/user/**").hasRole(UserRoleType.ADMINISTRATOR.name())
                .mvcMatchers("/api/auth/list").hasAnyRole(UserRoleType.ADMINISTRATOR.name(), UserRoleType.SUPPORT.name())
                .mvcMatchers(HttpMethod.POST, "/api/antifraud/transaction").hasAnyRole(UserRoleType.MERCHANT.name())
                .mvcMatchers("/api/antifraud/suspicious-ip/**").hasRole(UserRoleType.SUPPORT.name())
                .mvcMatchers("/api/antifraud/stolencard/**").hasRole(UserRoleType.SUPPORT.name())
                .mvcMatchers("/api/antifraud/history/**").hasRole(UserRoleType.SUPPORT.name())
                .mvcMatchers(HttpMethod.PUT, "/api/antifraud/transaction").hasRole(UserRoleType.SUPPORT.name())
                .mvcMatchers("/api/auth/access").hasRole(UserRoleType.ADMINISTRATOR.name())
                .mvcMatchers("/api/auth/role").hasRole(UserRoleType.ADMINISTRATOR.name())
                .antMatchers("/actuator/shutdown").permitAll() // needs to run test
                // other matchers
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder(13);
    }

    @Bean
    public AuthenticationEntryPoint getEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

}
