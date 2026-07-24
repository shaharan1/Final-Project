package emranhss.com.Modern_Hospital_Management_System.serviceimp;


import emranhss.com.Modern_Hospital_Management_System.dto.mapper.InvoiceMapper;
import emranhss.com.Modern_Hospital_Management_System.dto.request.InvoiceRequest;
import emranhss.com.Modern_Hospital_Management_System.dto.response.InvoiceResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.Doctor;
import emranhss.com.Modern_Hospital_Management_System.entity.Invoice;
import emranhss.com.Modern_Hospital_Management_System.entity.Tests;
import emranhss.com.Modern_Hospital_Management_System.exception.ResourceNotFoundException;
import emranhss.com.Modern_Hospital_Management_System.repository.DoctorRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.InvoiceRepository;
import emranhss.com.Modern_Hospital_Management_System.repository.TestsRepository;
import emranhss.com.Modern_Hospital_Management_System.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImp implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final DoctorRepository doctorRepository;
    private final TestsRepository testsRepository;
    private final InvoiceMapper mapper;


    @Override
    @Transactional
    public InvoiceResponse generateInvoice(InvoiceRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor records missing with ID: " + request.getDoctorId()));


        Invoice invoice = new Invoice();
        BeanUtils.copyProperties(request, invoice);
        invoice.setDoctor(doctor);

        // Compute payment status based on whether a remaining balance  exists
        if (invoice.getDue() != null && invoice.getDue() <= 0) {
            invoice.setStatus(true); // Paid in full
        } else {
            invoice.setStatus(false); // Balance pending
        }

        if (request.getTestOrderIds() != null && !request.getTestOrderIds().isEmpty()) {
            List<Tests> assignedTests = testsRepository.findAllById(request.getTestOrderIds());
            invoice.setTests(assignedTests);
        }

        return mapper.toResponse(invoiceRepository.save(invoice));
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice record missing with ID: " + id));
        return mapper.toResponse(invoice);
    }
}
