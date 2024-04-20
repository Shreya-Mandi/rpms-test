package rpms.config;

import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import rpms.services.AccountService;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AccountService userDetailsService;

    @Autowired
    public SecurityConfig(AccountService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers("/").permitAll() // TODO add css, js?
                        .requestMatchers("/login", "/registerStudent", "/registerFaculty").hasAuthority("ROLE_ANONYMOUS")
                        .requestMatchers("/projects", "/project/{projectId:[0-9]+}").hasAnyAuthority("STUDENT", "FACULTY", "ROLE_ANONYMOUS")
                        .requestMatchers("/adminDashboard/**", "/project/{projectId:[0-9]+}/delete").hasAuthority("ADMIN")
                        .requestMatchers("/project/**").hasAnyAuthority("STUDENT", "FACULTY")
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
                .formLogin((form) -> form
                    .loginPage("/login")
                );

        return http.build();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }
}
