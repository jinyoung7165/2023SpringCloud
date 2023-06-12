package cloudclud.msa.userservice.controller;

import cloudclud.msa.userservice.dto.UserDto;
import cloudclud.msa.userservice.jpa.UserEntity;
import cloudclud.msa.userservice.service.UserService;
import cloudclud.msa.userservice.utils.response.ApiResponse;
import cloudclud.msa.userservice.vo.Greeting;
import cloudclud.msa.userservice.vo.RequestUser;
import cloudclud.msa.userservice.vo.ResponseUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final Greeting greeting;
    private final UserService userService;
    private final Environment env;

    @GetMapping(value = "/health-check")
    public String status(HttpServletRequest request) {
        return String.format("It's working in User Service, on PORT %s",
                request.getServerPort()
                + ", token secret=" + env.getProperty("token.secret")
                + ", token expiration_time" + env.getProperty("token.expiration_time")
        ); // env-> local.server.port로 가져올 수도 있음
    }

    @GetMapping(value = "/welcome")
    public String welcome() {
        return greeting.getMessage();
    }

    @PostMapping(value = "/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        return ApiResponse.created(ResponseUser.of(userService.createUser(UserDto.of(user))));
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<ResponseUser>> getUsers() {
        Iterable<UserEntity> userList = userService.getAllUsers();
        List<ResponseUser> result = new ArrayList<>();
        userList.forEach(v -> {
            result.add(ResponseUser.of(v));
        });
        return ApiResponse.success(result);
    }

    @GetMapping(value = "/users/{userId}")
    public ResponseEntity<ResponseUser> getUserWithOrders(@PathVariable("userId") String userId) {
        return ApiResponse.success(ResponseUser.of(userService.getUserWithOrdersByUserId(userId)));
    }
}
