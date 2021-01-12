package com.example.androidwasserwaage;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager sensorManager;
    private Sensor rotationVectorSensor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        TextView x = findViewById(R.id.x);
        TextView y = findViewById(R.id.y);
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        Float xFloat = Float.valueOf(twoDForm.format(event.values[0]));
        Float yFloat = Float.valueOf(twoDForm.format(event.values[1]));
        if (xFloat == 0.00 && yFloat == 0.00 || xFloat == -0.00 && yFloat == -0.00) {
            View view = this.getWindow().getDecorView();
            view.setBackgroundColor(Color.GREEN);
            // vibration

        } else {
            View view = this.getWindow().getDecorView();
            view.setBackgroundColor(Color.WHITE);
        }
        x.setText(String.valueOf(xFloat));
        y.setText(String.valueOf(yFloat));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}