package tk.virtualcheap.virtualcheapclient;

import android.os.AsyncTask;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class UDP_Client {
    public boolean connected = false;
    private InetAddress serverAddr;
    private DatagramSocket ds = null;
    private String dstAddress;
    private int dstPort;
    private AsyncTask async_cient;

    UDP_Client(String ip, int port) {
        dstPort = port;
        dstAddress = ip;
    }
    void connect(){
        async_cient = new AsyncTask<Object, Boolean, Boolean>() {
            @Override
            protected Boolean doInBackground(Object... parms) {
                try {
                    ds = new DatagramSocket();
                    serverAddr = InetAddress.getByName(dstAddress);
                    String udpMsg = "connect";
                    DatagramPacket dp = new DatagramPacket(udpMsg.getBytes(), udpMsg.length(), serverAddr, dstPort);
                    ds.send(dp);
                    byte[] message = new byte[2048];
                    DatagramPacket DPRecv = new DatagramPacket(message, 2048);
                    ds.receive(DPRecv);
                    connected = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    connected = false;
                }
                connected = false;
                return connected;
            }

            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
            }
        };
        async_cient.execute();
    }
    void close(){
        if (ds != null) {
            ds.close();
        }
    }
}