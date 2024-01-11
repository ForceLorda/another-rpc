package com.forcelorda.rpc.nospring;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forcelorda.rpc.entity.RpcMethod;
import com.forcelorda.rpc.mapping.DefaultServiceMapping;

public class LocalRegisterServiceMapping extends DefaultServiceMapping {
	private ConcurrentHashMap<Set<String>, RpcMethod> map = new ConcurrentHashMap<>(); 
	
	private String packageName;

	public LocalRegisterServiceMapping(String packageName) {
		this.packageName = packageName;
		
	}

	public void scanPackage()
			throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		String scan = packageName.replaceAll("\\.", "/");
		Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(scan);
		while (dirs.hasMoreElements()) {
			URL url = dirs.nextElement();
			if (url.getProtocol().equals("file")) {
				List<File> classes = new ArrayList<File>();
				// 递归 变量路径下面所有的 class文件
				listFiles(new File(url.getFile()), classes);
				// 加载我们所有的 class文件 就行了
				loadeClasses(classes, scan);
			}

		}
	}

	private void loadClass(Class<?> clazz) throws InstantiationException, IllegalAccessException {
		Object bean = clazz.newInstance();
		if (bean.getClass().isAnnotationPresent(RestController.class)) {

			RequestMapping classRequestMapping = bean.getClass().getAnnotation(RequestMapping.class);
			if (null == classRequestMapping) {
				Method[] methods = bean.getClass().getMethods();
				for (Method method : methods) {
					Set<String> urls = new HashSet<>();
					RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
					String[] methodMappings = methodRequestMapping.value();
					for (String methodMapping : methodMappings) {
						urls.add(methodMapping);
					}
					RpcMethod rpcMethod = new RpcMethod(bean, method);
					registerServiceMapping(urls, rpcMethod);
				}

			} else {
				Method[] methods = bean.getClass().getMethods();
				String[] classMappings = classRequestMapping.value();
				for (String classMapping : classMappings) {
					for (Method method : methods) {
						Set<String> urls = new HashSet<>();
						RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
						String[] methodMappings = methodRequestMapping.value();
						for (String methodMapping : methodMappings) {
							if (classMapping.equals("/")) {
								urls.add(methodMapping);
							} else {
								urls.add(classMapping + methodMapping);
							}

						}
						RpcMethod rpcMethod = new RpcMethod(bean, method);
						registerServiceMapping(urls, rpcMethod);
					}
				}
			}

		}
	}

	private void loadeClasses(List<File> classes, String scan)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		for (File clazzFile : classes) {
			String clazzFilePath = clazzFile.getAbsolutePath().replaceAll("\\\\", "/");
			String className = clazzFilePath.substring(clazzFilePath.lastIndexOf(scan));
			className = className.replace(".class", "").replaceAll("/", ".");
			Class<?> clazz = Class.forName(className);
			loadClass(clazz);
		}

	}

	private void listFiles(File dir, List<File> fileList) {
		if (dir.isDirectory()) {
			for (File f : dir.listFiles()) {
				listFiles(f, fileList);
			}
		} else {
			if (dir.getName().endsWith(".class")) {
				fileList.add(dir);
			}
		}
	}
}
