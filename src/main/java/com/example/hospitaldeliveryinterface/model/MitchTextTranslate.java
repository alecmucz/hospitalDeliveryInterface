package com.example.hospitaldeliveryinterface.model;

import com.example.hospitaldeliveryinterface.controllers.HomepageController;
import com.google.firebase.database.core.utilities.Tree;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuItem;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MitchTextTranslate {

    private static TreeMap<String, String> languagesMap = new TreeMap<>();

    private static HashMap<String, String> engBtnText = new HashMap<>();
    private static HashMap<String,String> engLblText = new HashMap<>();



    public static HashMap<String, String> getEngBtnText() {
        return engBtnText;
    }

    public static void setEngBtnText(HashMap<String, String> engBtnText) {
        MitchTextTranslate.engBtnText = engBtnText;
    }

    public static void setEngLblText(HashMap<String, String> engLblText) {
        MitchTextTranslate.engLblText = engLblText;
    }

    public static HashMap<String, String> getEngLblText() {
        return engLblText;
    }




    public static TreeMap<String, String> getLanguagesMap() {
        return languagesMap;
    }

    public static void setLanguagesMap(TreeMap<String, String> languagesMap) {
        MitchTextTranslate.languagesMap = languagesMap;
    }

    public static void initialLanguages(){

        languagesMap.put("Arabic", "ar");
        languagesMap.put("Azerbaijani", "az");
        languagesMap.put("Catalan", "ca");
        languagesMap.put("Chinese", "zh");
        languagesMap.put("Czech", "cs");
        languagesMap.put("Danish", "da");
        languagesMap.put("Dutch", "nl");
        languagesMap.put("English", "en");
        languagesMap.put("Esperanto", "eo");
        languagesMap.put("Finnish", "fi");
        languagesMap.put("French", "fr");
        languagesMap.put("German", "de");
        languagesMap.put("Greek", "el");
        languagesMap.put("Hebrew", "he");
        languagesMap.put("Hindi", "hi");
        languagesMap.put("Hungarian", "hu");
        languagesMap.put("Indonesian", "id");
        languagesMap.put("Irish", "ga");
        languagesMap.put("Italian", "it");
        languagesMap.put("Japanese", "ja");
        languagesMap.put("Korean", "ko");
        languagesMap.put("Persian", "fa");
        languagesMap.put("Polish", "pl");
        languagesMap.put("Portuguese", "pt");
        languagesMap.put("Russian", "ru");
        languagesMap.put("Slovak", "sk");
        languagesMap.put("Spanish", "es");
        languagesMap.put("Swedish", "sv");
        languagesMap.put("Turkish", "tr");
        languagesMap.put("Ukrainian", "uk");

    }

}
