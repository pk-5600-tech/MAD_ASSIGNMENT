package com.example.sensor_sense;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer, light, proximity;
    private TextView accelerometerText, lightText, proximityText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Initialize TextViews first
        accelerometerText = findViewById(R.id.accmeter);
        lightText = findViewById(R.id.light);
        proximityText = findViewById(R.id.proximity);

        // 2. Initialize SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // 3. Initialize Sensors
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        // 4. Check if sensors exist on this specific device
        if (accelerometer == null) accelerometerText.setText("Accelerometer: Not Available");
        if (light == null) lightText.setText("Light: Not Available");
        if (proximity == null) proximityText.setText("Proximity: Not Available");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();

        if (type == Sensor.TYPE_ACCELEROMETER) {
            accelerometerText.setText("Accelerometer\nX: " + event.values[0] +
                    "\nY: " + event.values[1] +
                    "\nZ: " + event.values[2]);
        } else if (type == Sensor.TYPE_LIGHT) {
            lightText.setText("Light\nLux: " + event.values[0]);
        } else if (type == Sensor.TYPE_PROXIMITY) {
            String state = (event.values[0] < proximity.getMaximumRange()) ? "Near" : "Far";
            proximityText.setText("Proximity\nState: " + state + " (" + event.values[0] + ")");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    protected void onResume() {
        super.onResume();
        // Registering listeners
        if (accelerometer != null) sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        if (light != null) sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
        if (proximity != null) sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister to save battery
        sensorManager.unregisterListener(this);
    }
}

