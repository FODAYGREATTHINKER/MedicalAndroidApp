package jpf5321.cs.psu.edu.medicalapplication;

public class RecordsModel {
    private String birthDate;
    private String lastVisit;
    private String[] allergies;
    private String[] medications;
    private String[] surgeries;

    //setters and getters
    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(String lastVisit) {
        this.lastVisit = lastVisit;
    }

    public String[] getAllergies() {
        return allergies;
    }

    public void setAllergies(String[] allergies) {
        this.allergies = allergies;
    }

    public String[] getMedications() {
        return medications;
    }

    public void setMedications(String[] medications) {
        this.medications = medications;
    }

    public String[] getSurgeries() {
        return surgeries;
    }

    public void setSurgeries(String[] surgeries) {
        this.surgeries = surgeries;
    }
}
