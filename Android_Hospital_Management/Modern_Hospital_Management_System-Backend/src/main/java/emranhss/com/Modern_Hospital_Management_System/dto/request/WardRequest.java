package emranhss.com.Modern_Hospital_Management_System.dto.request;


import emranhss.com.Modern_Hospital_Management_System.enums.RoomType;
import lombok.Data;

@Data
public class WardRequest {



    private String name;
    private Long departmentId;
    private RoomType roomType;
    private Integer totalBeds;
    private Double basePricePerDay;
}
