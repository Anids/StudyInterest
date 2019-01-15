package com.swust.stylezz.studyinteret.http;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

public class HttpClient {
    private static JSONObject myJson=null;
    public static JSONObject sendRequestWithHttpClient(final String requestMethod, final String serverUrl,final JSONObject obj,final String userToken ) throws InterruptedException {
        final String token="'"+userToken+"'";
        Thread  thread = null;
        if(requestMethod=="GET")
        {
            thread=new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        URL url=new URL(serverUrl);
                        HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                        urlConnection.setConnectTimeout(5000);
                        urlConnection.setReadTimeout(5000);
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setRequestProperty("Authorization",token);
                        urlConnection.connect();
                        int code=urlConnection.getResponseCode();
                        if (code==200) {
                            InputStream inputStream=urlConnection.getInputStream();
                            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader (inputStream));
                            String line;
                            StringBuffer buffer=new StringBuffer();
                            while ((line=bufferedReader.readLine())!=null) {
                                buffer.append(line);

                            }
                            myJson = new JSONObject(buffer.toString());
                        }

                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };

            });
        }
        else
        {
            thread=new Thread(new Runnable() {

                @Override
                public void run()
                //post请求
                {
                    try {
                        //创建连接
                        URL url = new URL(serverUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        connection.setRequestMethod(requestMethod);
                        connection.setUseCaches(false);
                        connection.setRequestProperty("Authorization",token);

                        connection.setInstanceFollowRedirects(true);
                        connection.setRequestProperty("Content-Type", "application/json");
                        connection.connect();
                        // POST请求
                        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                        //参数

                        String json = obj.toString();
                        out.writeBytes(json);
                        out.flush();
                        out.close();
                        // 读取响应
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String lines;
                        StringBuffer sb = new StringBuffer("");
                        while ((lines = reader.readLine()) != null) {
                            lines = URLDecoder.decode(lines, "utf-8");
                            sb.append(lines);
                        }
                        myJson = new JSONObject(sb.toString());
                        String st= (String) myJson.get("status");
                        reader.close();
                        // 断开连接
                        connection.disconnect();

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        thread.start();
        thread.join();
        return myJson;
    }
}
