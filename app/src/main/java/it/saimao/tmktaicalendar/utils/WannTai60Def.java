package it.saimao.tmktaicalendar.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class WannTai60Def {
    private final InputStream is;

    public WannTai60Def() {
        is = getClass().getResourceAsStream("/assets/wann_tai_60.csv");
    }

    public String getDictByName(String name) {
        String definition = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String title = values[1].trim();
                if (title.equals(name)) {
                    definition = values[2].trim();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return definition;
    }

    public String getDefByNumber(int number) {
        String definition = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int no = Integer.parseInt(values[0]);
                if (number == no) {
                    definition = values[2].trim();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return definition;
    }

}
