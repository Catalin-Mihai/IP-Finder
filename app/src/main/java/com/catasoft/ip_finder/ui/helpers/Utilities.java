package com.catasoft.ip_finder.ui.helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.catasoft.ip_finder.R;

import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Utilities {

    private static final String IPV4_PATTERN =
            "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";

    private static final Pattern pattern = Pattern.compile(IPV4_PATTERN);

    public static boolean isValid(final String ip) {
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    public static void notGoodConnection(Context context, NetworkCallback callback){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(isNetworkAvailable(context)){
                    callback.onSuccess();
                    return;
                }
                callback.onFailure("Nu exista conexiune la internet");
            }
        });
        thread.start();
    }

    // https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android/4239019#4239019
    private static boolean isNetworkAvailable(Context context) {
        try{
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
    //https://stackoverflow.com/questions/9570237/android-check-internet-connection/9570292#9570292
    private static boolean isInternetAvailable() {
        try {
            InetAddress ipAddress = InetAddress.getByName("google.com");
            return !ipAddress.toString().equals("");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
     */

    public interface NetworkCallback {
        void onSuccess();
        void onFailure(String message);
    }
}
