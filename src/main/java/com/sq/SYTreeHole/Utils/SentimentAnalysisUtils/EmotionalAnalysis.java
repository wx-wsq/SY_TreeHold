package com.sq.SYTreeHole.Utils.SentimentAnalysisUtils;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;


public class EmotionalAnalysis {
	// webapi接口地址
	private static final String WEBTTS_URL = "https://ltpapi.xfyun.cn/v2/sa";
	// 应用ID
	private static final String APPID = "ab6fc10a";
	// 接口密钥
	private static final String API_KEY = "4c27a475894621e0e73328c8c2f15d75";

	private static final String TYPE = "dependent";

	@SuppressWarnings("ALL")
	public static Map<Object,Object> getMarkToMap(String text) {
		Map<String, String> header = buildHttpHeader();
		ObjectMapper objectMapper = new ObjectMapper();
		String result =  HttpUtil.doPost(WEBTTS_URL, header, "text=" + URLEncoder.encode(text, StandardCharsets.UTF_8));
		try {
			return objectMapper.readValue(result, Map.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 组装http请求头
	 */
	private static Map<String, String> buildHttpHeader() {
		String curTime = System.currentTimeMillis() / 1000L + "";
		String param = "{\"type\":\"" + TYPE +"\"}";
		String paramBase64 = new String(Base64.encodeBase64(param.getBytes(StandardCharsets.UTF_8)));
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert md != null;
        md.update((API_KEY + curTime + paramBase64).getBytes());
		String checkSum = new BigInteger(1, md.digest()).toString(16);
		System.out.println(checkSum);
		Map<String, String> header = new HashMap<>();
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		header.put("X-Param", paramBase64);
		header.put("X-CurTime", curTime);
		header.put("X-CheckSum", checkSum);
		header.put("X-Appid", APPID);
		return header;
	}
}
