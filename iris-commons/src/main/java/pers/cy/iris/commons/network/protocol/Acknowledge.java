package pers.cy.iris.commons.network.protocol;

/**
 * 应答方式
 */
public enum Acknowledge {

	/**
	 * 刷盘后应答
	 */
	ACK_FLUSH,
	/**
	 * 接收到数据应答
	 */
	ACK_RECEIVE,
	/**
	 * 不应答
	 */
	ACK_NO,
	/**
	 * 写入后应答
	 */
	ACK_WRITE;

	public static Acknowledge valueOf(final int value) {
		switch (value) {
			case 0:
				return ACK_FLUSH;
			case 1:
				return ACK_RECEIVE;
			case 2:
				return ACK_NO;
			case 3:
				return ACK_WRITE;
			default:
				return ACK_FLUSH;
		}
	}
}
