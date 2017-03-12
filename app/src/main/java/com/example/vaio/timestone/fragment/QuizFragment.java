package com.example.vaio.timestone.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

public class QuizFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    private static final String SHARE_PRE = "data";
    private static final String HIGH_SCORE = "high score";
    private static final String DEFAULT_VALUE = "0";
    public static final int ANSWER_1 = 0;
    public static final int ANSWER_2 = 1;
    public static final int ANSWER_3 = 2;
    public static final int ANSWER_4 = 3;
    public static final int MAX_ANSWER = 4;
    public static final int DELAY_SELECT_ANSWER = 1000;
    public static final int DELAY_CHANGE_QUESTION = 3000;

    private ArrayList<Item> arrItem = new ArrayList<>();
    private TextView tvQuestion;
    private TextView tvAnswer1;
    private TextView tvAnswer2;
    private TextView tvAnswer3;
    private TextView tvAnswer4;
    private Quiz quiz;
    private TextView tvHighScore, tvCurrentScore;
    private int currentScore = 0, highScore = 0;
    private SharedPreferences sharedPreferences;

    @SuppressLint("ValidFragment")
    public QuizFragment(ArrayList<Item> arrItem) {
        Log.e("TAG", arrItem.size() + "");
        this.arrItem.addAll(arrItem);
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
        tvHighScore.setText(sharedPreferences.getString(HIGH_SCORE, DEFAULT_VALUE));

        reset();
        tvQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogContent(getContext(), tvQuestion.getText().toString());
            }
        });

        tvAnswer1.setOnClickListener(this);
        tvAnswer2.setOnClickListener(this);
        tvAnswer3.setOnClickListener(this);
        tvAnswer4.setOnClickListener(this);

        tvAnswer1.setOnLongClickListener(this);
        tvAnswer2.setOnLongClickListener(this);
        tvAnswer3.setOnLongClickListener(this);
        tvAnswer4.setOnLongClickListener(this);
    }

    private void initData() {
        sharedPreferences = getContext().getSharedPreferences(SHARE_PRE, Context.MODE_PRIVATE);

        ArrayList<Item> questionSet = new ArrayList<>();
        String question = "";
        String[] answer = new String[4];
        int answerPosition = 0;
        Random random = new Random();
        int randType = random.nextInt(6); // random thể loại câu hỏi
        switch (randType){
            case 0:
                questionSet.clear();
                while (questionSet.size() < MAX_ANSWER){
                    int randomItem = 1+ random.nextInt(arrItem.size() - 1);
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
                while (questionSet.size() < MAX_ANSWER){
                    int randomItem = 1+ random.nextInt(arrItem.size() - 1);
                    if (arrItem.get(randomItem).getE_type().equals("Sinh")){
                        questionSet.add(arrItem.get(randomItem));
                    }
                }
                answerPosition = random.nextInt(MAX_ANSWER);
                question = "Ngày sinh của " + questionSet.get(answerPosition).getE_info();
                for (int i=0; i<MAX_ANSWER; i++){
                    answer[i] = questionSet.get(i).getE_day() + "/" + questionSet.get(i).getE_month() + "/" + questionSet.get(i).getE_year();
                }
                break;
            case 2:
                questionSet.clear();
                while (questionSet.size() < MAX_ANSWER){
                    int randomItem = 1+ random.nextInt(arrItem.size()-1);
                    if (arrItem.get(randomItem).getE_type().equals("Mất")){
                        questionSet.add(arrItem.get(randomItem));
                    }
                }
                answerPosition = random.nextInt(MAX_ANSWER);
                question = "Ngày mất của " + questionSet.get(answerPosition).getE_info();
                for (int i=0; i<MAX_ANSWER; i++){
                    answer[i] = questionSet.get(i).getE_day() + "/" + questionSet.get(i).getE_month() + "/" + questionSet.get(i).getE_year();
                }
                break;
            case 3:
                questionSet.clear();
                while (questionSet.size() < MAX_ANSWER){
                    int randomItem = 1+ random.nextInt(arrItem.size()-1);
                    if (arrItem.get(randomItem).getE_type().equals("Sự kiện")){
                        questionSet.add(arrItem.get(randomItem));
                    }
                }
                answerPosition = random.nextInt(MAX_ANSWER);
                question = questionSet.get(answerPosition).getE_day() + "/" + questionSet.get(answerPosition).getE_month() + "/" + questionSet.get(answerPosition).getE_year() + " diễn ra sự kiện nào?";
                for (int i=0; i<MAX_ANSWER; i++){
                    answer[i] = questionSet.get(i).getE_info();
                }
                break;
            case 4:
                questionSet.clear();
                while (questionSet.size() < MAX_ANSWER){
                    int randomItem = 1+ random.nextInt(arrItem.size()-1);
                    if (arrItem.get(randomItem).getE_type().equals("Sinh")){
                        questionSet.add(arrItem.get(randomItem));
                    }
                }
                answerPosition = random.nextInt(MAX_ANSWER);
                question = questionSet.get(answerPosition).getE_day() + "/" + questionSet.get(answerPosition).getE_month() + "/" + questionSet.get(answerPosition).getE_year() + " là ngày sinh của ai?";
                for (int i=0; i<MAX_ANSWER; i++){
                    answer[i] = questionSet.get(i).getE_info();
                }
                break;
            case 5:
                questionSet.clear();
                while (questionSet.size() < MAX_ANSWER){
                    int randomItem = 1+ random.nextInt(arrItem.size()-1);
                    if (arrItem.get(randomItem).getE_type().equals("Mất")){
                        questionSet.add(arrItem.get(randomItem));
                    }
                }
                answerPosition = random.nextInt(MAX_ANSWER);
                question = questionSet.get(answerPosition).getE_day() + "/" + questionSet.get(answerPosition).getE_month() + "/" + questionSet.get(answerPosition).getE_year() + " là ngày mất của ai?";
                for (int i=0; i<MAX_ANSWER; i++){
                    answer[i] = questionSet.get(i).getE_info();
                }
                break;
        }
        quiz = new Quiz(question, answer, answerPosition);

    }

    private void setClickable(boolean b) {
        tvAnswer1.setClickable(b);
        tvAnswer2.setClickable(b);
        tvAnswer3.setClickable(b);
        tvAnswer4.setClickable(b);
    }

    private void blink(final int position){
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                AnimationSet blink = (AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.blink);
//                switch (position){
//                    case ANSWER_1:
//                        tvAnswer1.startAnimation(blink);
//                        break;
//                    case ANSWER_2:
//                        tvAnswer2.startAnimation(blink);
//                        break;
//                    case ANSWER_3:
//                        tvAnswer3.startAnimation(blink);
//                        break;
//                    case ANSWER_4:
//                        tvAnswer4.startAnimation(blink);
//                        break;
//                }
//            }
//        }, 400);
        AnimationSet blink = (AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        switch (position){
            case ANSWER_1:
                tvAnswer1.startAnimation(blink);
                break;
            case ANSWER_2:
                tvAnswer2.startAnimation(blink);
                break;
            case ANSWER_3:
                tvAnswer3.startAnimation(blink);
                break;
            case ANSWER_4:
                tvAnswer4.startAnimation(blink);
                break;
        }
    }

    private void reset() {
        tvQuestion.setText(quiz.getQuestion());

        tvAnswer1.setBackgroundResource(R.drawable.bg_answer);
        tvAnswer2.setBackgroundResource(R.drawable.bg_answer);
        tvAnswer3.setBackgroundResource(R.drawable.bg_answer);
        tvAnswer4.setBackgroundResource(R.drawable.bg_answer);

        tvAnswer1.setText(quiz.getAnswer()[ANSWER_1]);
        tvAnswer2.setText(quiz.getAnswer()[ANSWER_2]);
        tvAnswer3.setText(quiz.getAnswer()[ANSWER_3]);
        tvAnswer4.setText(quiz.getAnswer()[ANSWER_4]);
    }

    public static void showDialogContent(Context context, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        builder.setMessage(content);
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        setClickable(false);
        Handler handler = new Handler();
        switch (v.getId()) {

            case R.id.tvAnswer1:
                tvAnswer1.setBackgroundResource(R.drawable.bg_answer_selected);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (quiz.getRightAnser() == ANSWER_1) {
                            currentScore++;
                        } else {
                            tvAnswer1.setBackgroundResource(R.drawable.bg_wrong_answer);
                            currentScore = 0;
                        }
                        tvCurrentScore.setText(currentScore + "");
                    }
                }, DELAY_SELECT_ANSWER);
                break;
            case R.id.tvAnswer2:
                tvAnswer2.setBackgroundResource(R.drawable.bg_answer_selected);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (quiz.getRightAnser() == ANSWER_2) {
                            currentScore++;
                        } else {
                            tvAnswer2.setBackgroundResource(R.drawable.bg_wrong_answer);
                            currentScore = 0;
                        }
                        tvCurrentScore.setText(currentScore + "");
                    }
                }, DELAY_SELECT_ANSWER);
                break;
            case R.id.tvAnswer3:
                tvAnswer3.setBackgroundResource(R.drawable.bg_answer_selected);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (quiz.getRightAnser() == ANSWER_3) {
                            currentScore++;
                        } else {
                            tvAnswer3.setBackgroundResource(R.drawable.bg_wrong_answer);
                            currentScore = 0;
                        }
                        tvCurrentScore.setText(currentScore + "");
                    }
                }, DELAY_SELECT_ANSWER);
                break;
            case R.id.tvAnswer4:
                tvAnswer4.setBackgroundResource(R.drawable.bg_answer_selected);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (quiz.getRightAnser() == ANSWER_4) {
                            currentScore++;
                        } else {
                            tvAnswer4.setBackgroundResource(R.drawable.bg_wrong_answer);
                            currentScore = 0;
                        }
                        tvCurrentScore.setText(currentScore + "");
                        if (currentScore > highScore) {
                            highScore = currentScore;
                            tvHighScore.setText(highScore + "");
                        }
                    }
                }, DELAY_SELECT_ANSWER);
                break;
        }


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentScore > highScore) {
                    highScore = currentScore;
                    tvHighScore.setText(highScore + "");

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(HIGH_SCORE, highScore+"");
                    editor.commit();
                }
                switch (quiz.getRightAnser()) {
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
        }, DELAY_SELECT_ANSWER);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setClickable(true);
                initData();
                reset();
            }
        }, DELAY_CHANGE_QUESTION);

    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.tvAnswer1:
                showDialogContent(getContext(), tvAnswer1.getText().toString());
                break;
            case R.id.tvAnswer2:
                showDialogContent(getContext(), tvAnswer1.getText().toString());
                break;
            case R.id.tvAnswer3:
                showDialogContent(getContext(), tvAnswer1.getText().toString());
                break;
            case R.id.tvAnswer4:
                showDialogContent(getContext(), tvAnswer1.getText().toString());
                break;
        }
        return true;
    }
}
