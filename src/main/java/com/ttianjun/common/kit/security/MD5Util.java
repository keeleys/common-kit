package com.ttianjun.common.kit.security;

import java.security.MessageDigest;

public class MD5Util {
    
    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    private static String byteArrayToHexString(byte b[]) {
	StringBuffer resultSb = new StringBuffer();
	for (int i = 0; i < b.length; i++) {
	    resultSb.append(byteToHexString(b[i]));
	}

	return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
	int n = b;
	if (n < 0)
	    n += 256;
	int d1 = n / 16;
	int d2 = n % 16;
	return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * MD5 32位加密
     * 
     * @param origin
     *                字符源
     * @param charset
     *                字符编码
     * @return 32位密文
     */
    public static String code32(String origin, String charset) {
		String resultString = null;
		try {
		    resultString = new String(origin);
		    MessageDigest md = MessageDigest.getInstance("MD5");
		    if (charset == null || charset.isEmpty()) {
		    	resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
		    } else {
		    	resultString = byteArrayToHexString(md.digest(resultString.getBytes(charset)));
		    }
		} catch (Exception exception) {
		}
		return resultString;
    }

    /**
     * MD5 16位加密
     * 
     * @param origin
     *                字符源
     * @param charset
     *                字符编码
     * @return 16位密文
     */
    public static String code16(String origin, String charset) {
	String resultString = code32(origin, charset);
	return resultString.substring(8, 24);
    }
   

    public static String MD5Encode(String origin, String charsetname) {
	String resultString = null;
	try {
	    resultString = new String(origin);
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    if (charsetname == null || "".equals(charsetname))
		resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
	    else
		resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
	} catch (Exception exception) {
	}
	return resultString;
    }

    public static String doSign(String content, String privateKey, String charset) throws Exception {
	String signStr = "";
	try {
	    StringBuffer sb = new StringBuffer();
	    sb.append(content);
	    sb.append(privateKey);
	    signStr = MD5Encode(sb.toString(), charset);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    throw new Exception("MD5签名[content = " + content + "; privateKey = " + privateKey + "; charset = " + charset + "]发生异常!", e);
	}
	return signStr;
    }

    public static String md5Sign(String plainText,String charset) throws Exception {
    	MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(plainText.getBytes(charset));
        byte b[] = md.digest();
        int i;
        StringBuffer buf = new StringBuffer("");
        for (int offset = 0; offset < b.length; offset++) {
           i = b[offset];
           if (i < 0)
              i += 256;
           if (i < 16)
              buf.append("0");
           buf.append(Integer.toHexString(i));
        }
        return buf.toString();
    }
    
    public static void main(String[] args) {
    	
    }
}
