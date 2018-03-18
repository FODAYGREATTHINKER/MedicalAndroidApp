package jpf5321.cs.psu.edu.medicalapplication;

public class SecureUserPassword extends User{
    private String password;
    private String username;

    public SecureUserPassword(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
