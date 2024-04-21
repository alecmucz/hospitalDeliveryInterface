package com.example.hospitaldeliveryinterface.model;

import com.example.hospitaldeliveryinterface.firebase.DataBaseMgmt;
import net.suuft.libretranslate.Translator;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MitchTextTranslate {

    private static TreeMap<String, String> languagesMap = new TreeMap<>();

    public  static HashMap<String,String[]> storedLang = new HashMap<>();

    public static HashMap<String, String[]> getStoredLang() {
        return storedLang;
    }

    public static TreeMap<String, String> getLanguagesMap() {
        return languagesMap;
    }

   public static String[] defaultEnglishText()
   {
       return new String[]{
               "Completed",
               "Pending",
               "Settings",
               "Deliver Package",
               "Return To Pending",
               "Edit Delivery",
               "Close Edit Delivery",
               "+ New Delivery",
               "Close New Delivery",
               "Admin Tools",
               "Login",
               "Sign Out",
               "Delete Orders",
               "Create Users",
               "Order",
               "Patient Name",
               "Location",
               "Medication",
               "Dose",
               "Number of Doses",
               "Order History",
               "view more",
               "close view more",
               "Reports"
       };
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


        storedLang = DataBaseMgmt.initialLanguageCheck(languagesMap);

    }

}
