package tml_model;

public class Household extends BaseEntity {

    private String familyName;
    private String address;
    private String createdAt;

    public Household() {
    }

    public Household(int id, String familyName, String address, String createdAt) {
        super(id);
        this.familyName = familyName;
        this.address = address;
        this.createdAt = createdAt;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return familyName + " (" + address + ")";
    }
}