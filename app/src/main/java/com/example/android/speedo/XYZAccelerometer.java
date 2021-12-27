package com.example.android.speedo;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;

public class XYZAccelerometer extends Accelerometer{
    private static final int BUFFER_SIZE = 500;

    // калибровка
    private  float dX = 0;
    private  float dY = 0;
    private  float dZ = 0;
    // буферные переменные
    private float X;
    private float Y;
    private float Z;
    private int cnt = 0;


    // параметры, возвращаемые с использованием буфера: среднее ускорение
    // с момента последнего вызова getPoint ()
    public Point getPoint(){
        if (cnt == 0){
            return new Point(lastX, lastY, lastZ, 1);
        }
        Point p =  new Point(X, Y, Z, cnt);
        reset();
        return p;
    }

    String format(float k1, float k2, float k3) {
        return String.format("%1$.3f\t\t%2$.3f\t\t%3$.3f", k1, k2,
                k3);
    }

    public String getAcc(){
        if (cnt == 0){
            return (""+ format(lastX, lastY, lastZ));
        }
        Point k =  new Point(X, Y, Z, cnt);
        reset();
        return (""+ format(k.getX(), k.getY(), k.getZ()));
    }

    // сбрасывает буфер
    public void reset(){
        cnt = 0;
        X = 0;
        Y = 0;
        Z = 0;
    }


    public void onSensorChanged(SensorEvent se) {
        float x = se.values[SensorManager.DATA_X] + dX;
        float y = se.values[SensorManager.DATA_Y] + dY;
        float z = se.values[SensorManager.DATA_Z] + dZ;

        lastX = x;
        lastY = y;
        lastZ = z;

        X+= x;
        Y+= y;
        Z+= z;

        if (cnt < BUFFER_SIZE-1) {
            cnt++;
        } else
        {
            reset();
        }
    }

    public  void setdX(float dX) {
        this.dX = dX;
    }

    public  void setdY(float dY) {
        this.dY = dY;
    }

    public  void setdZ(float dZ) {
        this.dZ = dZ;
    }

}

