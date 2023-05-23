package kea.dk.devtool.model;

public class User
    {
    private int userId;

    private String userName;
    private String userPassword;
    private HasRole role= HasRole.UNASSIGNED; // default indtil user er logget ind med en rolle

    public User(){
    }

    public User(int userId, String userName, String userPassword, HasRole role) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.role=role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public HasRole getRole()
        {
            return role;
        }

    public void setRole(HasRole role)
        {
            this.role = role;
        }

    @Override
    public String toString()
        {
            return "User{" +
                  "userId=" + userId +
                  ", userName='" + userName + '\'' +
                  ", userPassword='" + userPassword + '\'' +
                  ", role=" + role +
                  '}';
        }
}
