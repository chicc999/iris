package com.cy.iris.commons.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ReferenceType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Json工具类
 */
public class JsonUtil {
	private static final ObjectMapper mapper = new ObjectMapper();

	/**
	 * Json 转换成对应的 class
	 * @param content
	 * @param clazz
	 * @param <T>
	 * @return
	 * @throws IOException
	 */
	public static <T> T readValue(String content, Class<T> clazz) throws IOException {
		ArgumentUtil.isNotBlank(content);
		T t;
		try{
			t = mapper.readValue(content,clazz);
		}catch (Exception e){
			throw new IOException("反序列化失败",e);
		}
		return t;
	}

	/**
	 * Java类序列化
	 * @param o
	 * @return
	 * @throws IOException
	 */
	public static byte[] writeValue(Object o) throws IOException {
		ArgumentUtil.isNotNull("Object",o);
		byte[] result;
		try {
			result = mapper.writeValueAsBytes(o);
		} catch (JsonProcessingException e) {
			throw new IOException("序列化失败",e);
		}
		return result;
	}

	/**
	 * 从json串中获取一个list,容器中元素为clazz
	 * @param content
	 * @param clazz
	 * @param <T>
	 * @return
	 * @throws IOException
	 */
	public static <T> List<T> readListValue(String content, Class<T> clazz) throws IOException {
		ArgumentUtil.isNotBlank(content);
		JavaType javaType = getCollectionType(ArrayList.class, clazz);
		List<T> list;
		try {
			list = mapper.readValue(content, javaType);
		} catch (Exception e) {
			throw new IOException("反序列化失败", e);
		}
		return list;
	}

	/**
	 * 从json串中获取一个map,容器中元素为类型为key,value
	 * @param content
	 * @param key
	 * @param value
	 * @param <K>
	 * @param <V>
	 * @return
	 * @throws IOException
	 */
	public static <K,V> Map<K,V> readMapValue(String content, Class<K> key,Class<V> value) throws IOException {
		ArgumentUtil.isNotBlank(content);

		Map<K,V> map ;
		try {
			map = mapper.readValue(content, new TypeReference<Map<K,V>>() { });
		} catch (Exception e) {
			throw new IOException("反序列化失败", e);
		}
		return map;
	}

	private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}
}
