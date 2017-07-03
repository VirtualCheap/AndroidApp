package tk.virtualcheap.virtualcheapclient;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
    public String IP = "192.168.10.130"; //Change to your linux socket server
    public int PORT = 5000; //Change to port running on your linux socket server
    TCP_Client mTcpClient;
    TextView response;
    SensorManager mSensorManager;
    Sensor mGyro;
    SensorEventListener listener;

    void tcpOnReceive(String message){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        //UDP_Client UDP_socket = new UDP_Client (IP, PORT);
        //UDP_socket.connect();
        new ConnectTask().execute();
        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float axisX = sensorEvent.values[0];
                float axisY = sensorEvent.values[1];
                float axisZ = sensorEvent.values[2];
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(String.valueOf(axisX)+","+String.valueOf(axisY)+","+String.valueOf(axisZ));
                }
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
        if (mTcpClient != null) {
            mTcpClient.stopClient();
        }
    }

    public class ConnectTask extends AsyncTask<Void, String, TCP_Client> {

        @Override
        protected TCP_Client doInBackground(Void... message) {

            //we create a TCPClient object
            mTcpClient = new TCP_Client(new TCP_Client.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            }, IP, PORT);
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }
    }
}

