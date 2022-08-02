package cn.gxkj.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
public class BeanConvert {

	public static <T> T convertValue(Object data, Class clazz) {
		ObjectMapper mapper = new ObjectMapper();
		T userBean = mapper.convertValue(data, (Class<T>) clazz);
		return userBean;
	}
}
