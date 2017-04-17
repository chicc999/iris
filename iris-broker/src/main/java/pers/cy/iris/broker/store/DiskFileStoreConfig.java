package pers.cy.iris.broker.store;

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

	public void setJournalFileSize(int journalFileSize) {
		this.journalFileSize = journalFileSize;
	}

	public static int getJournalFileLength() {
		return JOURNAL_FILE_LENGTH;
	}
}
