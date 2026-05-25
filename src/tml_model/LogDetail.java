package tml_model;

public class LogDetail extends BaseEntity {

    private int logId;
    private int medicationId;
    private String timeGiven;
    private boolean wasTaken;
    private String sideEffects;

    private String medicationName;

    public LogDetail() {
    }

    public LogDetail(int id, int logId, int medicationId, String timeGiven, boolean wasTaken, String sideEffects) {
        super(id);
        this.logId = logId;
        this.medicationId = medicationId;
        this.timeGiven = timeGiven;
        this.wasTaken = wasTaken;
        this.sideEffects = sideEffects;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(int medicationId) {
        this.medicationId = medicationId;
    }

    public String getTimeGiven() {
        return timeGiven;
    }

    public void setTimeGiven(String timeGiven) {
        this.timeGiven = timeGiven;
    }

    public boolean isWasTaken() {
        return wasTaken;
    }

    public void setWasTaken(boolean wasTaken) {
        this.wasTaken = wasTaken;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    @Override
    public String toString() {
        return medicationName + " at " + timeGiven + " — " + (wasTaken ? "Taken" : "Skipped");
    }
}