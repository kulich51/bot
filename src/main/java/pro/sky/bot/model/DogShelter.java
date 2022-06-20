package pro.sky.bot.model;

public class DogShelter extends Shelter {

    public DogShelter() {
        this.setName("Petsitters");
        this.setSchedule("Круглосуточно");
        this.setAddress("ул. Заславская 17, Минск");
        this.setCoordinates("27.538223,53.909286");
        this.setRulesPath("src/main/resources/text/rules.txt");
        this.setSecurityPhone("+7-926-192-86-29");
    }
}