package com.domality.networking;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import android.content.Context;

public class Scanner extends AsyncTask<Void, Void, ArrayList> {

    private OnEventListener mCallBack;
    private Context mContext;
    public Exception mException;

    public Scanner(Context context, OnEventListener callback) {
        mCallBack = callback;
        mContext = context;
    }

    @Override
    protected ArrayList doInBackground(Void... params) {

        try {

            return this.readArp();

        } catch (Exception e) {

            mException = e;

        }

        return null;
    }

    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = inetAddress.getHostAddress();
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    private ArrayList<Device> readArp() {

        BufferedReader bufferedReader = null;
        final ArrayList Devices = new ArrayList<Device>();

        try {
            bufferedReader = new BufferedReader(new FileReader("/proc/net/arp"));

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4) {
                    String ip = splitted[0];
                    String mac = splitted[3];
                    if (mac.matches("..:..:..:..:..:..")) {
                        Device device = new Device(ip, mac);
                        device.setListener(new Device.EventListener() {
                            @Override
                            public void onDeviceIsResolved(Device device) {
                                mCallBack.onDeviceNameIsResolved(device);
                                mCallBack.onSuccess(Devices);
                            }
                        });
                        Devices.add(device);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Devices;
    }

    @Override
    protected void onPostExecute(ArrayList devices) {
        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess(devices);
            } else {
                mCallBack.onFailure(mException);
            }
        }
    }

    private void manuallyPingDevices() {
        Vector<String> Available_Devices = new Vector<>();

        String myip = this.getLocalIpAddress();

        System.out.println(myip);

        String mynetworkips = new String();

        for (int i = myip.length(); i > 0; --i) {
            if (myip.charAt(i - 1) == '.') {
                mynetworkips = myip.substring(0, i);
                break;
            }
        }

        System.out.println("My Device IP: " + myip + "\n");

        System.out.println("Search log:");
        for (int i = 1; i <= 2; ++i) {
            try {
                InetAddress addr = InetAddress.getByName(mynetworkips + new Integer(i).toString());
                if (addr.isReachable(1000)) {
                    System.out.println("IP: " + addr.getHostAddress());
                    System.out.println("Name: " + addr.getHostName());
                    Available_Devices.add(addr.getHostAddress());
                } else System.out.println("Not available: " + addr.getHostAddress());

            } catch (IOException ioex) {
            }
        }

        System.out.println("\nAll Connected devices(" + Available_Devices.size() + "):");
        for (int i = 0; i < Available_Devices.size(); ++i)
            System.out.println(Available_Devices.get(i));
    }

}