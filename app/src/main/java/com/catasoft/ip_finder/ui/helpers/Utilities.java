package com.catasoft.ip_finder.ui.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    private static final String IPV4_PATTERN =
            "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";

    private static final Pattern pattern = Pattern.compile(IPV4_PATTERN);

    public static boolean isValid(final String ip) {
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    public static boolean notGoodConnection(Context context){
        //return !(isNetworkAvailable(context) && isInternetAvailable());
        return true;
    }

    // https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android/4239019#4239019
    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //https://stackoverflow.com/questions/9570237/android-check-internet-connection/9570292#9570292
    private static boolean isInternetAvailable() {
        /*
        try {
            InetAddress ipAddress = InetAddress.getByName("www.google.com");
            System.out.println(ipAddress);
            System.out.println(ipAddress.toString());
            return !ipAddress.toString().equals("");
        } catch (Exception e) {
            return false;
        }
        */
         return true;
    }
}
