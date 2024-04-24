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
               "Completed",//0
               "Pending",//1
               "Settings",//2
               "Deliver Package",//3
               "Return To Pending",//4
               "Edit Delivery",//5
               "Close Edit Delivery",//6
               "+ New Delivery",//7
               "Close New Delivery",//8
               "Admin Tools",//9
               "Log In",//10
               "Sign Out",//11
               "Delete Orders",//12
               "Create User",//13
               "Order",//14
               "Patient Name",//15
               "Location",//16
               "Medication",//17
               "Dose",//18
               "Number of Doses",//19
               "Order History",//20
               "view more",//21
               "close view more",//22
               "Reports",//23
               "New Delivery Form",//24
               "Please fill out all the required fields",//25
               "Patient Information",//26
               "Name",//27
               "Strength",//28
               "Add Note",//29
               "Clear",//30
               "Submit",//31
               "Employee Login",//32
               "Return to Homepage",//33
               "Create User Form",//34
               "Employee ID",//35
               "Email",//36
               "Password",//37
               "Confirm Password",//38
               "Close Create User",//39
               "Medication Information",//40
               "Light Mode",//41
               "Dark Mode",//42
               "Edit Delivery Form",//43
               "First",//44
               "Last",//45
               "Close Add Note"//46

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
