package bresiu.sensordemo;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mMagneticFieldSensor;
    private Sensor mGyroscopeSensor;
    private Sensor mAccelerometerSensor;

    private int mCounter = 0;
    private long mLastTimestamp = 0;
    private boolean mHasAcc = false;
    private boolean mHasGyro = false;
    private boolean mHasMagn = false;

    private TextView mCounterTextView;

    private List<SingleData> array;
    private SingleData mSingleData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        getSensors();
    }

    private void initViews() {
        mCounterTextView = (TextView) findViewById(R.id.counter);
    }

    public void getSensors() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mMagneticFieldSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mMagneticFieldSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mGyroscopeSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mAccelerometerSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mSingleData == null) {
            mSingleData = new SingleData();
        }
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                mSingleData.setAccX(event.values[0]);
                mSingleData.setAccY(event.values[1]);
                mSingleData.setAccZ(event.values[2]);
                mHasAcc = true;
                break;
            case Sensor.TYPE_GYROSCOPE:
                mSingleData.setGyroX(event.values[0]);
                mSingleData.setGyroY(event.values[1]);
                mSingleData.setGyroZ(event.values[2]);
                mHasGyro = true;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mSingleData.setMagnX(event.values[0]);
                mSingleData.setMagnY(event.values[1]);
                mSingleData.setMagnZ(event.values[2]);
                mHasMagn = true;
                break;
        }
        if (mHasAcc && mHasGyro && mHasMagn) {
            mSingleData.setGeneration(mCounter);
            if (mLastTimestamp != 0) {
                mSingleData.setTimestamp(event.timestamp - mLastTimestamp);
            } else {
                mSingleData.setTimestamp(0);
                array = new ArrayList<SingleData>();
            }
            array.add(mSingleData);
            mCounter++;
            mCounterTextView.setText(mCounter + "");
            if (mCounter == 2000) {
                Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(500);
                mSensorManager.unregisterListener(this);
                exportData(array);
            }
            mLastTimestamp = event.timestamp;
            mHasAcc = false;
            mHasGyro = false;
            mHasMagn = false;

            mSingleData = null;
        }
    }

    private void exportData(List<SingleData> arrayList) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        String fname = "log.dat";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            for (SingleData singleData : arrayList) {
                pw.println(singleData.toString());
            }
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
