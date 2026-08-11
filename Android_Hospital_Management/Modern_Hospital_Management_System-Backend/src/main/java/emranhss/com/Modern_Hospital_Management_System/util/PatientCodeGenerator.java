package emranhss.com.Modern_Hospital_Management_System.util;


import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PatientCodeGenerator {


    public String generateCode() {
        return "PAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
