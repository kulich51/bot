package pro.sky.bot.model;

import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Shelter {

    private String name = "Petsitters";
    private String schedule = "понедельник - пятница";
    private String address = "ул. Заславская 17, Минск";
    private String coordinates = "27.538223, 53.909286";

    private String rules;

    @Value("${rules.text.path}")
    private String rulesPath;

    public Shelter() throws IOException {

        rules = getRulesFromFile();
    }

    private String getRulesFromFile() throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(rulesPath));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    public String getRules() {
        return rules;
    }
}
