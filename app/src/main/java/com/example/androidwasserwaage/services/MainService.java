package com.example.androidwasserwaage.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.androidwasserwaage.R;
import com.example.androidwasserwaage.SpiritLevel;

import java.text.DecimalFormat;

public class MainService extends Service {
    private final MainBinder mainBinder = new MainBinder();
    private SpiritLevel spiritLevel = new SpiritLevel();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mainBinder;
    }

    public class MainBinder extends Binder {
        public MainService getMainService() {
            return MainService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MainService", "Received start id " + startId + ": " + intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, R.string.main_service_stopped, Toast.LENGTH_SHORT).show();
    }

    public void setSpiritLevel(float x, float y) {
        if(spiritLevel == null) return;
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        spiritLevel.setX(Float.parseFloat(twoDForm.format(x)));
        spiritLevel.setY(Float.parseFloat(twoDForm.format(y)));
    }

    public float getX() {
        return spiritLevel.getX();
    }

    public float getY() {
        return spiritLevel.getY();
    }
}
