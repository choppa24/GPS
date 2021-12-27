package com.example.android.speedo;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;

//Для того, чтобы реагировать на изменение ускорения,
// необходимо реализовать где-нибудь интерфейс SensorEventListener.

public abstract class Accelerometer implements SensorEventListener {
    protected float lastX;
    protected float lastY;
    protected float lastZ;

    public abstract Point getPoint();
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }
}
