package com.example.vaio.timestone.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
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

public class QuizFragment extends Fragment implements View.OnClickListener {
    private ArrayList<Item> arrItem;
    private TextView tvQuestion;
    private TextView tvAnswer1;
    private TextView tvAnswer2;
    private TextView tvAnswer3;
    private TextView tvAnswer4;
    private Quiz quiz;
    private TextView tvScore;
    private int score = 0;

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
        tvScore = (TextView) view.findViewById(R.id.tvScore);
        tvScore.setText(score + "");
        tvQuestion.setText(quiz.getQuestion());
        tvAnswer1.setText(quiz.getAnswer()[0]);
        tvAnswer2.setText(quiz.getAnswer()[1]);
        tvAnswer3.setText(quiz.getAnswer()[2]);
        tvAnswer4.setText(quiz.getAnswer()[3]);

        tvAnswer1.setOnClickListener(this);
        tvAnswer2.setOnClickListener(this);
        tvAnswer3.setOnClickListener(this);
        tvAnswer4.setOnClickListener(this);
    }

    private void initData() {
        Random random = new Random();
        int randType = random.nextInt(); // random thể loại câu hỏi
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
                answer[i] = arrItem.get(position).getE_day() + "/" + arrItem.get(position).getE_month() + "/" + arrItem.get(position).getE_year();
            }
            Random randQuestion = new Random();
            int rightAnswer = randQuestion.nextInt(3);
            String question = arrItem.get(rightAnswer).getE_info() + " là ngày bao nhiêu ? ";
            quiz = new Quiz(question, answer, rightAnswer);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAnswer1:
                tvAnswer2.setClickable(false);
                tvAnswer3.setClickable(false);
                tvAnswer4.setClickable(false);
                tvAnswer1.setBackgroundResource(R.color.blue100);
                if (quiz.getRightAnser() == 0) {
                    score++;
                    tvScore.setText(score + "");
                }
                break;
            case R.id.tvAnswer2:
                tvAnswer1.setClickable(false);
                tvAnswer3.setClickable(false);
                tvAnswer4.setClickable(false);
                tvAnswer2.setBackgroundResource(R.color.blue100);
                if (quiz.getRightAnser() == 1) {
                    score++;
                    tvScore.setText(score + "");
                }
                break;
            case R.id.tvAnswer3:
                tvAnswer2.setClickable(false);
                tvAnswer1.setClickable(false);
                tvAnswer4.setClickable(false);
                tvAnswer3.setBackgroundResource(R.color.blue100);
                if (quiz.getRightAnser() == 2) {
                    score++;
                    tvScore.setText(score + "");
                } else {
                    score = 0;
                }
                break;
            case R.id.tvAnswer4:
                tvAnswer2.setClickable(false);
                tvAnswer3.setClickable(false);
                tvAnswer1.setClickable(false);
                tvAnswer4.setBackgroundResource(R.color.blue100);
                if (quiz.getRightAnser() == 3) {
                    score++;
                    tvScore.setText(score + "");
                } else {
                    score = 0;
                }
                break;
        }

        initData();
        tvScore.setText(score + "");
        tvQuestion.setText(quiz.getQuestion());
        tvAnswer1.setText(quiz.getAnswer()[0]);
        tvAnswer2.setText(quiz.getAnswer()[1]);
        tvAnswer3.setText(quiz.getAnswer()[2]);
        tvAnswer4.setText(quiz.getAnswer()[3]);
        tvAnswer1.setClickable(true);
        tvAnswer2.setClickable(true);
        tvAnswer3.setClickable(true);
        tvAnswer4.setClickable(true);
    }
}
