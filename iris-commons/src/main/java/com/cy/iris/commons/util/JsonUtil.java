package com.cy.iris.commons.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Json工具类
 */
public class JsonUtil {
	private static final ObjectMapper mapper = new ObjectMapper();

	public static <T> T readValue(String content, Class<T> clazz) throws IOException {
		T t;
		try{
			t = mapper.readValue(content,clazz);
		}catch (Exception e){
			throw new IOException("反序列化失败",e);
		}
		return t;
	}
}
