package pers.cy.iris.client.cli.exception;

/**
 * @Author:cy
 * @Date:Created in 16:38 17/3/13
 * @Destription: 命令行解析异常
 */
public class CliParseException extends CliException {
	public CliParseException(ParseException parseException) {
		super(parseException);
	}

	public CliParseException(String message) {
		super(message);
	}
}