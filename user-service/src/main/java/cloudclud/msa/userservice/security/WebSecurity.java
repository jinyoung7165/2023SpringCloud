package cloudclud.msa.userservice.security;

import cloudclud.msa.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter  {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Environment env;

    @Override
    protected void configure(HttpSecurity http) throws Exception { // 인가
        http    .headers() // 아래에 X-Frame-Option 헤더 설정을 위해 headers() 작성
                .frameOptions().sameOrigin() // 동일 도메인에서는 iframe 접근 가능하도록 X-Frame-Options을 smaeOrigin()으로 설정
                .and().cors().and()
                .httpBasic().disable()
                .csrf().disable() //session안 쓸 것
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 관리x
                .and()
                .authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/**").permitAll()
                //.hasIpAddress("127.0.0.1")
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(getAuthFilter());
//                .authorizeRequests().antMatchers("/users/**").permitAll();
    }

    private AuthFilter getAuthFilter() throws Exception {
        AuthFilter authFilter = new AuthFilter(userService, env);
        authFilter.setAuthenticationManager(authenticationManager());
        return authFilter;
    }

    // select pwd from users where email=?
    // db_pwd(encrypted) <-> input_pwd->encrypt해서 비교
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { // 인증
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }
}
