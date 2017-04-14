package pers.cy.iris.commons.util;

/**
 * @Author:cy
 * @Date:Created in  17/4/14
 * @Destription: 字符与unicode互转工具类
 */
public class CharacterSetUtil {

	public CharacterSetUtil() {
	}

	private static final char[] hexDigit = {
			'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
	};

	private static char toHex(int nibble) {
		return hexDigit[(nibble & 0xF)];
	}

	/**
	 * 将字符串编码成 Unicode 。
	 * @param theString 待转换成Unicode编码的字符串。
	 * @param escapeSpace 是否忽略空格。
	 * @return 返回转换后Unicode编码的字符串。
	 */
	public static String toUnicode(String theString, boolean escapeSpace) {
		int len = theString.length();
		int bufLen = len * 2;
		if (bufLen < 0) {
			bufLen = Integer.MAX_VALUE;
		}
		StringBuilder outBuffer = new StringBuilder(bufLen);

		appendUnicode(outBuffer,theString,escapeSpace);
		return outBuffer.toString();
	}

	/**
	 * 将字符串编码成 Unicode 。
	 * @param theString 待转换成Unicode编码的字符串。
	 * @param escapeSpace 是否忽略空格。
	 * @return 返回转换后Unicode编码的字符串。
	 */
	public static void appendUnicode(StringBuilder outBuffer, String theString, boolean escapeSpace) {
		for(int x=0; x<theString.length(); x++) {
			char aChar = theString.charAt(x);
			// Handle common case first, selecting largest block that
			// avoids the specials below
			if ((aChar > 61) && (aChar < 127)) {
				if (aChar == '\\') {
					outBuffer.append('\\'); outBuffer.append('\\');
					continue;
				}
				outBuffer.append(aChar);
				continue;
			}
			switch(aChar) {
				case ' ':
					if (x == 0 || escapeSpace)
						outBuffer.append('\\');
					outBuffer.append(' ');
					break;
				case '\t':outBuffer.append('\\'); outBuffer.append('t');
					break;
				case '\n':outBuffer.append('\\'); outBuffer.append('n');
					break;
				case '\r':outBuffer.append('\\'); outBuffer.append('r');
					break;
				case '\f':outBuffer.append('\\'); outBuffer.append('f');
					break;
				case '=': // Fall through
				case ':': // Fall through
				case '#': // Fall through
				case '!':
					outBuffer.append('\\'); outBuffer.append(aChar);
					break;
				default:
					if ((aChar < 0x0020) || (aChar > 0x007e)) {
						outBuffer.append('\\');
						outBuffer.append('u');
						outBuffer.append(toHex((aChar >> 12) & 0xF));
						outBuffer.append(toHex((aChar >>   8) & 0xF));
						outBuffer.append(toHex((aChar >>   4) & 0xF));
						outBuffer.append(toHex( aChar         & 0xF));
					} else {
						outBuffer.append(aChar);
					}
			}
		}
	}

	/**
	 * 从 Unicode 码转换成编码前的特殊字符串。
	 * @param in Unicode编码的字符数组。
	 * @param off 转换的起始偏移量。
	 * @param len 转换的字符长度。
	 * @param convtBuf 转换的缓存字符数组。
	 * @return 完成转换，返回编码前的特殊字符串。
	 */
	public String fromUnicode(char[] in, int off, int len, char[] convtBuf) {
		if (convtBuf.length < len) {
			int newLen = len * 2;
			if (newLen < 0) {
				newLen = Integer.MAX_VALUE;
			}
			convtBuf = new char[newLen];
		}
		char aChar;
		char[] out = convtBuf;
		int outLen = 0;
		int end = off + len;

		while (off < end) {
			aChar = in[off++];
			if (aChar == '\\') {
				aChar = in[off++];
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = in[off++];
						switch (aChar) {
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								value = (value << 4) + aChar - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								value = (value << 4) + 10 + aChar - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								value = (value << 4) + 10 + aChar - 'A';
								break;
							default:
								throw new IllegalArgumentException(
										"Malformed \\uxxxx encoding.");
						}
					}
					out[outLen++] = (char) value;
				} else {
					if (aChar == 't') {
						aChar = '\t';
					} else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f') {
						aChar = '\f';
					}
					out[outLen++] = aChar;
				}
			} else {
				out[outLen++] = (char) aChar;
			}
		}
		return new String(out, 0, outLen);
	}
}
