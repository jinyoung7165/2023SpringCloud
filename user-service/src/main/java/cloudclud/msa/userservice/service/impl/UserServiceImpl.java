package cloudclud.msa.userservice.service.impl;

import cloudclud.msa.userservice.client.OrderServiceClient;
import cloudclud.msa.userservice.dto.UserDto;
import cloudclud.msa.userservice.jpa.UserEntity;
import cloudclud.msa.userservice.jpa.UserRepository;
import cloudclud.msa.userservice.service.UserService;
import cloudclud.msa.userservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final RestTemplate restTemplate;

    private final OrderServiceClient orderServiceClient;

    private final Environment env;

    @Override // 구현이 돼 있는 거 다시 정의
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = getUserByEmail(email);
        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>());
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = getUserByEmail(email);
        return UserDto.of(userEntity);
    }

    private UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        log.info(userDto.getUserId());
        UserEntity userEntity = UserDto.toEntity(userDto);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));

        userRepository.save(userEntity);
        UserDto resUserDto = UserDto.of(userEntity);
        return resUserDto;
    }

    @Override
    public UserDto getUserWithOrdersByUserId(String userId) {
        UserDto userDto = getUserByUserId(userId);

        /* Using RestTemplate */
        // String orderUrl = "http://127.0.0.1:8000/order-service/%s/orders";
//        String orderUrl = String.format(env.getProperty("order_service.url"), userId);
//        ResponseEntity<List<ResponseOrder>> orderResponseList = restTemplate.exchange(orderUrl, HttpMethod.GET, null,
//            new ParameterizedTypeReference<List<ResponseOrder>>() {});
//
//        List<ResponseOrder> orderList = orderResponseList.getBody();

        /* Using FeignClient */
//        List<ResponseOrder> orderList = null;
//        try{
//            orderList = orderServiceClient.getOrders(userId);
//        } catch (FeignException ex) {
//            log.error(ex.getMessage());
//        }
        // ErrorDecoder가 FeignException 헨들링 해줄 것
        List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);
        userDto.setOrders(orderList);
        return userDto;
    }

    private UserDto getUserByUserId(String userId) {
        UserEntity userEntity =  userRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("User Not Found"));
        return UserDto.of(userEntity);
    }

    @Override
    public Iterable<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }


}
