package com.example.androidwasserwaage;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import android.os.Vibrator;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager sensorManager;
    private Sensor rotationVectorSensor;
    Vibrator vibrate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        TextView x = findViewById(R.id.x);
        TextView y = findViewById(R.id.y);
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        Float xFloat = Float.valueOf(twoDForm.format(event.values[0]));
        //Math.toDegrees(xFloat);
        Float yFloat = Float.valueOf(twoDForm.format(event.values[1]));
        //Math.toDegrees(yFloat);
        //convertToDegrees(xFloat);
        if (xFloat == 0.00 && yFloat == 0.00 || xFloat == -0.00 && yFloat == -0.00) {
            View view = this.getWindow().getDecorView();
            view.setBackgroundColor(Color.GREEN);
            // notification
            Toast.makeText(this, "The surface is even!", Toast.LENGTH_LONG).show();
            // vibration
            vibrate();

        } else {
            View view = this.getWindow().getDecorView();
            view.setBackgroundColor(Color.WHITE);
        }
        x.setText(String.valueOf(xFloat));
        y.setText(String.valueOf(yFloat));
    }

    @Override
    public void onPause() {
        super.onPause();
        vibrate.cancel();  // cancel for example here
    }

    @Override
    public void onStop() {
        super.onStop();
        vibrate.cancel();   // or cancel here
    }

    public void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, VibrationEffect.EFFECT_TICK));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
        }
    }

    private void convertToDegrees(float[] vector){
        for (int i = 0; i < vector.length; i++){
            vector[i] = Math.round(Math.toDegrees(vector[i]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}