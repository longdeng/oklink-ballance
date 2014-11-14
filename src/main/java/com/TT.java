package com;
/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-13 下午5:07:49
 * 类说明
 */
public class TT {

	public static void main(String[] args) {
		TT tt = new TT();
		String clazz = Thread.currentThread().getStackTrace()[1].getClassName();
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println("class name: " + clazz + " Method Name " + method);
		tt.anotherMethod();
	}

	private void anotherMethod() {
		String clazz = Thread.currentThread().getStackTrace()[1].getClassName();
		String method = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println("class name: " + clazz + " Method Name " + method);
	}

}
