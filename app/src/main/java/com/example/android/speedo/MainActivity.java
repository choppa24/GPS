package com.example.android.speedo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.location.Location;
import android.location.LocationManager;

import android.location.LocationListener;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private double cDistance;
    private TextView distance;
    private float beforeSpeed = 0;
    private TextView resultTextView;

    private XYZAccelerometer xyzAcc;
    private SensorManager mSensorManager;
    private static final long UPDATE_INTERVAL = 500;
    private static final long MEASURE_TIMES = 20;
    private String result = "";
    private TextView speed;
    private Button startButton;
    private EditText dx;
    private EditText dy;
    private EditText dz;

    int TAG_CODE_PERMISSION_LOCATION;
    private LocationManager locationManager;
    private  TextView tvEnabledGPS ;
    private  TextView tvLocationGPS ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION },
                TAG_CODE_PERMISSION_LOCATION);

        distance = (TextView)findViewById(R.id.distance);
                speed = (TextView)findViewById(R.id.speed);
        resultTextView = (TextView)findViewById(R.id.result);

        speed = (TextView) findViewById(R.id.speed);
        startButton = (Button) findViewById(R.id.start);
        speed.setText("...");
        distance.setText("...");
        dx = (EditText)findViewById((R.id.dx));
        dy = (EditText)findViewById((R.id.dy));
        dz = (EditText)findViewById((R.id.dz));
        tvEnabledGPS = (TextView) findViewById(R.id.tvEnabledGPS);
        tvLocationGPS = (TextView) findViewById(R.id.tvLocationGPS);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        xyzAcc = new XYZAccelerometer();
        mSensorManager.registerListener(xyzAcc,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkEnabled();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    // вызывается после нажатия на start
    public void startButton(View v) {
        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    500, 0, locationListener);
        }
        catch (SecurityException e){
            System.out.println("Нет Доступа");
        }
        cDistance = 0;
        xyzAcc.setdX(Float.valueOf(dx.getText().toString()));
        xyzAcc.setdY(Float.valueOf(dy.getText().toString()));
        xyzAcc.setdZ(Float.valueOf(dz.getText().toString()));
        disableButtons();
    }

    // метод блокирует кнопку start
    private void disableButtons() {
        startButton.setEnabled(false); // блокировка кнопки
    }

    private void enableButtons() {
        startButton.setEnabled(true); //разблокировка кнопки
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            formatLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }

        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            try{
                formatLocation(locationManager.getLastKnownLocation(provider));
            }
            catch (SecurityException e){
                System.out.println("Нет Доступа");
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private void formatLocation(Location location) {
        if (location != null) {
            result = " ax, ay, az: " + xyzAcc.getAcc() +"\n";
            Point point = xyzAcc.getPoint();
            MeasurePoint measurePoint = new MeasurePoint(point.getX(), point.getY(), point.getZ(),
                    0, 500,0);
            double deltaSpeed = (Math.round(measurePoint.getSpeedAfter()* 3.6f * 100) / 100f);
            double deltaDistance = (Math.round(measurePoint.getDistance() * 100) / 100f);
            speed.setText(String.format("%1$.3f", location.getSpeed()*3.6f + deltaSpeed));
            cDistance += (location.getSpeed()*3.6f) *0.5 + deltaDistance;
            distance.setText(String.format("%1$.3f", cDistance));
            tvLocationGPS.setText(String.format(
                    "Координаты : широта = %1$.6f, долгота = %2$.6f, время = %3$tF %3$tT",
                    location.getLatitude(), location.getLongitude(), new Date(
                            location.getTime())));

            resultTextView.setText(result);
        }
    }

    private void checkEnabled() {
        tvEnabledGPS.setText("Доступность: "
                + locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

    public void stopButton(View view){
        locationManager.removeUpdates(locationListener);
        enableButtons();
        dx.setText("0");
        dy.setText("0");
        dz.setText("0");
        result = "";
        cDistance = 0;
        speed.setText("0.000");
        distance.setText("0.000");
    }

}
