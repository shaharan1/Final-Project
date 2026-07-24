package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDepartmentResponse {
    private long id;
    private String departmentName;
    private String description;
}
