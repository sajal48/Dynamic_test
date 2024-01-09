package com.sajal.dynamic.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;


@Service
public class ClassService {
	private String userDir = "";
	private String sourceDir = "";
	private String classDir = "";

    @PostConstruct
	public void reload() {
		userDir = getUserDirectory();
		sourceDir = Paths.get(userDir, "Code", "java").toString();
		classDir = Paths.get(userDir, "Code", "class").toString();
		compileJavaFiles(sourceDir, classDir);
	}

	public Object runClass(String className,String argument) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		URLClassLoader classLoader = new URLClassLoader(new URL[]{new File(classDir).toURI().toURL()});
		Class<?> dynamicClass = classLoader.loadClass(className);
		Object instance = dynamicClass.getDeclaredConstructor().newInstance();
		Object result = dynamicClass.getMethod("run", Map.class).invoke(instance, convertJsonStringToMap(argument));
		return  result;
	}

	private String getUserDirectory() {
		return Paths.get("").toAbsolutePath().toString();
	}

	public Map<String, Object> convertJsonStringToMap(String jsonString) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(jsonString, Map.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void compileJavaFiles(String sourceDirectory, String classDirectory) {
		try {
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
			File outputDir = new File(classDirectory);
			if (!outputDir.exists()) {
				outputDir.mkdirs();
			}
			File[] sourceFiles = new File(sourceDirectory).listFiles((dir, name) -> name.endsWith(".java"));
			compiler.getTask(null, fileManager, null, Arrays.asList("-d", classDirectory), null,
					fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFiles))).call();
			fileManager.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
