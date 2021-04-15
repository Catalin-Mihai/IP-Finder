package com.catasoft.ip_finder.ui.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

public class NetworkBroadcastReceiver extends BroadcastReceiver {

    private NetworkListener networkListener;

    public void setupListener(NetworkListener networkListener){
        this.networkListener = networkListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(networkListener != null) {
            Utilities.notGoodConnection(context, new Utilities.NetworkCallback() {
                @Override
                public void onSuccess() {
                    networkListener.onChange(true);
                }
                @Override
                public void onFailure(String message) {
                    networkListener.onChange(false);
                }
            });
        }
    }

    public interface NetworkListener {
        void onChange(boolean isGoodConnection);
    }
}
