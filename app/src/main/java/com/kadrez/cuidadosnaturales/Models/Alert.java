package com.kadrez.cuidadosnaturales.Models;


import java.util.Date;

public class Alert {

    private String plantName;
    private String alertType;
    private Date date;
    private String image_url;

    public Alert() {
    }

    public Alert(String plantName, String alertType, Date date, String image_url) {
        this.plantName = plantName;
        this.alertType = alertType;
        this.date = date;
        this.image_url = image_url;
    }


    public String getName() {
        return plantName;
    }

    public String getAlertType() {
        return alertType;
    }

    public Date getDate() {
        return date;
    }

    public String getImage_url() {
        return image_url;
    }


    public void setName(String plantName) {
        this.plantName = plantName;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}