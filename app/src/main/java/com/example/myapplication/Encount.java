package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Encount extends AppCompatActivity {

    ImageView pokeView;
    ImageView ballView;
    ImageView ball2View;
    TextView textView;
    TextView numView;
    Button returnButton;
    Button aButton;
    Button CButton;

    int state = 0;
    int numOfClick = 0;
    int pokeId;
    double start;
    double end;

    String data[][]  = {
            {"アシマリ", "20"},
            {"ピカチュウ","10"},
            {"ヤドン", "10"},
            {"ラプラス", "35"},
            {"ロコン", "10"},
            {"ブースター", "30"},
            {"イーブイ", "10"},
            {"モクロー", "20"},
            {"ニャビー", "20"},
            {"コイキング", "0"},
            {"プリン", "10"},
            {"ヒトカゲ", "20"},
            {"フシギダネ", "20"},
            {"ゼニガメ", "20"},
            {"サンダース", "30"},
            {"シャワーズ", "30"},
            {"カビゴン", "50"},
            {"ニャース", "10"},
            {"カイリュー", "50"},
            {"ミュウ", "80"}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encount);

        Intent intent = getIntent();
        int id = intent.getIntExtra(MainActivity.EXTRA_MESSAGE, -1);
        pokeId = id;

        String filename = "poke" + id;
        int picId = getResources().getIdentifier(filename, "drawable", getPackageName());

        textView = (TextView)findViewById(R.id.text);
        ballView = (ImageView)findViewById(R.id.ball);
        ball2View = (ImageView)findViewById(R.id.ball2);
        returnButton = (Button)findViewById(R.id.left);
        aButton = (Button)findViewById(R.id.right);
        CButton = (Button)findViewById(R.id.confirm);

        //numView = (TextView)findViewById(R.id.num);

        pokeView = (ImageView) findViewById(R.id.pokemon);
        pokeView.setImageResource(picId);


        textView.setText(data[id][0] + "があらわれた！ \n ボールをタップして捕まえろ！");

    }

    public void throwBall(float fd, float td, int tt){

        float fromDegrees = fd;//0
        float toDegrees = td;//20
        int pivotXType = 2;
        int pivotYType = 0;
        float pivotXValue = 2;//2
        float pivotYValue = 1;//1
        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotXType, pivotXValue, pivotYType, pivotYValue);
        animation.setDuration(tt);//800
        animation.setFillAfter(true);
        ballView.startAnimation(animation);
        //ballView.setVisibility(View.INVISIBLE);

    }

    public void enterBall(int tf, int dt){
        AnimationSet animationSet = new AnimationSet(true);

        float from = 1 - tf;
        float to = 0 +  tf;
        AlphaAnimation animation = new AlphaAnimation(from, to);

        float fromX = 1 - tf;
        float toX = 0 + tf;
        float fromY = 1 - tf;
        float toY = 0 + tf;
        int pivotXType = Animation.ABSOLUTE;
        int pivotYType = Animation.ABSOLUTE;
        float pivotXValue = 500;
        float pivotYValue = 400;
        ScaleAnimation animation1 = new ScaleAnimation(fromX, toX, fromY, toY, pivotXType, pivotXValue, pivotYType, pivotYValue);

        animationSet.addAnimation(animation);
        animationSet.addAnimation(animation1);
        animationSet.setDuration(1000);
        animationSet.setFillAfter(true);
        animationSet.setStartOffset(dt);
        pokeView.startAnimation(animationSet);


        /*float from = 1;
        float to = 0;
        AlphaAnimation animation = new AlphaAnimation(from, to);
        animation.setDuration(500);
        animation.getFillAfter();
        animation.setStartOffset(1000);
        pokeView.startAnimation(animation);
        pokeView.setVisibility(View.INVISIBLE);*/
    }

    public void downBall(View v){
        //ball2View.setVisibility(View.VISIBLE);
        int fromXType = 1;
        int toXType = 1;
        int fromYType = 1;
        int toYType = 1;
        float fromXValue = 0;
        float toXValue = 0;
        float fromYValue = -3;
        float toYValue = 0;
        TranslateAnimation animation = new TranslateAnimation(fromXType, fromXValue, toXType, toXValue, fromYType, fromYValue, toYType, toYValue);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        v.startAnimation(animation);
    }

    public void onBall(View v){
        if(state == 0){
            state = 1;
            returnButton.setVisibility(View.INVISIBLE);

            throwBall(0, 20, 800);
            enterBall(0, 1000);
            textView.setText("Aボタンを連打しろ！");
        }else{

        }


    }

    public void catchPhase(){
        textView.setText(pokeId + "を捕まえた！");
    }

    /*class Catch extends TimerTask{
        @Override
        public void run(){
            //catchPhase();
            textView.setText(pokeId + "を捕まえた！");
        }
    }*/

    public void onA(View v){
        if(state == 1){
            state = 2;
            numOfClick += 1;
            textView.setText("Aボタンを連打しろ！\n捕獲率：" + numOfClick + "%");

            //タイマー起動
            /*Timer timer = new Timer("myTimer");
            Catch catching = new Catch();
            timer.schedule(catching, 1000);*/

            start = System.currentTimeMillis();

        }else if(state == 2) {
            numOfClick += 2;
            textView.setText("Aボタンを連打しろ！\n捕獲率：" + numOfClick + "%");
            if(numOfClick >= 50){
                state = 3;
            }

        }else if(state == 3){
            numOfClick += 3;
            textView.setText("Aボタンを連打しろ！\n捕獲率：??%");
            if(numOfClick >= 100){
                state = 4;
            }

        }else if(state == 4){
            aButton.setVisibility(View.INVISIBLE);
            end = System.currentTimeMillis();
            textView.setText("結果は…");
            CButton.setVisibility(View.VISIBLE);
        }
    }

    public void onConfirm(View v){
        state = 5;
        int prob = 170 - (int)(end - start)/100 - Integer.parseInt(data[pokeId][1]); //捕獲率の設定
        //textView.setText("" + prob);

        Random rand = new Random();
        int catchTF = prob - rand.nextInt(100) - pokeId;

        if(catchTF >= 0){
            //ballView.setVisibility(View.INVISIBLE);
            CButton.setVisibility(View.INVISIBLE);
            downBall(ballView);
            textView.setText("おめでとう！\n" + data[pokeId][0] + "を捕まえたぞ！");
            returnButton.setVisibility(View.VISIBLE);
        }else{
            //ballView.setVisibility(View.INVISIBLE);
            throwBall(20, 60, 400);
            CButton.setVisibility(View.INVISIBLE);
            enterBall(1, 0);
            textView.setText("残念…\n" + data[pokeId][0] + "に逃げられてしまった");
            returnButton.setVisibility(View.VISIBLE);

        }

    }

    public void onReturn(View v){
        if(state == 0){
            finish();
        }else if(state == 5){
            finish();
        }
        /*Intent intent = new Intent(Encount.this, MainActivity.class);
        intent.putExtra(EXTRA_MESSAGE, 0);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/

    }




}
