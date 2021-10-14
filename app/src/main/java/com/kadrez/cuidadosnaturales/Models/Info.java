package com.kadrez.cuidadosnaturales.Models;




public class Info {

    private String title;
    private String description;
    private String content;
    private String category;


    public Info() {
    }

    public Info(String title, String description, String content, String category) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.category = category;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    public String getCategory() {
        return category;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}