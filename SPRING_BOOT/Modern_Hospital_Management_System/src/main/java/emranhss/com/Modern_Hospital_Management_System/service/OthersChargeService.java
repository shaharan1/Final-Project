package emranhss.com.Modern_Hospital_Management_System.service;


import emranhss.com.Modern_Hospital_Management_System.dto.request.OthersChargeRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.OthersChargeResponse;

import java.util.List;

public interface OthersChargeService {


    OthersChargeResponse addCharge(OthersChargeRequest request);
    List<OthersChargeResponse> getChargesByAdmission(Long admittedPatientId);
    List<OthersChargeResponse> getUnbilledChargesByAdmission(Long admittedPatientId);
}
