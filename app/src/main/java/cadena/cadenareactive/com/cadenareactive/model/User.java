package cadena.cadenareactive.com.cadenareactive.model;

public class User extends DomainEntity {

    private String username;

    private String email;

    private String password;

    private String fName;

    private String lName;

    public User(String description, String email, String fName, Long idLong, String lName, String password, String username) {
        super.setDescription(description);
        super.setIdLong(idLong);
        this.username = username;
        this.email = email;
        this.password = password;
        this.fName = fName;
        this.lName = lName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }
}
