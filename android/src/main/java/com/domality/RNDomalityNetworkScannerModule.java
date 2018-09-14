
package com.domality;

import com.domality.networking.Device;
import com.domality.networking.OnEventListener;
import com.domality.networking.Scanner;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.json.JSONObject;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.io.UnsupportedEncodingException;


public class RNDomalityNetworkScannerModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public static final String EVENT_START = "RNDomStart";
    public static final String EVENT_STOP = "RNDomStop";
    public static final String EVENT_ERROR = "RNDomError";
    public static final String EVENT_RESOLVED = "RNDomResolved";
    public static final String EVENT_UPDATED_DEVICE = "RNDomUpdated";


    public RNDomalityNetworkScannerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNDomalityNetworkScanner";
    }

    @ReactMethod
    public void scan() {
        sendEvent(getReactApplicationContext(), EVENT_START, null);
        Scanner scanner = new Scanner(getReactApplicationContext(), new OnEventListener() {
            @Override
            public void onSuccess(ArrayList<Device> devices) {
                WritableArray devicesArray = new WritableNativeArray();
                for (Device device : devices) {
                    WritableMap deviceMap = new WritableNativeMap();
                    deviceMap.putString("device", device.toString());
                    devicesArray.pushMap(deviceMap);
                }
                sendEvent(getReactApplicationContext(), EVENT_RESOLVED, devicesArray);
            }

            @Override
            public void onDeviceNameIsResolved(Device device) {
                WritableMap deviceMap = new WritableNativeMap();
                deviceMap.putString("device", device.toString());
                sendEvent(getReactApplicationContext(), EVENT_UPDATED_DEVICE, deviceMap);
            }

            @Override
            public void onFailure(Exception e) {
                sendEvent(getReactApplicationContext(), EVENT_ERROR, e);
            }
        });
        scanner.execute();
    }

    protected void sendEvent(ReactContext reactContext,
                             String eventName,
                             @Nullable Object params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

}