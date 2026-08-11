package emranhss.com.Modern_Hospital_Management_System.dto.response;


import emranhss.com.Modern_Hospital_Management_System.enums.RoomType;
import lombok.Data;

@Data
public class WardResponse {


    private Long id;
    private String name;
    private Long departmentId;
    private String departmentName;
    private RoomType roomType;
    private Integer totalBeds;
    private Double basePricePerDay;
}
