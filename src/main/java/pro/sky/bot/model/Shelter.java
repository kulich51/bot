package pro.sky.bot.model;

public abstract class Shelter {

    private String name;
    private String schedule;
    private String address;
    private String coordinates;
    private String rulesPath;
    private String securityPhone;

    public String getName() {
        return name;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getAddress() {
        return address;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public String getRulesPath() {
        return rulesPath;
    }

    public String getSecurityPhone() {
        return securityPhone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public void setRulesPath(String rulesPath) {
        this.rulesPath = rulesPath;
    }

    public void setSecurityPhone(String securityPhone) {
        this.securityPhone = securityPhone;
    }
}
