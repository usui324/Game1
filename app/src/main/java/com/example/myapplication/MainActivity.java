package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;
import android.webkit.*;
import android.webkit.GeolocationPermissions.Callback;
import android.widget.Toast;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;

    private float acc[] = new float[3];
    private float gyro[] = new float[3];
    private float mag[] = new float[3];

    int step_flag = 0;
    int step_count = 0;

    float theta = 0;

    public static final String EXTRA_MESSAGE = "com.example.myapplication.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        WebView webView1 = findViewById(R.id.webView1);


        class MainJsInterface{
            final float FALSETHETA = -10000;

            @JavascriptInterface
            public float stepDetection(){
                double sum = Math.sqrt(acc[0]*acc[0]+acc[1]*acc[1]+acc[2]*acc[2]);
                theta += gyro[2]*50/1000 *180/Math.PI;
                if(theta > 180) theta -= 360;
                if(theta < -180) theta += 360;

                if(sum>11.5){
                    if(step_flag==2){
                        step_flag=1;
                        step_count++;

                        return theta;
                    }else if (step_flag==0){
                        step_flag=1;
                    }
                }else if(sum<8.5&&step_flag==1){
                    step_flag=2;
                }

                return FALSETHETA;
            }

            @JavascriptInterface
            public void ChangeActivity(int id){
                Intent intent = new Intent(MainActivity.this, Encount.class);
                intent.putExtra(EXTRA_MESSAGE, id);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @JavascriptInterface
            public int stepTeach(){
                return step_count;
            }

            /*@JavascriptInterface
            public void UpdateActivity(int id){
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra(EXTRA_MESSAGE, id);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }*/
        }

        //JavaScript有効化
        webView1.getSettings().setJavaScriptEnabled(true);

        //位置情報有効化
        webView1.getSettings().setGeolocationEnabled(true);
        webView1.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, Callback callback){
                callback.invoke(origin, true, false);
            }
        });

        //ここで指定した名前で呼び出すことができる
        webView1.addJavascriptInterface(new MainJsInterface(), "Android");

        //map_display.htmlをロード
        webView1.loadUrl("file:///android_asset/map_display.html");
    }



    @Override
    protected void onResume() {
        super.onResume();
        Sensor acc_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor gyro_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Sensor mag_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener((SensorEventListener)this, acc_sensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener((SensorEventListener)this, gyro_sensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener((SensorEventListener)this, mag_sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener((SensorEventListener)this);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                for(int i=0; i<3; i++) acc[i] = event.values[i];
                break;
            case Sensor.TYPE_GYROSCOPE:
                for(int i=0; i<3; i++) gyro[i] = event.values[i];
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                for(int i=0; i<3; i++) mag[i] = event.values[i];
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
