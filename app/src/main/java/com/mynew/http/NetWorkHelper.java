package com.mynew.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.net.ConnectivityManagerCompat;
import android.util.Log;

/**
 * Created by user on 2017/5/1.
 */
public class NetWorkHelper {

    private final static String TAG="NetWorkHelper";
    //判断是否有网络
    public static boolean isNetWorkAvailable(Context context){
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager==null){
            return false;
        }
        NetworkInfo info=connectivityManager.getActiveNetworkInfo();
        if(info!=null&&info.isAvailable()){
            return true;
        }
        return false;
    }

    //判断是否为漫游
    public static boolean isNetWorkRoaming(Context context){
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager==null){
            Log.d(TAG,"connectivity is null");
        }else {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if(info!=null&&info.getType()==ConnectivityManager.TYPE_MOBILE){
                if(info.isRoaming()){
                    Log.d(TAG,"is roaming");
                    return true;
                }else {
                    Log.d(TAG,"not is roaming");
                }
            }else {
                Log.d(TAG,"not use roaming");
            }
        }

        return false;
    }

    //判断Mobile是否可用
    public static boolean isMobileDataEnable(Context context){
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager==null){
            Log.d(TAG,"connectivity is null");
        }else {
            NetworkInfo info=connectivityManager.getActiveNetworkInfo();
            if (info!=null&&info.getType()==ConnectivityManager.TYPE_MOBILE){
                return info.isConnectedOrConnecting();
            }
        }
        return false;
    }

    //判断WIFI是否可用
    public static boolean isWifiDataEnable(Context context){
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager==null){
            Log.d(TAG,"connectivity is null");
        }else {
            NetworkInfo info=connectivityManager.getActiveNetworkInfo();
            if (info!=null&&info.getType()==ConnectivityManager.TYPE_WIFI){
                return info.isConnectedOrConnecting();
            }
        }
        return false;
    }
}
