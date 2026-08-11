package emranhss.com.Modern_Hospital_Management_System.enums;

public enum TriageLevel {

    LEVEL_1("Resuscitation", "RED"),
    LEVEL_2("Emergency", "ORANGE"),
    LEVEL_3("Urgent", "YELLOW"),
    LEVEL_4("Semi-Urgent", "GREEN"),
    LEVEL_5("Non-Urgent", "BLUE");

    private final String description;
    private final String color;

    TriageLevel(String description, String color) {
        this.description = description;
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }
}
