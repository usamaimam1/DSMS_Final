package com.example.usama.dsms_fb;

import android.util.Log;
import android.widget.Toast;

import java.nio.DoubleBuffer;
import java.sql.ResultSet;
import java.util.Random;



public class WaterRequestData {

    private String startDate;
    private String endDate;
    private String cropName;
    private String startTime;
    private String endTime;
    private String requestID;
    private String horsePower;
    private String CNIC;

    private double estimatedPowerConsumption;
    private double estimatedwaterExtraction;
    private double expectedBill;
    private double numberofhours;
    private double numberofdays;
    private double boringinfeet;
    private double peakHourRate;
    private double nonpeakHourRate;
    private double peakhours;
    private double nonpeakhours;
    private double peakStart;
    private double peakEnd;
    private double peakBill;
    private double nonpeakBill;
    public WaterRequestData(String sD, String eD, String cN, String sT, String eT, String rID, String hP,String bif, String c){
        this.startDate = sD;
        this.endDate = eD;
        this.cropName = cN;
        this.startTime = sT;
        this.endTime = eT;
        this.requestID = rID;
        this.horsePower = hP;
        this.boringinfeet = Double.parseDouble(bif);
        this.peakHourRate = 10.5;
        this.nonpeakHourRate =5.5;
        this.CNIC =c;
        this.peakStart= 7.0;
        this.peakEnd=11.0;
        calNumberofdaysandhours();
    }
    public void calNumberofdaysandhours(){
        this.numberofdays = Double.parseDouble(endDate.split("/")[0]) - Double.parseDouble(startDate.split("/")[0]);
        this.numberofhours = Double.parseDouble(endTime.split(":")[0]) + Double.parseDouble(endTime.split(":")[1])/60 - (Double.parseDouble(startTime.split(":")[0]) + Double.parseDouble(startTime.split(":")[1])/60) ;
        if(numberofdays == 0.0){
            numberofdays = 1.0;
        }
        calHours();
        this.expectedBill = numberofdays * calBill();
        this.estimatedPowerConsumption = Double.parseDouble(horsePower) * 0.746 * numberofhours * numberofdays;
        this.estimatedwaterExtraction = ((estimatedPowerConsumption * 367 * 0.50)/boringinfeet) *1000;
    }

    public void calHours(){
        double eTime = Double.parseDouble(endTime.split(":")[0]);
        double sTime = Double.parseDouble(startTime.split(":")[0]);
        if(peakStart <= sTime && peakEnd >= eTime ){
            peakhours = numberofhours;
            nonpeakhours=0.0;
        }else if(peakStart <= sTime && peakEnd >= eTime){
            peakhours = peakEnd - sTime;
            nonpeakhours = numberofhours - peakhours;
        }else if(peakStart >= sTime && peakStart <= eTime){
            peakhours = eTime-peakStart;
            nonpeakhours =numberofhours-peakhours;
        }else{
            peakhours=0.0;
            nonpeakhours=numberofhours;
        }
    }
    public double calBill(){
        peakBill = peakhours * peakHourRate;
        nonpeakBill = nonpeakhours * nonpeakHourRate;
        return peakBill+nonpeakBill;
    }
    public double perHourPowerConsumption(){
        return(estimatedPowerConsumption/(numberofdays*numberofhours));
    }
    public double peakHourBillperHour(){
        if(peakhours >0.0){
            return(peakBill/peakhours);
        }
        return 0.0;
    }
    public double nonpeakHourBillperHour(){
        if(nonpeakhours >0.0){
            return(nonpeakBill/nonpeakhours);
        }
        return 0.0;
    }

    public String getStartDate(){
        return this.startDate;
    }
    public void setStartDate(String sD){
        this.startDate = sD;
    }

    public String getEndDate(){
        return this.endDate;
    }
    public void setEndDate(String eD){
        this.endDate = eD;
    }

    public String getCropName(){
        return this.cropName;
    }
    public void setCropName(String cN){
        this.cropName = cN;
    }

    public String getStartTime(){
        return this.startTime;
    }
    public void setStartTime(String sT){
        this.startTime = sT;
    }

    public String getEndTime(){
        return this.endTime;
    }
    public void setEndTime(String eT){
        this.endTime = eT;
    }

    public String getRequestID(){
        return this.requestID;
    }
    public void setRequestID(String rID){
        this.requestID = rID;
    }

    public String getHorsePower(){
        return this.horsePower;
    }
    public void setHorsePower(String hP){
        this.horsePower = hP;
    }

    public String getCNIC(){
        return this.CNIC;
    }
    public void setCNIC(String c){
        this.CNIC = c;
    }

    public double getEstimatedPowerConsumption(){
        return this.estimatedPowerConsumption;
    }
    public void setEstimatedPowerConsumption(double epc){
        this.estimatedPowerConsumption =epc;
    }

    public double getEstimatedwaterExtraction(){
        return this.estimatedwaterExtraction;
    }
    public void setEstimatedwaterExtraction(double ewc){
        this.estimatedwaterExtraction =ewc;
    }

    public double getExpectedBill(){
        return this.expectedBill;
    }
    public void setExpectedBill(double eb){
        this.expectedBill =eb;
    }

    public double getNumberofhours(){
        return this.numberofhours;
    }
    public void setNumberofhours(double noh){
        this.numberofhours =noh;
    }

    public double getNumberofdays(){
        return this.numberofdays;
    }
    public void setNumberofdays(double nod){
        this.numberofdays =nod;
    }

    public double getBoringinfeet(){
        return this.boringinfeet;
    }
    public void setBoringinfeet(double bif){
        this.boringinfeet =bif;
    }

    public double getPeakHourRate(){
        return this.peakHourRate;
    }
    public void setPeakHourRate(double phr){
        this.peakHourRate =phr;
    }

    public double getNonpeakHourRate(){
        return this.nonpeakHourRate;
    }
    public void setNonpeakHourRate(double nphr){
        this.nonpeakHourRate =nphr;
    }
    public double getPeakhours(){
        return peakhours;
    }
    public double getNonpeakhours(){
        return nonpeakhours;
    }
    public double getPeakStart(){
        return peakStart;
    }
    public double getPeakEnd(){
        return peakEnd;
    }
    public double getPeakBill(){
        return peakBill;
    }
    public double getNonpeakBill(){
        return nonpeakBill;
    }
    public void setPeakhours(double ph){
        peakhours=ph;
    }
    public void setNonpeakhours(double nph){
        nonpeakhours= nph;
    }
    public void setPeakStart(double ps){
        peakStart=ps;
    }
    public void setPeakEnd(double pe){
        peakEnd=pe;
    }
    public void setPeakBill(double pb){
        peakBill=pb;
    }
    public void setNonpeakBill(double npb){
        nonpeakBill=npb;
    }
    public String printWaterRequestData(){
        String print="";
        print += " Request ID # "+ this.getRequestID()+ "\n";
        print += " Time Duartion # "+ this.getStartTime()+" - "+ this.getEndTime()+" \n";
        print += " Date Duration # "+ this.getStartDate()+" - "+this.getEndDate()+" \n";
        print += " CropName # "+this.getCropName()+" \n";
        print += " HorsePower # "+this.getHorsePower() +" \n";
        print += " CNIC # "+this.getCNIC()+" \n";
        print += " Power Consumption # "+ Double.toString(this.getEstimatedPowerConsumption())+" \n";
        print += " Estimated Bill # "+ Double.toString(this.getExpectedBill())+" \n";
        print += " Water Consumption # "+ Double.toString(this.getEstimatedwaterExtraction())+" \n";
        print += " Boring in feet # "+ Double.toString(this.getBoringinfeet())+" \n";
        print += " Peak Hour Rate # "+ Double.toString(this.getPeakHourRate())+" \n";
        print += " Non Peak Hour Rate # "+ Double.toString(this.getNonpeakHourRate())+" \n";
        print += " Number Of Hours # "+Double.toString(this.getNumberofhours())+"\n";
        print += " Number Of Days # " + Double.toString(this.getNumberofdays())+"\n";
        print += " Number Of Peak Hours # "+Double.toString(this.peakhours)+"\n";
        print += " Number Of Non Peak Hours # "+Double.toString(this.nonpeakhours)+"\n";
        print += " Peak Bill # "+Double.toString(this.peakBill)+"\n";
        print += " Non Peak Bill # "+Double.toString(this.nonpeakBill)+"\n \n";
        return print;
    }
}

