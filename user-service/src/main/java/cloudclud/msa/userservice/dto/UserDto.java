package cloudclud.msa.userservice.dto;

import cloudclud.msa.userservice.jpa.UserEntity;
import cloudclud.msa.userservice.utils.CustomObjectMapper;
import cloudclud.msa.userservice.vo.RequestUser;
import cloudclud.msa.userservice.vo.ResponseOrder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private Date createdAt;

    private String encryptedPwd;

    private List<ResponseOrder> orders;

    public static UserDto of(RequestUser user) { return CustomObjectMapper.to(user, UserDto.class); }
    public static UserDto of(UserEntity userEntity) { return CustomObjectMapper.to(userEntity, UserDto.class); }
    public static UserEntity toEntity(UserDto userDto) {
        return CustomObjectMapper.to(userDto, UserEntity.class);
    }

}
