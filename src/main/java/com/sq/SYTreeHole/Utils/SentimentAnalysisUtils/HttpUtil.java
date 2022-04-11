package com.sq.SYTreeHole.Utils.SentimentAnalysisUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * Http Client 工具类
 */
public class HttpUtil {
	public static String doPost(String url, Map<String, String> header, String body) {
		StringBuilder result = new StringBuilder();
		BufferedReader in;
		PrintWriter out;
		try {
			URL realUrl = new URL(url);
			URLConnection connection = realUrl.openConnection();
			HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
			for (String key : header.keySet())
				httpURLConnection.setRequestProperty(key, header.get(key));
			// 设置请求 body
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			out = new PrintWriter(httpURLConnection.getOutputStream());
			out.print(body);
			out.flush();
			if (HttpURLConnection.HTTP_OK != httpURLConnection.getResponseCode()) {
				System.out.println("Http 请求失败，状态码：" + httpURLConnection.getResponseCode());
				return null;
			}
			in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null)
				result.append(line);
		} catch (Exception e) {
			return null;
		}
		return result.toString();
	}
}
