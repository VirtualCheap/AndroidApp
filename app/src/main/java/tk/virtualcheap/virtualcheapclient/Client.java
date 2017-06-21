package tk.virtualcheap.virtualcheapclient;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.LocaleList;
import android.util.Log;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
//TODO Read Gyroscope info
//TODO add network discovery of PC for automatic IP

public class Client extends AsyncTask<Object, Object, String> {

    String dstAddress;
    int dstPort;
    String response = "";
    TextView textResponse;
    float gyroX, gyroY, gyroZ;

    Client(String addr, int port, TextView textResponse, float X,float Y, float Z) {
        dstAddress = addr;
        dstPort = port;
        this.textResponse = textResponse;
        gyroX = X;
        gyroY = Y;
        gyroZ = Z;

    }

    @Override
    protected String doInBackground(Object... arg0) {
        Socket socket = null;
        try {
            socket = new Socket(dstAddress, dstPort);
            OutputStream outputStream;
            String msgReply = "X:"+  Float.toString(gyroX) + " Y:" + Float.toString(gyroY) + " Z:"+ Float.toString(gyroZ);
            outputStream = socket.getOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            printStream.print(msgReply);
            printStream.flush();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                    Log.d("Socket", "Closing");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        //textResponse.setText(response);
        super.onPostExecute(result);
    }

}