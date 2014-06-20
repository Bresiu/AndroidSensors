package bresiu.sensordemo;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import bresiu.sensordemo.logs.Lo;


public class Main extends Activity implements SensorEventListener, LocationListener {

    private SensorManager mSensorManager;
    private LocationManager mLocationManager;
    private Sensor mPressureSensor;
    private Sensor mMagneticFieldSensor;
    private Sensor mGyroscopeSensor;
    private Sensor mAccelerometerSensor;
    private String mProvider = "NONE";

    private TextView mPressureValue;
    private TextView mMagneticFieldValue;
    private TextView mGyroscopeValue;
    private TextView mAccelerometerValue;
    private TextView mGpsValue;
    private TextView mGpsProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        getSensors();
    }

    private void initViews() {
        mPressureValue = (TextView) findViewById(R.id.pressure_value);
        mMagneticFieldValue = (TextView) findViewById(R.id.magnetic_field_value);
        mGyroscopeValue = (TextView) findViewById(R.id.gyroscope_value);
        mAccelerometerValue = (TextView) findViewById(R.id.accelerometer_value);
        mGpsValue = (TextView) findViewById(R.id.gps_value);
        mGpsProvider = (TextView) findViewById(R.id.gps_provider);
    }

    public void getSensors() {

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        mProvider = mLocationManager.getBestProvider(criteria, false);
        mGpsProvider.setText(mProvider);
        onLocationChanged(mLocationManager.getLastKnownLocation(mProvider));

        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        for (Sensor sensor : deviceSensors) {
            Lo.g("sensor name: " + sensor.getName());
            Lo.g("vendor: " + sensor.getVendor());
        }

        mPressureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mMagneticFieldSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mPressureSensor, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mMagneticFieldSensor, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mGyroscopeSensor, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        mLocationManager.requestLocationUpdates(mProvider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_PRESSURE:
                mPressureValue.setText("" + event.values[0]);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagneticFieldValue.setText(
                        "x: " + event.values[0] +
                                "\ny: " + event.values[1] + "" +
                                "\nz: " + event.values[2]
                );
                break;
            case Sensor.TYPE_GYROSCOPE:
                mGyroscopeValue.setText(
                        "x: " + event.values[0] +
                                "\ny: " + event.values[1] + "" +
                                "\nz: " + event.values[2]
                );
                break;
            case Sensor.TYPE_ACCELEROMETER:
                mAccelerometerValue.setText(
                        "x: " + event.values[0] +
                                "\ny: " + event.values[1] + "" +
                                "\nz: " + event.values[2]
                );
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        float speed = location.getSpeed();
        String provider = location.getProvider();


        mGpsValue.setText("Latitude: " + latitude + "\nLongitude: " + longitude + "\nSpeed: " +
                speed);
        Gson gson = new Gson();
        String json = gson.toJson(new Json(longitude, latitude, speed, provider));
        Lo.g(json);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        mGpsProvider.setText(provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}
