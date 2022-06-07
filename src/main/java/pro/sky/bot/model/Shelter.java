package pro.sky.bot.model;

public class Shelter {

    private final String name = "Petsitters";
    private final String schedule = "Круглосуточно";
    private final String address = "ул. Заславская 17, Минск";
    private final String coordinates = "27.538223,53.909286";
    private final String rulesFile = "rules.txt";
    private final String shelterInfoFile = "shelter_info.txt";

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

    public String getRulesFile() {
        return rulesFile;
    }

    public String getShelterInfoFile() {
        return shelterInfoFile;
    }
}
