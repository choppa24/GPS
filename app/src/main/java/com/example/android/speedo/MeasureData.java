package com.example.android.speedo;

import android.widget.TextView;

import java.util.LinkedList;

//класс для хранения информации обо всем эксперименте

public class MeasureData {
    // точки от акселерометра
    private LinkedList accData;
    private LinkedList data;
    private TextView  checkAcc;

    // таймер интервала генерации точек
    private long interval;

    public MeasureData(long interval) {
        this.interval = interval;
        accData = new LinkedList ();
        data = new LinkedList ();
    }

    public void addPoint(Point p){
        accData.add(p);
    }

    public void process(){
        for(int i = 0; i < accData.size(); ++i){
            Point p = (Point)accData.get(i);
            float speed = 0;
            float distance = 0;
            if(i > 0){
                MeasurePoint mp = (MeasurePoint) data.get(i-1);
                speed = mp.getSpeedAfter();
                distance = mp.getDistance();
            }
            data.add(new MeasurePoint(p.getX(), p.getY(), p.getZ(), speed, interval,distance));
        }
    }

    String format(float k1, float k2, float k3) {
        return String.format("%1$.3f\t\t%2$.3f\t\t%3$.3f", k1, k2,
                k3);
    }

    public float getLastSpeed(){
        MeasurePoint mp = (MeasurePoint) data.getLast();
        return mp.getSpeedAfter();
    }

    public String getAcc(){
        Point k = (Point)accData.get(accData.size()-1);
        return (""+ format(k.getX(), k.getY(), k.getZ()));
    }


    public float getSpeedBeforeKm(){
        MeasurePoint mp = (MeasurePoint) data.getLast();
        return mp.getSpeedBefore();
    }

    public float getLastSpeedKm(){
        float ms = getLastSpeed();
        return ms*3.6f;
    }
    public float getDistanceM(){
        MeasurePoint mp = (MeasurePoint) data.getLast();
        return mp.getDistance();
    }
}
