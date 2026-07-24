package emranhss.com.Modern_Hospital_Management_System.dto.mapper;


import emranhss.com.Modern_Hospital_Management_System.dto.response.MedicineStockResponse;
import emranhss.com.Modern_Hospital_Management_System.dto.response.SupplierResponse;
import emranhss.com.Modern_Hospital_Management_System.entity.MedicineStock;
import emranhss.com.Modern_Hospital_Management_System.entity.Supplier;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PharmacyInventoryMapper {

    public SupplierResponse toResponse(Supplier supplier) {
        if (supplier == null) return null;
        SupplierResponse resp = new SupplierResponse();
        BeanUtils.copyProperties(supplier, resp);
        return resp;
    }

    public MedicineStockResponse toResponse(MedicineStock stock) {
        if (stock == null) return null;

        MedicineStockResponse resp = new MedicineStockResponse();
        BeanUtils.copyProperties(stock, resp);

        if (stock.getSupplier() != null) {
            resp.setSupplierId(stock.getSupplier().getId());
            resp.setSupplierName(stock.getSupplier().getName());
        }

        // Real-time status flagging logic
        if (stock.getExpiryDate() != null && stock.getExpiryDate().isBefore(LocalDate.now())) {
            resp.setInventoryStatus("EXPIRED");
        } else if (stock.getStockQuantity() != null && stock.getStockQuantity() <= 10) {
            resp.setInventoryStatus("LOW_STOCK");
        } else {
            resp.setInventoryStatus("AVAILABLE");
        }

        return resp;
    }

}
