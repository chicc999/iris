package pers.cy.iris.broker.store;

/**
 * @Author:cy
 * @Date:Created in  17/4/17
 * @Destription: 存储文件的配置
 */
public class DiskFileStoreConfig {

	//日志文件大小,默认128M
	private int journalFileSize = 1024 * 1024 * 128;
	//备份文件后缀
	public static String BACK_SUFFIX = ".1";

	//数据目录
	private String dataDirectory = "/export/data";

	//日志镜像文件占用最大内存空间(20G,160个日志文件)
	private long journalCacheSize = 21474836480L;
	//队列镜像文件占用最大内存空间(10G,,1626个队列文件)
	private long queueCacheSize = 10737418240L;
	//缓存新生代比率(保留最热点的文件，超过则启动检查程序卸载)
	private int cacheNewRatio = 40;
	//缓存最大比率(超过该值，则不能缓存，可以设置为0不启用缓存)
	private int cacheMaxRatio = 95;
	//缓存强制收回的比率阀值(超过则按照优先级强制收回，队列文件数也可以尽量保留少)
	private int cacheEvictThreshold = 90;
	//缓存一次强制收回的最小比率
	private int cacheEvictMinRatio = 10;
	//优先保留每个队列最近的文件数量
	private int cacheQueueFiles = 3;
	//缓存最大空闲时间，超过则会被移除到待清理队列
	private int cacheMaxIdle = 1000 * 60 * 10;
	//检查缓存时间间隔
	private int cacheCheckInterval = 1000 * 2;
	//缓存延时回收的时间，为0则立即释放
	private int cacheEvictLatency = 1000 * 10;
	//缓存存在内存泄露时间
	private int cacheLeakTime = 1000 * 180;

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
