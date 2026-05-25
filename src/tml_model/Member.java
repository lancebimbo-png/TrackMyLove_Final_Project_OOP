package tml_model;

public class Member extends BaseEntity {

    private int householdId;
    private String fullName;
    private String role;
    private String location;
    private String passwordHash;
    private String createdAt;

    public Member() {
    }

    public Member(int id, int householdId, String fullName, String role, String location, String passwordHash, String createdAt) {
        super(id);
        this.householdId = householdId;
        this.fullName = fullName;
        this.role = role;
        this.location = location;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }

    public int getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(int householdId) {
        this.householdId = householdId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return fullName + " [" + role + "]";
    }
}