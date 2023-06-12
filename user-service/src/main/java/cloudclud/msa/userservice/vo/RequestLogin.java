package cloudclud.msa.userservice.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestLogin {
    @Email
    @NotNull(message = "Email cannot be null")
    @Size(min=2, message = "Email not be less then 2 chars")
    private String email;
    @NotNull(message = "PW cannot be null")
    @Size(min=8, message = "PW not be less then 8 chars")
    private String password;
}
