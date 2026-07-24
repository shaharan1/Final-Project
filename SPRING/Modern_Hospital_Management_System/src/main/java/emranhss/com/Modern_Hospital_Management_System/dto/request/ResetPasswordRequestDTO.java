package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.Data;

@Data
public class ResetPasswordRequestDTO {

    private String token;        // from email link
    private String newPassword;

}

