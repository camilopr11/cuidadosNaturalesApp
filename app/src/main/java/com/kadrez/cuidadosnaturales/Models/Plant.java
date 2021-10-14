package com.kadrez.cuidadosnaturales.Models;


import java.util.Date;

public class Plant {

    private String name;
    private String type;
    private String scientific_name;
    private String order;
    private String img_url;

    public Plant() {
    }

    public Plant(String name, String type, String scientific_name, String order, String img_url) {
        this.name = name;
        this.type = type;
        this.scientific_name = scientific_name;
        this.order = order;
        this.img_url = img_url;
    }


    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getScientificName() {
        return scientific_name;
    }

    public String getOrder() {
        return order;
    }

    public String getImg_url() {
        return img_url;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setScientificName(String scientific_name) {
        this.scientific_name = scientific_name;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}