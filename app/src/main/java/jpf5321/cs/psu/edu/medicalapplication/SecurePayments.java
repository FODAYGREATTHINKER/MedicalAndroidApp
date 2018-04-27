package jpf5321.cs.psu.edu.medicalapplication;

public class SecurePayments {
    private String CreditCardNumber;
    private String CreditCardHolder;
    private String ExpirationDate;
    private String Cvv;
    private String Reason;
    private int Amount;
    private int userID;

    public SecurePayments(String cardHolder, String cardNumber, String expiration, String cvv, String reason, int amount, int user)
    {
        this.CreditCardHolder = cardHolder;
        this.Amount = amount;
        this.CreditCardNumber = cardNumber;
        this.Cvv = cvv;
        this.ExpirationDate = expiration;
        this.Reason = reason;
        this.userID = user;
    }

    public String getCreditCardNumber() {
        return CreditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        CreditCardNumber = creditCardNumber;
    }

    public String getCreditCardHolder() {
        return CreditCardHolder;
    }

    public void setCreditCardHolder(String creditCardHolder) {
        CreditCardHolder = creditCardHolder;
    }

    public String getExpirationDate() {
        return ExpirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        ExpirationDate = expirationDate;
    }

    public String getCvv() {
        return Cvv;
    }

    public void setCvv(String cvv) {
        Cvv = cvv;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) { this.userID = userID; }
}
