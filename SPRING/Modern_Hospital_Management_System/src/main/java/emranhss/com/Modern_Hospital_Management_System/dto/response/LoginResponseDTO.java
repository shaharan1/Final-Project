package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.Data;

@Data
public class LoginResponseDTO {

    private String  token;
    private String  tokenType = "Bearer";

    private Long    userId;
    private String  name;
    private String  email;
    private String  phone;
    private String  role;


}
