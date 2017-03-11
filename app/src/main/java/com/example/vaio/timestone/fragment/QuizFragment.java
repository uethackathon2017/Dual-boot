package com.example.vaio.timestone.fragment;

import android.annotation.SuppressLint;
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
import com.example.vaio.timestone.database.Database;
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
    private TextView tvHighScore, tvCurrentScore;
    private int currentScore = 0, highScore = 0;

    @SuppressLint("ValidFragment")
    public QuizFragment(ArrayList<Item> arrItem) {
        Log.e("TAG", arrItem.size() + "");
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
        tvHighScore = (TextView) view.findViewById(R.id.tvHighScore);
        tvCurrentScore = (TextView) view.findViewById(R.id.tvCurrentScore);
        tvCurrentScore.setText("0");

        reset();

        tvAnswer1.setOnClickListener(this);
        tvAnswer2.setOnClickListener(this);
        tvAnswer3.setOnClickListener(this);
        tvAnswer4.setOnClickListener(this);
    }

    private void initData() {
        ArrayList<Item> questionSet = new ArrayList<>();
        String question = "";
        String[] answer = new String[4];
        int answerPosition = 0;
        Random random = new Random();
        int randType = random.nextInt(2); // random thể loại câu hỏi
        switch (randType){
            case 0:
                questionSet.clear();
                while (questionSet.size() < 4){
                    int randomItem = 1+ random.nextInt(arrItem.size()-1);
                    if (arrItem.get(randomItem).getE_type().equals("Sự kiện")){
                        questionSet.add(arrItem.get(randomItem));
                    }
                }
                answerPosition = random.nextInt(4);
                question = questionSet.get(answerPosition).getE_info() + " diễn ra khi nào?";
                for (int i=0; i<4; i++){
                    answer[i] = questionSet.get(i).getE_day() + "/" + questionSet.get(i).getE_month() + "/" + questionSet.get(i).getE_year();
                }
                break;
            case 1:
                questionSet.clear();
                while (questionSet.size() < 4){
                    int randomItem = 1+ random.nextInt(arrItem.size()-1);
                    if (arrItem.get(randomItem).getE_type().equals("Sinh")){
                        questionSet.add(arrItem.get(randomItem));
                    }
                }
                answerPosition = random.nextInt(4);
                question = "Ngày sinh của " + questionSet.get(answerPosition).getE_info();
                for (int i=0; i<4; i++){
                    answer[i] = questionSet.get(i).getE_day() + "/" + questionSet.get(i).getE_month() + "/" + questionSet.get(i).getE_year();
                }
                break;
            case 2:
                questionSet.clear();
                while (questionSet.size() < 4){
                    int randomItem = 1+ random.nextInt(arrItem.size()-1);
                    if (arrItem.get(randomItem).getE_type().equals("Mất")){
                        questionSet.add(arrItem.get(randomItem));
                    }
                }
                answerPosition = random.nextInt(4);
                question = "Ngày mất của " + questionSet.get(answerPosition).getE_info();
                for (int i=0; i<4; i++){
                    answer[i] = questionSet.get(i).getE_day() + "/" + questionSet.get(i).getE_month() + "/" + questionSet.get(i).getE_year();
                }
                break;
            case 3:
                questionSet.clear();
                while (questionSet.size() < 4){
                    int randomItem = 1+ random.nextInt(arrItem.size()-1);
                    if (arrItem.get(randomItem).getE_type().equals("Sự kiện")){
                        questionSet.add(arrItem.get(randomItem));
                    }
                }
                answerPosition = random.nextInt(4);
                question = questionSet.get(answerPosition).getE_day() + "/" + questionSet.get(answerPosition).getE_month() + "/" + questionSet.get(answerPosition).getE_year() + " diễn ra sự kiện nào?";
                for (int i=0; i<4; i++){
                    answer[i] = questionSet.get(i).getE_info();
                }
                break;
            case 4:
                questionSet.clear();
                while (questionSet.size() < 4){
                    int randomItem = 1+ random.nextInt(arrItem.size()-1);
                    if (arrItem.get(randomItem).getE_type().equals("Sinh")){
                        questionSet.add(arrItem.get(randomItem));
                    }
                }
                answerPosition = random.nextInt(4);
                question = questionSet.get(answerPosition).getE_day() + "/" + questionSet.get(answerPosition).getE_month() + "/" + questionSet.get(answerPosition).getE_year() + " là ngày sinh của ai?";
                for (int i=0; i<4; i++){
                    answer[i] = questionSet.get(i).getE_info();
                }
                break;
            case 5:
                questionSet.clear();
                while (questionSet.size() < 4){
                    int randomItem = 1+ random.nextInt(arrItem.size()-1);
                    if (arrItem.get(randomItem).getE_type().equals("Mất")){
                        questionSet.add(arrItem.get(randomItem));
                    }
                }
                answerPosition = random.nextInt(4);
                question = questionSet.get(answerPosition).getE_day() + "/" + questionSet.get(answerPosition).getE_month() + "/" + questionSet.get(answerPosition).getE_year() + " là ngày mất của ai?";
                for (int i=0; i<4; i++){
                    answer[i] = questionSet.get(i).getE_info();
                }
                break;
        }
        quiz = new Quiz(question, answer, answerPosition);

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
        }, 400);
    }

    private void reset(){
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
                    currentScore++;
                } else {
                    tvAnswer1.setBackgroundResource(R.drawable.bg_wrong_answer);
                    currentScore = 0;
                }
                tvCurrentScore.setText(currentScore + "");
                break;
            case R.id.tvAnswer2:
                if (quiz.getRightAnser() == 1) {
                    currentScore++;
                } else {
                    tvAnswer2.setBackgroundResource(R.drawable.bg_wrong_answer);
                    currentScore = 0;
                }
                tvCurrentScore.setText(currentScore + "");
                break;
            case R.id.tvAnswer3:
                if (quiz.getRightAnser() == 2) {
                    currentScore++;
                } else {
                    tvAnswer3.setBackgroundResource(R.drawable.bg_wrong_answer);
                    currentScore = 0;
                }
                tvCurrentScore.setText(currentScore + "");
                break;
            case R.id.tvAnswer4:
                if (quiz.getRightAnser() == 3) {
                    currentScore++;
                } else {
                    tvAnswer4.setBackgroundResource(R.drawable.bg_wrong_answer);
                    currentScore = 0;
                }
                tvCurrentScore.setText(currentScore + "");
                break;
        }
        if (currentScore > highScore){
            highScore = currentScore;
            tvHighScore.setText(highScore + "");
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
