package com.domality.networking;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Device {

    private String ip;
    private String mac;
    private String vendor = "Generic device";
    private String apiEndpoint = "https://macvendors.co/api/";
    private EventListener listener;

    public Device(String ip, String mac) {
        this.ip = ip;
        this.mac = mac;
        this.listener = null;
        this.getVendor();
    }

    public Device(String ip, String mac, String vendor) {
        this.ip = ip;
        this.mac = mac;
        this.listener = null;
        this.vendor = vendor;
        this.getVendor();
    }

    public interface EventListener {
        void onDeviceIsResolved(Device device);
    }

    public void setListener(EventListener listener) {
        this.listener = listener;
    }

    /**
     * This method try to get the vendor from the MacVendor API
     * https://macvendors.co/api/08:74:02:00:00:00/json
     *
     * @return Vendor name
     */
    private String getVendor() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(apiEndpoint.concat(mac).concat("/json"));
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    JSONObject RootJSON = new JSONObject(readStream(in));
                    JSONObject CompanyObject = new JSONObject(RootJSON.get("result").toString());
                    updateVendor(CompanyObject.get("company").toString() + " device");
                    if (listener != null)
                        listener.onDeviceIsResolved(new Device(ip, mac, vendor));

                } catch (MalformedURLException exception) {

                } catch (IOException exception) {

                } catch (JSONException exception) {

                }
            }
        });
        return vendor;
    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    private void updateVendor(String vendor) {
        this.vendor = vendor;
    }

    public String toString() {
        return this.toJson();
    }

    public String toJson() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("VENDOR", this.vendor);
            jsonObj.put("IP", this.ip);
            jsonObj.put("MAC", this.mac);
        } catch (JSONException e) {
            return "";
        }
        return jsonObj.toString();
    }

}
