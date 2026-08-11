package emranhss.com.Modern_Hospital_Management_System.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineResponse {


    private Long id;

    private String medicineName;

    private String genericName;

    private String dosage;

    private Long genericId;

    private Long prescriptionId;
}
