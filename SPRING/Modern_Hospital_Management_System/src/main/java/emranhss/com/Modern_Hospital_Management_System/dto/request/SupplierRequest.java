package emranhss.com.Modern_Hospital_Management_System.dto.request;
import lombok.Data;
@Data
public class SupplierRequest {
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
}
