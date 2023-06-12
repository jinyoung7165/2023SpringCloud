package cloudclud.msa.userservice.security;

import cloudclud.msa.userservice.dto.UserDto;
import cloudclud.msa.userservice.service.UserService;
import cloudclud.msa.userservice.utils.CustomObjectMapper;
import cloudclud.msa.userservice.vo.RequestLogin;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends UsernamePasswordAuthenticationFilter {
    private final UserService userService;
    private final Environment env;
    // userId, pwd, 권한 가진 Token 만들어 AuthenticationManager에게 인증 작업 요청
    // WebSecurity에 정의한 대로, userDetailService를 상속한 userService에 인증 요청 = db의 user정보와 일치 확인
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
       try {
           RequestLogin creds =CustomObjectMapper.objectMapper.readValue(request.getInputStream(), RequestLogin.class);
           return getAuthenticationManager().authenticate(
               new UsernamePasswordAuthenticationToken(
                       creds.getEmail(),
                       creds.getPassword(),
                       new ArrayList<>())
           );
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
    }

    @Override // 인증 성공 후 User, 반환해줄지(토큰)
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        String userName = ((User)authResult.getPrincipal()).getUsername(); // email
        UserDto userDetails = userService.getUserDetailsByEmail(userName);
        String token = Jwts.builder()
                .setSubject(userDetails.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() +
                        Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();
        response.addHeader("token", token);
        response.addHeader("userId", userDetails.getUserId());
    }
}
