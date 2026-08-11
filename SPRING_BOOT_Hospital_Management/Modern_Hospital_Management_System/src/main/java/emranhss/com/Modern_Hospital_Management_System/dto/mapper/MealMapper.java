package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.MealRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.MealResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.AdmittedPatient;
import emranhss.com.Modern_Hospital_Management_System.entity.BedBooking;
import emranhss.com.Modern_Hospital_Management_System.entity.Meal;
import emranhss.com.Modern_Hospital_Management_System.entity.MealMaster;
import org.springframework.stereotype.Component;

@Component
public class MealMapper {

    public MealResponse toResponse(Meal entity) {
        if (entity == null) return null;

        MealResponse response = new MealResponse();
        response.setId(entity.getId());
        response.setMealCost(entity.getMealCost());
        response.setServedAt(entity.getServedAt());
        response.setBillingStatus(entity.getBillingStatus());

        if (entity.getMealMaster() != null) {
            response.setMealMasterId(entity.getMealMaster().getId());
            response.setMealMasterName(entity.getMealMaster().getName());
        }

        if (entity.getBedBooking() != null) {
            response.setBedBookingId(entity.getBedBooking().getId());
        }

        if (entity.getAdmittedPatient() != null) {
            response.setAdmittedPatientId(entity.getAdmittedPatient().getId());
        }

        return response;
    }

    public Meal toEntity(MealRequest request) {
        if (request == null) return null;

        Meal entity = new Meal();
        entity.setMealCost(request.getMealCost());
        entity.setServedAt(request.getServedAt());
        entity.setBillingStatus(request.getBillingStatus() != null ? request.getBillingStatus() : "PENDING");

        if (request.getMealMasterId() != null) {
            MealMaster master = new MealMaster();
            master.setId(request.getMealMasterId());
            entity.setMealMaster(master);
        }

        if (request.getBedBookingId() != null) {
            BedBooking booking = new BedBooking();
            booking.setId(request.getBedBookingId());
            entity.setBedBooking(booking);
        }

        if (request.getAdmittedPatientId() != null) {
            AdmittedPatient patient = new AdmittedPatient();
            patient.setId(request.getAdmittedPatientId());
            entity.setAdmittedPatient(patient);
        }

        return entity;
    }

    public void updateEntityFromRequest(MealRequest request, Meal entity) {
        if (request == null || entity == null) return;

        entity.setMealCost(request.getMealCost());
        if (request.getServedAt() != null) entity.setServedAt(request.getServedAt());
        if (request.getBillingStatus() != null) entity.setBillingStatus(request.getBillingStatus());

        if (request.getMealMasterId() != null) {
            if (entity.getMealMaster() == null) entity.setMealMaster(new MealMaster());
            entity.getMealMaster().setId(request.getMealMasterId());
        }

        if (request.getBedBookingId() != null) {
            if (entity.getBedBooking() == null) entity.setBedBooking(new BedBooking());
            entity.getBedBooking().setId(request.getBedBookingId());
        } else if (request.getBedBookingId() == null) {
            entity.setBedBooking(null); // Allows unlinking optional inventory items
        }

        if (request.getAdmittedPatientId() != null) {
            if (entity.getAdmittedPatient() == null) entity.setAdmittedPatient(new AdmittedPatient());
            entity.getAdmittedPatient().setId(request.getAdmittedPatientId());
        }
    }
}
