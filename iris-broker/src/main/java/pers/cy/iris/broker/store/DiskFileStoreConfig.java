package pers.cy.iris.broker.store;

import java.io.File;

/**
 * @Author:cy
 * @Date:Created in  17/4/17
 * @Destription:
 */
public class DiskFileStoreConfig {

	//日志文件大小,默认128M
	private int journalFileSize = 1024 * 1024 * 128;
	//备份文件后缀
	public static String BACK_SUFFIX = ".1";

	//数据目录
	private String dataDirectory = "/export/data";

	public String getDataDirectory() {
		return dataDirectory;
	}

	public void setDataDirectory(String dataDirectory) {
		this.dataDirectory = dataDirectory;
	}

	public void setJournalFileSize(int journalFileSize) {
		this.journalFileSize = journalFileSize;
	}

	public int getJournalFileLength() {
		return this.journalFileSize;
	}
}
