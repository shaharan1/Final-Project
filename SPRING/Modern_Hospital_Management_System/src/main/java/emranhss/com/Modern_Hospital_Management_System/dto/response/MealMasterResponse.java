package emranhss.com.Modern_Hospital_Management_System.dto.response;

import lombok.Data;

@Data
public class MealMasterResponse {
    private Long id;
    private String category;
    private String type;
    private String name;
    private String details;
    private Double price;
    private Boolean active;
}
