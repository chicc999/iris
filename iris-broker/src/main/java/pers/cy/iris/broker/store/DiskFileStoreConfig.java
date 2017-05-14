package pers.cy.iris.broker.store;

import java.io.File;

/**
 * @Author:cy
 * @Date:Created in  17/4/17
 * @Destription:
 */
public class DiskFileStoreConfig {
	//日志文件大小
	public static final int JOURNAL_FILE_LENGTH = 1024 * 1024 * 128;
	//日志文件大小
	private int journalFileSize = JOURNAL_FILE_LENGTH;



	//数据目录
	private String dataDirectory;

	public String getDataDirectory() {
		return dataDirectory;
	}

	public void setDataDirectory(String dataDirectory) {
		this.dataDirectory = dataDirectory;
	}

	public void setJournalFileSize(int journalFileSize) {
		this.journalFileSize = journalFileSize;
	}

	public static int getJournalFileLength() {
		return JOURNAL_FILE_LENGTH;
	}
}
