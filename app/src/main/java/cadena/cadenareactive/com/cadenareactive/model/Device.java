package cadena.cadenareactive.com.cadenareactive.model;


public class Device extends DomainEntity {

    private String name;

    private String model;

    private String phoneNumber;

    private User user;

    public Device(String description, Long idLong, String model, String name, String phoneNumber, User user) {
        super.setDescription(description);
        super.setIdLong(idLong);
        this.name = name;
        this.model = model;
        this.phoneNumber = phoneNumber;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
