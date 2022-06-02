package pro.sky.bot.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

public class Shelter {

    private final String name = "Petsitters";
    private final String schedule = "Круглосуточно";
    private final String address = "ул. Заславская 17, Минск";
    private final String coordinates = "27.538223,53.909286";
    private final String rulesPath = "src/main/resources/rules.txt";
    private final String shelter_infoPath = "src/main/resources/shelter_info.txt";

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
    public String getShelterInfoPath() {
        return shelter_infoPath;
    }

}
