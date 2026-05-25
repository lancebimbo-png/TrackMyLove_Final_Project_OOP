package tml_model;

public class Medication extends BaseEntity {

    private String name;
    private double dosage;
    private String unit;
    private String purpose;
    private String notes;

    public Medication() {
    }

    public Medication(int id, String name, double dosage, String unit, String purpose, String notes) {
        super(id);
        this.name = name;
        this.dosage = dosage;
        this.unit = unit;
        this.purpose = purpose;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDosage() {
        return dosage;
    }

    public void setDosage(double dosage) {
        this.dosage = dosage;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return name + " " + dosage + unit;
    }
}