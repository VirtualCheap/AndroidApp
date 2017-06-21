package tk.virtualcheap.virtualcheapclient;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
    String IP = "192.168.0.108"; //Change to your linux socket server
    int PORT = 50000; //Change to port running on your linux socket server
    TextView response;
    SensorManager mSensorManager;
    Sensor mGyro;
    SensorEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float axisX = sensorEvent.values[0];
                float axisY = sensorEvent.values[1];
                float axisZ = sensorEvent.values[2];
                Client myClient = new Client(IP, PORT, response, axisX, axisY, axisZ);
                myClient.execute();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };


        final boolean senslist = mSensorManager.registerListener(listener, mGyro, 10);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(listener);
    }
}
