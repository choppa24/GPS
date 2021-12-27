package com.example.android.speedo;

//класс для хранения и вычисления параметров движения на интервале

public class MeasurePoint {
    private final float x;
    private final float y;
    private final float z;
    private final float speedBefore;
    private float speedAfter;
    private float distance;
    private float acceleration;
    private final long interval;


    public MeasurePoint(float x, float y, float z, float speedBefore, long interval, float distance) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.speedBefore = speedBefore;
        this.interval = interval;
        this.distance = distance;
        speedAfter = 0;
        calc();
    }

    private void calc(){
        System.out.println(this.x + "   "+ this.y+ "    "+this.z);
        //вычисление ускорения
        acceleration = (float) Math.sqrt(Math.signum(x)* this.x * this.x +
                Math.signum(y) * this.y * this.y + Math.signum(z) * this.z * this.z);
        float t = ((float)interval / 1000f);
        //вычисление скорости
        speedAfter = speedBefore + acceleration * t; // v=v0+at;
        //вычисление расстояния
        distance += speedBefore*t + acceleration*t*t/2;// s=v0*t + a*t^2/2
    }

    public float getSpeedAfter(){
        return speedAfter;
    }

    public float getDistance(){
        return  distance;
    }

    public float getSpeedBefore(){ return speedBefore;}
}
