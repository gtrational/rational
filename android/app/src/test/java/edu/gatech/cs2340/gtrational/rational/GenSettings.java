package edu.gatech.cs2340.gtrational.rational;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by Robert on 10/17/2017.
 * Class to generate settings file
 */

public class GenSettings {

    private static final String defaultSettings = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<resources>\n" +
            "    <setting name=\"host_url\">http://rational.tk:80</setting>\n" +
            "    <setting name=\"are_we_lazy\">yes</setting>\n" +
            "</resources>";

    @Test
    public void generateSettingsXml() throws Exception {
        File directory = new File("app/src/main/res/xml");
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new RuntimeException("Could not make directory");
            }
        }
        File out = new File(directory.getAbsolutePath() + File.separator + "settings.xml");
        BufferedWriter writer = new BufferedWriter(new FileWriter(out));
        writer.write(defaultSettings);
        writer.close();
    }

}
