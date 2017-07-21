package com.edcs.tds.storm.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestUtils {
	
	public static InputStream sendState(String cmccrul, String write) {

        HttpURLConnection connection = null;
        URL url = null;
        InputStream is = null;
        OutputStream os = null;
        DataOutputStream dos = null;
        try {
            url = new URL(cmccrul);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Authorization", "Basic eWV6bDpBYTEyMzQ1Ng==");
            os = connection.getOutputStream();
            dos = new DataOutputStream(os);
            dos.write(write.getBytes());
            dos.flush();
            is = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();//FIXME 统一输出到日志
            try {
            	if(dos!=null) dos.close();
            	if(os!=null) os.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
        }finally{
        	try {
            	if(dos!=null) dos.close();
            	if(os!=null) os.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
        }
        return is;
    }

}
