package com.mynew.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by user on 2017/4/13.
 */
public class HttpUrlConnectUtil {
    private static String TAG="HttpUrlConnectUtil";

    //get方式访问服务器
    public static String getFormWebByHttpUrlConnect(String path,Map<String,String> param){
        StringBuffer result=null;

        try{
            URL url=new URL(path);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();   //打开网络连接
            connection.setConnectTimeout(3000);              //设置连接超时时长
            connection.setReadTimeout(4000);
            connection.setDoInput(true);                    //打开输入流
            connection.setDoOutput(true);                  //打来输出流
            connection.setRequestMethod("GET");

            if(connection.getResponseCode()==200) {                      //访问数据成功
                InputStream inputStream = connection.getInputStream();     //获取数据库传回的数据
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = reader.readLine();
                result=new StringBuffer();
                while (line!=null){
                    result.append(line);
                    line=reader.readLine();
                }
                reader.close();
            }
            connection.disconnect();

            return result.toString();
        }catch (MalformedURLException e){
            Log.e(TAG,"getFormWebByHttpUrlConnectionMalformedURLExction:"+e.getMessage());
            e.printStackTrace();
            return null;
        }catch (IOException e){
            Log.e(TAG,"getFormWebByHttpUrlConnectionIOException:"+e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    //post方式访问服务器
    public static String postFormWebByHttpUrlConnect(String path,Map<String,String> param){
        StringBuffer result=null;
        try{
            URL url=new URL(path);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();   //打开网络连接
            connection.setConnectTimeout(3000);              //设置连接超时时长
            connection.setReadTimeout(4000);
            connection.setDoInput(true);                    //打开输入流
            connection.setDoOutput(true);                  //打来输出流
            connection.setRequestMethod("POST");

            if(getParamDate(param)!=null) {                                                             //数据不为空，执行写入数据
                byte[] data = getParamDate(param).getBytes();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); //设置头字段
                connection.setRequestProperty("Content-Length", String.valueOf(data.length));           //设置实体长度
                OutputStream out = connection.getOutputStream();                                           //获取输出流
                out.write(data);                                                                         //发送数据
            }
            if(connection.getResponseCode()==200) {                      //访问数据成功
                InputStream inputStream = connection.getInputStream();     //获取数据库传回的数据
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = reader.readLine();
                while(line != null) {
                    result.append(line);
                    line=reader.readLine();
                }
            }
            return result.toString();
        }catch (MalformedURLException e){
            Log.e(TAG,"getFormWebByHttpUrlConnection:"+e.getMessage());
            e.printStackTrace();
            return null;
        }catch (IOException e){
            Log.e(TAG,"getFormWebByHttpUrlConnection:"+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //获取传递的参数
    private static String getParamDate(Map<String,String> map){
        StringBuffer result=null;
        if(map==null){
            return null;
        }
            try {
                for (Map.Entry<String,String> entry:map.entrySet()) {
                    result.append(entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "utf-8") + "&");
                }
                result.deleteCharAt(result.length()-1);
                return result.toString();
            }catch (UnsupportedEncodingException e){
                Log.e(TAG,"getParamDateEncodingError:"+e.getMessage());
                e.printStackTrace();
                return null;
            }


    }
}
