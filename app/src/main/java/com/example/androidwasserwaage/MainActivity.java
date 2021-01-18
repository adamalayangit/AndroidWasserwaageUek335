package com.example.androidwasserwaage;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidwasserwaage.services.MainService;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private MainService mainService = null;
    private boolean mainServiceIsBound = false;
    private ServiceConnection mainServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MainService.MainBinder mainBinder = ((MainService.MainBinder) service);
            mainService = mainBinder.getMainService();
            Toast.makeText(MainActivity.this, R.string.main_service_connected,
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mainService = null;
            mainServiceIsBound = false;
        }
    };

    private SensorManager sensorManager;
    private Sensor rotationVectorSensor;
    private Vibrator vibrate;

    private TextView xTextView;
    private TextView yTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadTextViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        doBindService();
        startSensor();
    }

    private void startSensor() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void loadTextViews() {
        xTextView = findViewById(R.id.x);
        yTextView = findViewById(R.id.y);
    }

    private void doBindService() {
        Intent bindServiceIntent = new Intent(MainActivity.this, MainService.class);
        if (bindService(bindServiceIntent, mainServiceConnection, Context.BIND_AUTO_CREATE)) {
            mainServiceIsBound = true;
        } else {
            Toast.makeText(MainActivity.this, R.string.failed_starting_main_service,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void doUnbindService() {
        if (mainServiceIsBound) {
            unbindService(mainServiceConnection);
            mainServiceIsBound = false;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mainService.setSpiritLevel(event.values[0], event.values[1]);
        float x = mainService.getX();
        float y = mainService.getY();
        if (x == 0.00 && y == 0.00 || x == -0.00 && y == -0.00) {
            View view = this.getWindow().getDecorView();
            view.setBackgroundColor(Color.GREEN);
            // notification
            Toast.makeText(this, "The surface is even!", Toast.LENGTH_SHORT).show();
            // vibration
            vibrate();
        } else {
            View view = this.getWindow().getDecorView();
            view.setBackgroundColor(Color.WHITE);
        }
        xTextView.setText(String.valueOf(x));
        yTextView.setText(String.valueOf(y));
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void vibrate() {
        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, VibrationEffect.EFFECT_TICK));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}