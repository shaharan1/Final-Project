package emranhss.com.Modern_Hospital_Management_System.dto.response;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class SupplierResponse {
    private Long id;
    private String name;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private String companyName;
    private String tradeLicense;
    private String drugLicense;
    private String website;
    private String notes;
    private Double totalDue;
    private Boolean active;
    private LocalDateTime createdDate;
}
