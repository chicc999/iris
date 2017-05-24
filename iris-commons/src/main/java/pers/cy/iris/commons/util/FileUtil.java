package pers.cy.iris.commons.util;

import java.io.File;
import java.io.IOException;

/**
 * @Author:cy
 * @Date:Created in  17/5/24
 * @Destription: 文件操作辅助类
 */
public class FileUtil {

	/**
	 * 创建文件
	 *
	 * @param file 文件
	 * @return 创建成功
	 */
	public static boolean createFile(File file) {
		if (file == null) {
			return false;
		}
		if (file.exists()) {
			return true;
		}
		try {
			if (file.createNewFile()) {
				return true;
			}
			return file.exists();
		} catch (IOException e) {
			return false;
		}
	}
}
