package jpf5321.cs.psu.edu.medicalapplication;

public class SecureRecords {
    private String birthDate;
    private String[] allergies;
    private String[] medications;
    private String[] surgeries;
    private String lastVisit;

    public SecureRecords(String birthDate, String[] allergies, String[] medications, String[] surgeries, String lastVisit) {
        this.birthDate = birthDate;
        this.allergies = allergies;
        this.medications = medications;
        this.surgeries = surgeries;
        this.lastVisit = lastVisit;
    }

    public SecureRecords() {

    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
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

    public String getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(String lastVisit) {
        this.lastVisit = lastVisit;
    }
}
