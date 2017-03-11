package com.example.vaio.timestone.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.vaio.timestone.R;
import com.example.vaio.timestone.model.Item;
import com.example.vaio.timestone.model.Quiz;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
    private TextView tvScore, tvTime;
    private int score = 0, time = 0;

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
    Timer timer = new Timer();
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            time ++;
            tvTime.post(new Runnable() {
                @Override
                public void run() {
                    tvTime.setText(time + "s");
                }
            });
        }
    };
    private void initView(View view) {
        timer.schedule(timerTask, 1000, 1000);
        tvQuestion = (TextView) view.findViewById(R.id.tvQuestion);
        tvAnswer1 = (TextView) view.findViewById(R.id.tvAnswer1);
        tvAnswer2 = (TextView) view.findViewById(R.id.tvAnswer2);
        tvAnswer3 = (TextView) view.findViewById(R.id.tvAnswer3);
        tvAnswer4 = (TextView) view.findViewById(R.id.tvAnswer4);
        tvScore = (TextView) view.findViewById(R.id.tvScore);
        tvTime = (TextView) view.findViewById(R.id.tvTime);

        reset();

        tvAnswer1.setOnClickListener(this);
        tvAnswer2.setOnClickListener(this);
        tvAnswer3.setOnClickListener(this);
        tvAnswer4.setOnClickListener(this);
    }

    private void initData() {
        Random random = new Random();
        int randType = random.nextInt(2); // random thể loại câu hỏi
        if (randType == 1) {
            Random randAnswer = new Random();  // random 4 câu trả lời
            String question = "";
            ArrayList<Item> arrItemTmp = new ArrayList<>();
            String[] answer = new String[4];
            Random randQuestion = new Random();
            int positionRightAnswer = randQuestion.nextInt(4); // random vị trí của câu trả lời đúng 0->3
            for (int i = 0; i < 4; i++) {
                int position = randAnswer.nextInt(arrItem.size() - 1); // 0 - 9xxx;
                arrItemTmp.add(arrItem.get(position));
                answer[i] = arrItem.get(position).getE_info();
                if (i == positionRightAnswer){
                    question = arrItem.get(position).getE_day() + "/" + arrItem.get(position).getE_month() + "/" + arrItem.get(position).getE_year() + " diễn ra sự kiện nào ? ";
                }
            }
            quiz = new Quiz(question, answer, positionRightAnswer);
        } else {
            Random randAnswer = new Random();  // random 4 câu trả lời
            String question = "";
            ArrayList<Item> arrItemTmp = new ArrayList<>();
            String[] answer = new String[4];
            Random randQuestion = new Random();
            int positionRightAnswer = randQuestion.nextInt(4); // random vị trí của câu trả lời đúng 0->3
            for (int i = 0; i < 4; i++) {
                int position = randAnswer.nextInt(arrItem.size() - 1); // 0 - 9xxx;
                arrItemTmp.add(arrItem.get(position));
                answer[i] = arrItem.get(position).getE_day() + "/" + arrItem.get(position).getE_month() + "/" + arrItem.get(position).getE_year();
                if (i == positionRightAnswer){
                    question = arrItem.get(position).getE_info() + " là khi nào?";
                }
            }
            quiz = new Quiz(question, answer, positionRightAnswer);
        }

    }

    private void setClickable(boolean b){
        tvAnswer1.setClickable(b);
        tvAnswer2.setClickable(b);
        tvAnswer3.setClickable(b);
        tvAnswer4.setClickable(b);
    }

    private void blink(final int position){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationSet blink = (AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.blink);
                switch (position){
                    case 0:
                        tvAnswer1.startAnimation(blink);
                        break;
                    case 1:
                        tvAnswer2.startAnimation(blink);
                        break;
                    case 2:
                        tvAnswer3.startAnimation(blink);
                        break;
                    case 3:
                        tvAnswer4.startAnimation(blink);
                        break;
                }
            }
        }, 500);
    }

    private void reset(){
        tvScore.setText(score + "");
        tvQuestion.setText(quiz.getQuestion());

        tvAnswer1.setBackgroundResource(R.drawable.bg_answer);
        tvAnswer2.setBackgroundResource(R.drawable.bg_answer);
        tvAnswer3.setBackgroundResource(R.drawable.bg_answer);
        tvAnswer4.setBackgroundResource(R.drawable.bg_answer);

        tvAnswer1.setText(quiz.getAnswer()[0]);
        tvAnswer2.setText(quiz.getAnswer()[1]);
        tvAnswer3.setText(quiz.getAnswer()[2]);
        tvAnswer4.setText(quiz.getAnswer()[3]);
    }

    @Override
    public void onClick(View v) {
        setClickable(false);
        switch (v.getId()) {
            case R.id.tvAnswer1:
                if (quiz.getRightAnser() == 0) {
                    score++;
                } else {
                    tvAnswer1.setBackgroundResource(R.drawable.bg_wrong_answer);
                    score--;
                }
                tvScore.setText(score + "");
                break;
            case R.id.tvAnswer2:
                if (quiz.getRightAnser() == 1) {
                    score++;
                } else {
                    tvAnswer2.setBackgroundResource(R.drawable.bg_wrong_answer);
                    score--;
                }
                tvScore.setText(score + "");
                break;
            case R.id.tvAnswer3:
                if (quiz.getRightAnser() == 2) {
                    score++;
                } else {
                    tvAnswer3.setBackgroundResource(R.drawable.bg_wrong_answer);
                    score--;
                }
                tvScore.setText(score + "");
                break;
            case R.id.tvAnswer4:
                if (quiz.getRightAnser() == 3) {
                    score++;
                } else {
                    tvAnswer4.setBackgroundResource(R.drawable.bg_wrong_answer);
                    score--;
                }
                tvScore.setText(score + "");
                break;
        }
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (quiz.getRightAnser()){
                    case 0:
                        tvAnswer1.setBackgroundResource(R.drawable.bg_right_answer);
                        break;
                    case 1:
                        tvAnswer2.setBackgroundResource(R.drawable.bg_right_answer);
                        break;
                    case 2:
                        tvAnswer3.setBackgroundResource(R.drawable.bg_right_answer);
                        break;
                    case 3:
                        tvAnswer4.setBackgroundResource(R.drawable.bg_right_answer);
                        break;
                }
                blink(quiz.getRightAnser());
            }
        }, 1000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setClickable(true);
                initData();
                reset();
            }
        },3000);

    }

}
