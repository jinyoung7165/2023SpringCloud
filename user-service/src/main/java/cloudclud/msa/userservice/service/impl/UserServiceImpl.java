package cloudclud.msa.userservice.service.impl;

import cloudclud.msa.userservice.dto.UserDto;
import cloudclud.msa.userservice.jpa.UserEntity;
import cloudclud.msa.userservice.jpa.UserRepository;
import cloudclud.msa.userservice.service.UserService;
import cloudclud.msa.userservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        List<ResponseOrder> orders = new ArrayList<>();
        userDto.setOrders(orders);
        return userDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity =  userRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("User Not Found"));
        return UserDto.of(userEntity);
    }

    @Override
    public Iterable<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }


}
