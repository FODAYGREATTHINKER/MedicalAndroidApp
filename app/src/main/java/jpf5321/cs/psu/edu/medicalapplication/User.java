package jpf5321.cs.psu.edu.medicalapplication;

public class User {

    private String userName;
    private String passWord;

    public User(String uName, String pWord)
    {
        userName = uName;
        passWord = pWord;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getPassWord()
    {
        return passWord;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
