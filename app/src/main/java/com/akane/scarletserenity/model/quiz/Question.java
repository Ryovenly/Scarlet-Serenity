package com.akane.scarletserenity.model.quiz;

import java.util.List;

public class Question {
    private String question;
    private List<String> choiceList;

    public Question(String question, List<String> choiceList) {
        this.question = question;
        this.choiceList = choiceList;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public List<String> getChoiceList() {
        return choiceList;
    }

    public void setChoiceList(List<String> choiceList) {
        this.choiceList = choiceList;
    }


}
