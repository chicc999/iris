package pers.cy.iris.commons.util;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.*;
import java.lang.reflect.Method;

/**
 * @Author:cy
 * @Date:Created in  17/7/14
 * @Destription: JsonUtil测试类
 */
public class JsonUtilTest {

	private String dataDir = "/export/test";
	private File  file = null;

	@DataProvider
	public Object[][] providerMethod(Method method){
		Object[][] result = null;
		if(method.getName().equals("testWriteTOStream")){
			result = new Object[][]{new Object[]{"aaa","1.txt"},new Object[]{"bbb","2"}};
		}else if(method.getName().equals("testmethod2")){
			result = new Object[][]{new Object[]{2}};
		}else{
			result = new Object[][]{new Object[]{3}};
		}
		return result;
	}

	@BeforeMethod
	public void  beforeMethod(){
		file = new File(dataDir);
		if(!file.exists()){
			file.mkdirs();
		}
	}

	@Test(dataProvider="providerMethod")
	public void testWriteTOStream(Object target ,String fileName) throws IOException {

		if(target==null || StringUtils.isEmpty(fileName)){
			throw new IllegalArgumentException(String.format("非法的参数 target=%s , fileName=%s",target,fileName));
		}

		File file = new File(dataDir,fileName);

		if(!file.isDirectory() && file.exists()){
			file.delete();
		}
		if(!file.createNewFile()){
			throw new RuntimeException("创建文件 \""+fileName+"\" 失败");
		}

		if(!file.canRead() || !file.canWrite()){
			throw new RuntimeException("文件 \""+fileName+"\" 缺少权限");
		}

		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			JsonUtil.writeTOStream(outputStream,target);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(outputStream != null){
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(file.exists()){
				file.delete();
			}
		}
	}

	@AfterMethod
	public void afterMethod() throws IOException {
	}
}
