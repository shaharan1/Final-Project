package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.dto.request.AmbulanceTripRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.AmbulanceTripResponse;

import java.util.List;

public interface AmbulanceTripService {

    AmbulanceTripResponse create(AmbulanceTripRequest request);

    AmbulanceTripResponse getById(Long id);

    List<AmbulanceTripResponse> getAll();

    AmbulanceTripResponse dispatchTrip(Long tripId);

    AmbulanceTripResponse completeTrip(Long tripId);

    AmbulanceTripResponse cancelTrip(Long tripId);

    List<AmbulanceTripResponse> getActiveTrips();
}
