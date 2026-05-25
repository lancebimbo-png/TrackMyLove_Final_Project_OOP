package tml_model;

public class MedicationLog extends BaseEntity {

    private int patientId;
    private int givenById;
    private String logDate;
    private String notes;
    private String loggedAt;

    private String patientName;
    private String givenByName;

    public MedicationLog() {
    }

    public MedicationLog(int id, int patientId, int givenById, String logDate, String notes, String loggedAt) {
        super(id);
        this.patientId = patientId;
        this.givenById = givenById;
        this.logDate = logDate;
        this.notes = notes;
        this.loggedAt = loggedAt;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getGivenById() {
        return givenById;
    }

    public void setGivenById(int givenById) {
        this.givenById = givenById;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLoggedAt() {
        return loggedAt;
    }

    public void setLoggedAt(String loggedAt) {
        this.loggedAt = loggedAt;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getGivenByName() {
        return givenByName;
    }

    public void setGivenByName(String givenByName) {
        this.givenByName = givenByName;
    }

    @Override
    public String toString() {
        return "Log #" + getId() + " — " + logDate + " | " + patientName + " given by " + givenByName;
    }
}