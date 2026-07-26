package emranhss.com.Modern_Hospital_Management_System.service;

import emranhss.com.Modern_Hospital_Management_System.entity.Refund;
import emranhss.com.Modern_Hospital_Management_System.enums.RefundStatus;

import java.util.List;

public interface RefundService {

    List<Refund> getAll();

    Refund getById(Long id);

    List<Refund> getPending();

    Refund createRefund(Refund refund);

    Refund approveRefund(Long id, String approvedBy);

    Refund rejectRefund(Long id, String reason);

    Refund processRefund(Long id);

    List<Refund> getByStatus(RefundStatus status);
}
