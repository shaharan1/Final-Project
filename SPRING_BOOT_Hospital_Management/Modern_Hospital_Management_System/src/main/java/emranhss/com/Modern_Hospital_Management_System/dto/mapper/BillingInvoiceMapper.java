package emranhss.com.Modern_Hospital_Management_System.dto.mapper;

import emranhss.com.Modern_Hospital_Management_System.dto.response.BillingInvoiceResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.BillingInvoice;
import emranhss.com.Modern_Hospital_Management_System.entity.BillingInvoiceItem;
import emranhss.com.Modern_Hospital_Management_System.entity.Doctor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BillingInvoiceMapper {

    public BillingInvoiceResponse toResponse(BillingInvoice invoice) {
        if (invoice == null) return null;

        BillingInvoiceResponse resp = new BillingInvoiceResponse();
        resp.setId(invoice.getId());
        resp.setInvoiceNumber(invoice.getInvoiceNumber());
        resp.setInvoiceType(invoice.getInvoiceType());
        resp.setSubtotal(invoice.getSubtotal());
        resp.setTaxRate(invoice.getTaxRate());
        resp.setTaxAmount(invoice.getTaxAmount());
        resp.setDiscountPercent(invoice.getDiscountPercent());
        resp.setDiscountAmount(invoice.getDiscountAmount());
        resp.setNetAmount(invoice.getNetAmount());
        resp.setTotalPaid(invoice.getTotalPaid());
        resp.setDueAmount(invoice.getDueAmount());
        resp.setPaymentStatus(invoice.getPaymentStatus());
        resp.setInvoiceStatus(invoice.getInvoiceStatus());
        resp.setNotes(invoice.getNotes());
        resp.setPreparedBy(invoice.getPreparedBy());
        resp.setFinalizedBy(invoice.getFinalizedBy());
        resp.setFinalizedDate(invoice.getFinalizedDate());
        resp.setCreatedDate(invoice.getCreatedDate());
        resp.setLastUpdated(invoice.getLastUpdated());

        if (invoice.getPatient() != null) {
            resp.setPatientId(invoice.getPatient().getId());
            resp.setPatientName(invoice.getPatient().getName());
            resp.setPatientCode(invoice.getPatient().getPatientCode());
            resp.setPatientPhone(invoice.getPatient().getPhone());
        }

        if (invoice.getAdmittedPatient() != null) {
            resp.setAdmittedPatientId(invoice.getAdmittedPatient().getId());
            if (invoice.getAdmittedPatient().getPrimaryDoctor() != null) {
                resp.setReferringDoctorId(invoice.getAdmittedPatient().getPrimaryDoctor().getId());
                Doctor doc = invoice.getAdmittedPatient().getPrimaryDoctor();
                resp.setReferringDoctorName(doc.getUser() != null ? doc.getUser().getName() : doc.getSpecialization());
            }
        }

        if (invoice.getReferringDoctor() != null) {
            resp.setReferringDoctorId(invoice.getReferringDoctor().getId());
            Doctor doc = invoice.getReferringDoctor();
            resp.setReferringDoctorName(doc.getUser() != null ? doc.getUser().getName() : doc.getSpecialization());
        }

        if (invoice.getItems() != null) {
            resp.setItems(invoice.getItems().stream()
                    .map(this::toItemResponse)
                    .collect(Collectors.toList()));
        } else {
            resp.setItems(new ArrayList<>());
        }

        return resp;
    }

    public BillingInvoiceResponse.BillingInvoiceItemResponse toItemResponse(BillingInvoiceItem item) {
        if (item == null) return null;

        BillingInvoiceResponse.BillingInvoiceItemResponse resp = new BillingInvoiceResponse.BillingInvoiceItemResponse();
        resp.setId(item.getId());
        resp.setCategoryCode(item.getCategoryCode());
        resp.setDescription(item.getDescription());
        resp.setQuantity(item.getQuantity());
        resp.setUnitPrice(item.getUnitPrice());
        resp.setDiscountPercent(item.getDiscountPercent());
        resp.setDiscountAmount(item.getDiscountAmount());
        resp.setAmount(item.getAmount());
        resp.setSourceModule(item.getSourceModule());
        resp.setSourceId(item.getSourceId());
        resp.setItemStatus(item.getItemStatus());

        if (item.getChargeCategory() != null) {
            resp.setChargeCategoryId(item.getChargeCategory().getId());
            resp.setCategoryName(item.getChargeCategory().getName());
        }

        return resp;
    }
}
