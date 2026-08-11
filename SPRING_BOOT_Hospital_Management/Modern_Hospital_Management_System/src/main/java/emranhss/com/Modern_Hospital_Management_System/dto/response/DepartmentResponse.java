package emranhss.com.Modern_Hospital_Management_System.dto.response;


import lombok.Data;

@Data
public class DepartmentResponse {


    private Long id;
    private String name;
    private String description;
    private String code;
    private Boolean active;
}
