package com.domality.networking;

import java.util.ArrayList;

public interface OnEventListener {
    public void onSuccess(ArrayList<Device> object);
    public void onDeviceNameIsResolved(Device object);
    public void onFailure(Exception e);
}
