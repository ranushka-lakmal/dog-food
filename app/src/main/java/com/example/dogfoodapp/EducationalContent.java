package com.example.dogfoodapp;

public class EducationalContent {
    private String title;
    private String type;
    private String content;
    private String breed;
    private String lifeStage;

    public EducationalContent(String title, String type, String content,
                              String breed, String lifeStage) {
        this.title = title;
        this.type = type;
        this.content = content;
        this.breed = breed;
        this.lifeStage = lifeStage;
    }

    // Getters
    public String getTitle() { return title; }
    public String getType() { return type; }
    public String getContent() { return content; }
    public String getBreed() { return breed; }
    public String getLifeStage() { return lifeStage; }
}