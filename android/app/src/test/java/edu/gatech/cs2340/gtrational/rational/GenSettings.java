package edu.gatech.cs2340.gtrational.rational;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by Robert on 10/17/2017.
 */

public class GenSettings {

    private static final String defaultSettings = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<resources>\n" +
            "    <setting name=\"hosturl\">http://rational.tk:80</setting>\n" +
            "    <setting name=\"arewelazy\">yes</setting>\n" +
            "</resources>";

    @Test
    public void generateSettingsXml() throws Exception {
        File directory = new File("app/src/main/res/xml");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File out = new File(directory.getAbsolutePath() + File.separator + "settings.xml");
        BufferedWriter writer = new BufferedWriter(new FileWriter(out));
        writer.write(defaultSettings);
        writer.close();
    }

}
