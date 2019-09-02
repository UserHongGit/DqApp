package com.hong.util;


public class NullHelper {
	/**
	 * 如果words为null替换为空
	 * @param words
	 * @return
	 */
	public static String isNull(String words){
		if(words==null||words.equals("null")){
			return "";
		}else{
			return words;
		}
	}
	public static String isNullNull(String words){
		if(words==null||words.equals("null")){
			return "";
		}else{
			return words+"-";
		}
	}
	public static StringBuffer delHelper(StringBuffer sb){
		sb.delete(sb.length()-1, sb.length());
        return sb;
	}
	
	/**
	 * 传递实体,遍历实体属性,将属性值为null替换为空
	 * @param o
	 */
//	public static void entityNull2(Object o){
//		if(o instanceof Zyfp1){
//			Zyfp1 oo = (Zyfp1)o;
//			//System.out.println(oo.getCcfs()+"//"+oo.getBxdw());
//			Field[] fileds = oo.getClass().getDeclaredFields();
//			for(int i = 0 ; i <fileds.length;i++){
//				String type = fileds[i].getGenericType().toString();
//				 if (type.equals("class java.lang.String")) {
//					 String name = fileds[i].getName();
//					 name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
//					 try {
//						 Method m = oo.getClass().getMethod("get" + name);
//						 String value = (String) m.invoke(oo);
//						 String temp = NullHelper.isNull(value);
//						 Method m2 = oo.getClass().getMethod("set" + name,String.class);
//						 m2.invoke(oo,temp);
//					 } catch (Exception e) {
//						 System.err.println("获取属性值出错!!!");
//						 e.printStackTrace();
//					 }
//
//		         }
//
//			}
//			for(int i = 0 ; i <fileds.length;i++){
//				String type = fileds[i].getGenericType().toString();
//				 if (type.equals("class java.lang.String")) {
//					 String name = fileds[i].getName();
//					 name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
//					 try {
//						 Method m = oo.getClass().getMethod("get" + name);
//						 String value = (String) m.invoke(oo);
//						// System.out.println("22222"+name+"?///"+value);
//					 } catch (Exception e) {
//						 //System.err.println("获取属性值出错!!!");
//						 e.printStackTrace();
//					 }
//
//		         }
//
//			}
//
//		}else if(o instanceof Zyrb){
//
//
//			Zyrb oo = (Zyrb)o;
//			Field[] fileds = oo.getClass().getDeclaredFields();
//			for(int i = 0 ; i <fileds.length;i++){
//				String type = fileds[i].getGenericType().toString();
//				 if (type.equals("class java.lang.String")) {
//					 String name = fileds[i].getName();
//					 name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
//					 try {
//						 Method m = oo.getClass().getMethod("get" + name);
//						 String value = (String) m.invoke(oo);
//						 String temp = NullHelper.isNull(value);
//						 Method m2 = oo.getClass().getMethod("set" + name,String.class);
//						 m2.invoke(oo,temp);
//					 } catch (Exception e) {
//						 System.err.println("获取属性值出错!!!");
//						 e.printStackTrace();
//					 }
//
//		         }
//
//			}
//
//
//		}
//
//	}
	
	
}
