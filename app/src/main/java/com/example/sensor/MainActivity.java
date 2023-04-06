package com.example.sensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.SensorEventListener;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.media.audiofx.AudioEffect;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accMeter;
    private Button startButton;
    private Button startAngle;
    private Button reset;
    private TextView textV;
    private boolean started = false;
    private boolean angle = false;
    private Button stopButton;
    private Vibrator v;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accMeter = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        player = MediaPlayer.create(MainActivity.this, R.raw.flowers);
        this.textV = (TextView) findViewById(R.id.Text);
        this.startButton = (Button) findViewById(R.id.start_button);
        this.stopButton = (Button) findViewById(R.id.stop_button);
        this.startAngle = (Button) findViewById(R.id.start_angle);
        this.reset = (Button) findViewById(R.id.reset_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //start the accMeter
                 started= true;
                 onResume();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //start the accMeter
                onPause();
                started = false;
                angle = false;
                player.pause();
            }
        });
        startAngle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //start the angle Detection
                angle = true;
                onResume();
                started = true;
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //start the angle Detection
                textV.setText("(X: 0, Y: 0, Z: 0)");
                onPause();
                player.reset();
                player = MediaPlayer.create(MainActivity.this, R.raw.flowers);
            }
        });
        onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() ==Sensor.TYPE_ACCELEROMETER) {
            if (started) {
                textV.setText("X: " + event.values[0] + " Y: " + event.values[1] + " Z: " + event.values[2]);
            }
        }
        if(event.values[1] <= 0 && event.values[0] > 0 && angle){
            if(player.isPlaying()){

            }else {
                player.start();
            }
        } if(event.values[1] <= 0 && event.values[0] < 0 && player.isPlaying()){

            player.pause();
        }
        if(event.values[2] >= 7 && angle){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(500);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accMeter, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

}