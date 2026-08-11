package emranhss.com.Modern_Hospital_Management_System.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionItemResponse {


    private Long id;
    private String medicineType;
    
    private Long medicineId;

    private String medicineName;

    private String dosage;

    private String duration;

    private String instruction;
}
