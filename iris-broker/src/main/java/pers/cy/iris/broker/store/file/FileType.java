package pers.cy.iris.broker.store.file;

/**
 * @Author:cy
 * @Date:Created in  17/5/19
 * @Destription: 文件类型
 */
public enum FileType {
	/**
	 * 日志文件
	 */
	JOURNAL(1, "journal"),
	/**
	 * 队列文件
	 */
	QUEUE(2, "queue");
	// ID
	private int id;
	// 名称
	private String name;

	FileType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
