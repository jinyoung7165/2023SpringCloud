package cloudclud.msa.userservice.service;

import cloudclud.msa.userservice.dto.UserDto;
import cloudclud.msa.userservice.jpa.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);

    UserDto getUserWithOrdersByUserId(String userId);

    UserDto getUserByUserId(String userId);
    Iterable<UserEntity> getAllUsers();

    UserDto getUserDetailsByEmail(String email);
}
