package com.util.MD5;

public class Client {

	public static void main(String[] args) {
		String encryptedPwd = MD5Util.getEncryptedPwd("111112222");
		System.out.println(encryptedPwd);
		boolean bool = MD5Util.validation("111112222", encryptedPwd);
		System.out.println(bool);

	}
}
