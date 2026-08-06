package emranhss.com.Modern_Hospital_Management_System.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurgeryCategoryRequest {
    private String code;
    private String name;
    private String description;
    private Boolean active;
    private Integer sortOrder;
}
