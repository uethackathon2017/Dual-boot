package com.example.vaio.timestone.model;

/**
 * Created by sonbn on 3/10/17.
 */

public class Quiz {
    String question; // câu hỏi
    String[] answer = new String[4]; //  4 câu trả lời
    int rightAnser; // câu trả lời đúng

    public Quiz(String question, String[] answer, int rightAnser) {
        this.question = question;
        this.answer = answer;
        this.rightAnser = rightAnser;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getAnswer() {
        return answer;
    }

    public void setAnswer(String[] answer) {
        this.answer = answer;
    }

    public int getRightAnser() {
        return rightAnser;
    }

    public void setRightAnser(int rightAnser) {
        this.rightAnser = rightAnser;
    }
}