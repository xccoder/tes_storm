package com.edcs.tds.common.engine.groovy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;

public class HttpRequestUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		StringBuilder result = new StringBuilder();

		BufferedReader in = null;

		try {
			String urlNameString = url + "?" + param;

			URL realUrl = new URL(urlNameString);

			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

			connection.connect();

			// 读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charsets.UTF_8));

			String line;

			while ((line = in.readLine()) != null) {
				result.append(line);
			}

		} catch (Exception e) {
			logger.error("发送GET请求出现异常！", e);
		}

		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				logger.error("", e2);
			}
		}
		return result.toString();
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;

		BufferedReader in = null;

		StringBuilder result = new StringBuilder();

		try {
			URL realUrl = new URL(url);

			URLConnection conn = realUrl.openConnection();

			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

			conn.setConnectTimeout(2000); // 设置连接请求超时为2s
			conn.setReadTimeout(2000); // 设置等待服务器返回数据的超时为2s

			conn.setDoOutput(true);
			conn.setDoInput(true);

			out = new PrintWriter(conn.getOutputStream());
			
			out.print(param);

			out.flush();
			// 读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charsets.UTF_8));

			String line;

			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (ConnectException e) {
			logger.error("Connect telecom interface time out", e);
			return "Connect time out";
		} catch (SocketTimeoutException e) {
			logger.error("Read telecom interface time out", e);
			return "Read time out";
		} catch (Exception e) {
			logger.error("发送POST请求出现异常！", e);
			return "Unknow exception";
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				logger.error("", ex);
			}
		}
		return result.toString();
	}
}
