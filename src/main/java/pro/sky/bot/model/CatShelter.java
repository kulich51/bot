package pro.sky.bot.model;

public class CatShelter extends Shelter {

    public CatShelter() {
        this.setName("Catsitters");
        this.setSchedule("Круглосуточно. Понедельник выходной");
        this.setAddress("ул. Заславская 17, Минск");
        this.setCoordinates("27.538223,53.909286");
        this.setRulesPath("src/main/resources/text/rules.txt");
        this.setSecurityPhone("+7-926-192-00-00");
    }
}
