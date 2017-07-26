package com.lucene.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.lucene.entity.News;

public class LuceneUtils {

	private static Version version;
	private static Analyzer analyzer;
	private static Directory directory;

	static {
		version = Version.LUCENE_41;
		analyzer = new IKAnalyzer();
		try {
			directory = FSDirectory.open(new File("d:\\indexdb"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static Version getVersion() {
		return version;
	}

	public static Analyzer getAnalyzer() {
		return analyzer;
	}

	public static Directory getDirectory() {
		return directory;
	}

	// 私有化构造器
	private LuceneUtils() {
	}

	// java对象转document对象
	public static Document javaBean2Document(Object obj) throws Exception {
		Document document = new Document();
		// 获取obj的类字节码文件描述
		Class clazz = obj.getClass();
		// 获取obj所有的属性
		java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
		for (java.lang.reflect.Field field : fields) {
			// 获取所有的属性
			field.setAccessible(true);
			// 获取属性名
			String name = field.getName();
			// 获取属性对应的get方法名
			String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
			// 获取方法
			Method method = clazz.getMethod(methodName, null);
			// 执行方法
			String value = method.invoke(obj, null).toString();
			document.add(new Field(name, value, TextField.TYPE_STORED));
		}

		return document;
	}

	//document对象转java对象
	public static Object decoment2JavaBean(Document document,Class clazz) throws Exception{
		Object obj = clazz.newInstance();
		java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
		for (java.lang.reflect.Field field : fields) {
			// 获取所有的属性
			field.setAccessible(true);
			// 获取属性名
			String name = field.getName();
			BeanUtils.setProperty(obj, name, document.get(name));
		}
		return obj;
	}
	
	public static void main(String[] args) throws Exception {
		News news1 = new News(2,"马云","马云是中国首富，首富");
		Document doc = LuceneUtils.javaBean2Document(news1);
		News news = (News)LuceneUtils.decoment2JavaBean(doc, News.class);
		System.out.println(news);
	}
}
