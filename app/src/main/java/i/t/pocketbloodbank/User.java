package i.t.pocketbloodbank;

public class User {
    private String email;
    private String name;
    private String phoneNumber;
    private String location;
    private String bloodGrp;
    private String availability;
    private String dpURL;

    public User(String email, String name, String phoneNumber, String location, String bloodGrp, String availability,String dpURL) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.bloodGrp = bloodGrp;
        this.availability = availability;
        this.dpURL = dpURL;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setBloodGrp(String bloodGrp) {
        this.bloodGrp = bloodGrp;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public void setDpURL(String dpURL) {
        this.dpURL = dpURL;
    }

    public String getDpURL() {
        return dpURL;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public String getBloodGrp() {
        return bloodGrp;
    }

    public String getAvailability() {
        return availability;
    }

}
