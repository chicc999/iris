package pers.cy.iris.broker.store.file;

import org.testng.annotations.*;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * @Author:cy
 * @Date:Created in  18/5/10
 * @Destription: channelFile测试类
 */
public class TestChannelFile {

	private final String baseDir = "/export/test/iris/store";
	private ChannelFile channelFile;

	@BeforeClass
	public void beforeClass() {
		File f = new File(baseDir);
		if (!f.exists()) {
			f.mkdirs();
		}
		f = new File(baseDir + File.separator + this.getClass().getName());
		if (f.exists()) {
			f.delete();
		}

		try {
			RandomAccessFile rf = new RandomAccessFile(f,"rw");
			rf.setLength(123);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(f.length());
		channelFile = new ChannelFile(f);
	}

	@AfterClass
	public void afterClass() {
		channelFile.close();
		if(channelFile.getFile().exists()){
			channelFile.getFile().delete();
		}
	}

	@Test
	public void testAppend() {

	}

}
