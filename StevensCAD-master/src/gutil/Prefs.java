package gutil;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;

public class Prefs {
    private HashMap<String, Properties> locales;
    private Properties messages;
    private HashMap<String, Icon> icons;
    private String language;

    private Style def;
    private Style big;
    private Style normal;
    private Style field;
    private Style label;
    private Style small;
    private Style menu;
    private Style controls;
    private String toolbarLocation;

    private Properties loadLocale(String lang) {
        Properties locale = locales.get(lang);
        if (locale != null)
            return locale;
        locale = new Properties();
        locales.put(lang, locale);
        FileReader fr = null;
        try {
            fr = new FileReader("conf/" + lang);
            locale.load(fr);
        } catch (Exception e) {
            System.out.println("file reader");
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    System.out.println("failed to close");
                }
            }
        }
        return locale;
    }
    
    public Properties getMessages()  { return messages; }

    public Prefs(Conf conf) {
        Font f = null;
        String defaultFamily = "Arial";
        
        try {
            GraphicsEnvironment ge = 
                 GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("font/cyberbit.ttf")));
            defaultFamily = "cyberbit";
        } catch (IOException|FontFormatException e) {
            e.printStackTrace();//Handle exception
        }

        def = new Style(defaultFamily, 24, Font.PLAIN, Color.BLACK, Color.WHITE);
        def = conf.defaulted("defaultStyle", def);
        big = conf.defaulted("bigStyle", def.bigBold(24));
        normal = small = controls = def;
        menu = conf.defaulted("menuStyle", def);
        field = conf.defaulted("fieldStyle", def);
        label = conf.defaulted("labelStyle", def);
        System.out.println(field);
        language = conf.defaulted("lang", "en");
        locales = new HashMap<>();
        loadLocale(language);
        messages = locales.get(language);
        toolbarLocation = BorderLayout.WEST;
    }

    public Style getMenuStyle() {
        return menu;
    }
    
    public Style getFieldStyle() {
        return field;
    }
    public Style getLabelStyle() {
        return label;
    }
    public String getLanguage() {
        return language;
    }
}
