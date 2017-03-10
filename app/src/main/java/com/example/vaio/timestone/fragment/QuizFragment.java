package com.example.vaio.timestone.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vaio.timestone.R;
import com.example.vaio.timestone.model.Item;
import com.example.vaio.timestone.model.Quiz;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by vaio on 10/03/2017.
 */

public class QuizFragment extends Fragment {
    private ArrayList<Item> arrItem;
    private TextView tvQuestion;
    private TextView tvAnswer1;
    private TextView tvAnswer2;
    private TextView tvAnswer3;
    private TextView tvAnswer4;
    private Quiz quiz;

    @SuppressLint("ValidFragment")
    public QuizFragment(ArrayList<Item> arrItem) {
        this.arrItem = arrItem;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        initData();
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvQuestion = (TextView) view.findViewById(R.id.tvQuestion);
        tvAnswer1 = (TextView) view.findViewById(R.id.tvAnswer1);
        tvAnswer2 = (TextView) view.findViewById(R.id.tvAnswer2);
        tvAnswer3 = (TextView) view.findViewById(R.id.tvAnswer3);
        tvAnswer4 = (TextView) view.findViewById(R.id.tvAnswer4);

        tvQuestion.setText(quiz.getQuestion());
        tvAnswer1.setText(quiz.getAnswer()[0]);
        tvAnswer2.setText(quiz.getAnswer()[1]);
        tvAnswer3.setText(quiz.getAnswer()[2]);
        tvAnswer4.setText(quiz.getAnswer()[3]);
    }

    private void initData() {
        Random random = new Random();
        int randType = random.nextInt(1); // random thể loại câu hỏi
        if (randType == 1) {
            Random randAnswer = new Random();  // random 4 câu trả lời
            ArrayList<Item> arrItemTmp = new ArrayList<>();
            String[] answer = new String[4];
            for (int i = 0; i < 4; i++) {
                int position = randAnswer.nextInt(arrItem.size() - 1); // 0 - 9xxx;
                arrItemTmp.add(arrItem.get(position));
                answer[i] = arrItem.get(position).getE_info();
            }
            Random randQuestion = new Random();
            int rightAnswer = randQuestion.nextInt(3); // random vị trí của câu trả lời đúng 0->3
            String question = arrItem.get(rightAnswer).getE_date() + " diễn ra sự kiện nào ? ";
            quiz = new Quiz(question, answer, rightAnswer);
        } else {
            Random randAnswer = new Random();
            ArrayList<Item> arrItemTmp = new ArrayList<>();
            String[] answer = new String[4];
            for (int i = 0; i < 4; i++) {
                int position = randAnswer.nextInt(arrItem.size() - 1); // 0 - 3432;
                arrItemTmp.add(arrItem.get(position));
                answer[i] = arrItem.get(position).getE_date();
            }
            Random randQuestion = new Random();
            int rightAnswer = randQuestion.nextInt(3);
            String question = arrItem.get(rightAnswer).getE_info() + " là ngày bao nhiêu ? ";
            quiz = new Quiz(question, answer, rightAnswer);
        }

    }
}
