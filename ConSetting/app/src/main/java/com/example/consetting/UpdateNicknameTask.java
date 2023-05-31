package com.example.consetting;


import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class UpdateNicknameTask extends AsyncTask<String, Void, Integer> {

    @Override
    protected Integer doInBackground(String... strings) {
        String newNickname = strings[0];

        URL url;
        try {
            url = new URL(Constants.URL_Setting);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return 2;
        }

        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return 3;
        }

        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
            return 4;
        }

        connection.setDoOutput(true);
        OutputStream outputStream;
        try {
            outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data = URLEncoder.encode("nickname", "UTF-8") + "=" + URLEncoder.encode(newNickname, "UTF-8");
            writer.write(data);
            writer.flush();
            writer.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 5;
        }

        try {
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                inputStream.close();
                connection.disconnect();

                String responseData = response.toString();
                if (responseData.equals("success")) {
                    return 0;
                } else {
                    return 1;
                }
            } else {
                return 6;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 7;
        }
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (result == 0) {
            // 更新成功的处理逻辑
        } else {
            // 更新失败的处理逻辑
        }
    }
}

