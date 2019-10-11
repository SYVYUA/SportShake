package com.example.sportshake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ShakeBaby";

    //чуствительность датчика
    private static final int SHAKE_SENSITIVITY = 1;


    private TextView startRun, finishRun, calmPosition;

    private SensorManager sensorManager;

    private float accel = SensorManager.GRAVITY_EARTH;
    //положение телефона в пространсте до встряхивания
    private float accelPrevious = SensorManager.GRAVITY_EARTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startRun = findViewById(R.id.startRun);
        finishRun = findViewById(R.id.finishRun);
        calmPosition = findViewById(R.id.calmPosition);

        //получаем доступ к сенсорам
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(
                sensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    //получаем обновлённые данные
    //SensorManager.SENSOR_DELAY_GAME - частота обновлений показаний датчика
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(
                sensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }

    // остановливаем получение данных
    @Override
    protected void onStop() {
        sensorManager.unregisterListener(sensorListener);
        super.onStop();
    }

    protected void onShake(){
        Log.d(TAG, "SHAKE");
        startRun.setVisibility(View.VISIBLE);
    }



    /* слушатель для событий перемещения устройства в пространстве*/
    private final SensorEventListener sensorListener = new SensorEventListener() {

        //вызиваем эту службу каждый раз при изменении значений
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            accelPrevious = accel;
            accel = (float) Math.sqrt((double) (x * x + y * y + z * z));
            if (accel - accelPrevious > SHAKE_SENSITIVITY) {
                onShake();
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    /* *******************************/
}
