package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.NurseRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.NurseResponse;
import java.util.List;

public interface NurseService {

    NurseResponse saveNurseProfile(NurseRequest request);
    NurseResponse getNurseById(Long id);
    NurseResponse getNurseByEmail(String email);
    List<NurseResponse> getAllActiveNurses();
    List<NurseResponse> getNursesByWard(String wardName);
    List<NurseResponse> getOnDutyNurses();
    NurseResponse updateNurseDutyStatus(Long id, boolean onDuty);
    NurseResponse toggleNurseActiveStatus(Long id, boolean active);
}
