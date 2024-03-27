package com.example.hospitaldeliveryinterface.model;

import com.example.hospitaldeliveryinterface.firebase.DataBaseMgmt;

import java.lang.invoke.SwitchPoint;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.SimpleTimeZone;

public class NotifyMessg {

    private static Queue<NotifyMessg> messgQueue = new LinkedList<>();

    private String mssgDate;
    private String mssgTime;

    private String message;

    public NotifyMessg(String mssgDate, String mssgTime, String message){
        this.mssgDate = mssgDate;
        this.mssgTime = mssgTime;
        this.message = message;
    }

    public void setMssgDate(String mssgDate){this.mssgDate = mssgDate;}
    public String getMssgDate(){return mssgDate;}

    public void setMssgTime(String mssgTime){this.mssgTime = mssgTime;}

    public String getMssgTime(){return mssgTime;}

    public void setMessage(String message){this.message = message;}
    public String getMessage(){return message;}


    public static void setMessgQueue(Queue<NotifyMessg> mQueue){messgQueue = mQueue;}
    public static Queue<NotifyMessg> getMessgQueue(){return messgQueue;}


    public static void addMessg(NotifyMessg messg){
        messgQueue.add(messg);
    }

    public static NotifyMessg removeMessg(){
        if(messgQueue.isEmpty()){
            return null;
        }

        return messgQueue.poll();
    }

    public static String[] currentDateAndTime(){

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");

        return new String[]{dateFormat.format(currentDate), timeFormat.format(currentDate)};
    }

    public static void createMessg(String status, String employeeID, String orderNum){

        String[] dateAndTimeArr = currentDateAndTime();
        String messgTemp = "";

        switch(status){
            case "newDelivery":
                messgTemp = employeeID + " created a new order: #" + orderNum;
                break;
            case "delivered":
                messgTemp = employeeID + " signed off order: #" + orderNum;
                break;
            case "edited":
                messgTemp = employeeID + " edited order: #" + orderNum;
                break;
            case "returnToPending":
                messgTemp = employeeID + " returned order: #" + orderNum + " to pending";
                break;
            default:
                System.out.println("Status type: " + status + " does not exist.");
                break;
        }

        DataBaseMgmt.addNotifyMessgToDB(dateAndTimeArr[0], dateAndTimeArr[1], messgTemp);

    }




}
