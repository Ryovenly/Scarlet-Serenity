package com.akane.scarletserenity.model.quiz;

import com.akane.scarletserenity.model.quiz.Question;

import java.util.Collections;
import java.util.List;

public class QuestionBank {
    private List<Question> questionList;
    private int nextQuestionIndex;

    public QuestionBank(List<Question> mQuestionList) {
        questionList = mQuestionList;

        // Shuffle the question list
        Collections.shuffle(questionList);

        nextQuestionIndex = 0;
    }

    public Question getQuestion() {
        if (nextQuestionIndex == questionList.size()){
            nextQuestionIndex = 0;
        }

        return questionList.get(nextQuestionIndex++);
    }
}
