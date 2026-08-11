package emranhss.com.Modern_Hospital_Management_System.serviceimp;

import emranhss.com.Modern_Hospital_Management_System.dto.mapper.AmbulanceTripMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.AmbulanceTripRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.AmbulanceTripResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Ambulance;
import emranhss.com.Modern_Hospital_Management_System.entity.AmbulanceTrip;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.AmbulanceRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.AmbulanceTripRepository;
import emranhss.com.Modern_Hospital_Management_System.service.AmbulanceTripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AmbulanceTripServiceImp implements AmbulanceTripService {

    private final AmbulanceTripRepository ambulanceTripRepository;
    private final AmbulanceTripMapper ambulanceTripMapper;
    private final AmbulanceRepository ambulanceRepository;

    @Override
    @Transactional
    public AmbulanceTripResponse create(AmbulanceTripRequest request) {
        AmbulanceTrip trip = ambulanceTripMapper.toEntity(request);
        trip.setStatus("CREATED");
        AmbulanceTrip saved = ambulanceTripRepository.save(trip);
        return ambulanceTripMapper.toResponse(saved);
    }

    @Override
    public AmbulanceTripResponse getById(Long id) {
        AmbulanceTrip trip = ambulanceTripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ambulance trip not found with id: " + id));
        return ambulanceTripMapper.toResponse(trip);
    }

    @Override
    public List<AmbulanceTripResponse> getAll() {
        return ambulanceTripRepository.findAll().stream()
                .map(ambulanceTripMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AmbulanceTripResponse dispatchTrip(Long tripId) {
        AmbulanceTrip trip = ambulanceTripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Ambulance trip not found with id: " + tripId));
        trip.setStatus("DISPATCHED");
        trip.setDispatchTime(LocalDateTime.now());

        if (trip.getAmbulance() != null) {
            Ambulance ambulance = trip.getAmbulance();
            ambulance.setStatus("ON_DUTY");
            ambulanceRepository.save(ambulance);
        }

        return ambulanceTripMapper.toResponse(ambulanceTripRepository.save(trip));
    }

    @Override
    @Transactional
    public AmbulanceTripResponse completeTrip(Long tripId) {
        AmbulanceTrip trip = ambulanceTripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Ambulance trip not found with id: " + tripId));
        trip.setStatus("COMPLETED");
        trip.setCompletionTime(LocalDateTime.now());

        if (trip.getDispatchTime() != null) {
            long minutes = ChronoUnit.MINUTES.between(trip.getDispatchTime(), LocalDateTime.now());
            trip.setResponseTimeMinutes((double) minutes);
        }

        if (trip.getAmbulance() != null) {
            Ambulance ambulance = trip.getAmbulance();
            ambulance.setStatus("AVAILABLE");
            ambulance.setCurrentLocation("Hospital");
            ambulanceRepository.save(ambulance);
        }

        return ambulanceTripMapper.toResponse(ambulanceTripRepository.save(trip));
    }

    @Override
    @Transactional
    public AmbulanceTripResponse cancelTrip(Long tripId) {
        AmbulanceTrip trip = ambulanceTripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Ambulance trip not found with id: " + tripId));
        trip.setStatus("CANCELLED");

        if (trip.getAmbulance() != null) {
            Ambulance ambulance = trip.getAmbulance();
            ambulance.setStatus("AVAILABLE");
            ambulanceRepository.save(ambulance);
        }

        return ambulanceTripMapper.toResponse(ambulanceTripRepository.save(trip));
    }

    @Override
    public List<AmbulanceTripResponse> getActiveTrips() {
        return ambulanceTripRepository.findByStatus("DISPATCHED").stream()
                .map(ambulanceTripMapper::toResponse)
                .collect(Collectors.toList());
    }
}
