package cloudclud.msa.userservice.vo;

import cloudclud.msa.userservice.dto.UserDto;
import cloudclud.msa.userservice.jpa.UserEntity;
import cloudclud.msa.userservice.utils.CustomObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // null값은 결과에 포함하지 않음
public class ResponseUser {
    private String email;
    private String name;
    private String userId;
    private List<ResponseOrder> orders;

    public static ResponseUser of(UserDto user) { return CustomObjectMapper.to(user, ResponseUser.class); }
    public static ResponseUser of(UserEntity user) { return CustomObjectMapper.to(user, ResponseUser.class); }
}
