package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.Data;

@Data
public class MealMasterRequest {
    private String category;
    private String type;
    private String name;
    private String details;
    private Double price;
    private Boolean active;
}
