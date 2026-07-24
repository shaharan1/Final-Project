package emranhss.com.Modern_Hospital_Management_System.enums;

public enum Role {
    Admin,
    OfficeStaff,
    Nurse,
    Doctor,
    Receptionist,
    
    Pharmacist,
    LabTechnician,
    BillingClerk,
    InventoryManager,
    WardManager;


    // Returns Spring Security compatible authority string
    public String getAuthority() {
        return "ROLE_" + this.name();
    }



}
