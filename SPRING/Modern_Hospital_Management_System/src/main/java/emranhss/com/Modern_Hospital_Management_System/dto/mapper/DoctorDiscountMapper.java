package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.request.DoctorDiscountRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.DoctorDiscountResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Doctor;
import emranhss.com.Modern_Hospital_Management_System.entity.DoctorDiscount;
import org.springframework.stereotype.Component;

@Component
public class DoctorDiscountMapper {

    public DoctorDiscount toEntity(DoctorDiscountRequest request, Doctor doctor) {
        DoctorDiscount discount = new DoctorDiscount();
        discount.setDoctor(doctor);
        applyRequest(discount, request);
        return discount;
    }

    public void updateEntity(DoctorDiscount discount, DoctorDiscountRequest request, Doctor doctor) {
        if (doctor != null) {
            discount.setDoctor(doctor);
        }
        applyRequest(discount, request);
    }

    private void applyRequest(DoctorDiscount discount, DoctorDiscountRequest request) {
        discount.setFixedDiscount(request.getFixedDiscount());
        discount.setPercentageDiscount(request.getPercentageDiscount());
        discount.setDepartmentDiscount(request.getDepartmentDiscount());
        discount.setSpecialPromoDiscount(request.getSpecialPromoDiscount());
        discount.setNotes(request.getNotes());
        if (request.getActive() != null) {
            discount.setActive(request.getActive());
        }
    }

    public DoctorDiscountResponse toResponse(DoctorDiscount discount) {
        DoctorDiscountResponse response = new DoctorDiscountResponse();
        response.setId(discount.getId());
        if (discount.getDoctor() != null) {
            response.setDoctorId(discount.getDoctor().getId());
            response.setDoctorName(discount.getDoctor().getUser().getName());
        }
        response.setFixedDiscount(discount.getFixedDiscount());
        response.setPercentageDiscount(discount.getPercentageDiscount());
        response.setDepartmentDiscount(discount.getDepartmentDiscount());
        response.setSpecialPromoDiscount(discount.getSpecialPromoDiscount());
        response.setEffectiveDiscountPercent(discount.getEffectiveDiscountPercent());
        response.setActive(discount.getActive());
        response.setNotes(discount.getNotes());
        return response;
    }
}
