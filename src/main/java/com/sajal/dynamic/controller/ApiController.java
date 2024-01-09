package com.sajal.dynamic.controller;

import com.sajal.dynamic.service.ClassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/api")
public class ApiController {
	private final Logger logger = LoggerFactory.getLogger(ApiController.class);
	private final ClassService classService;

	public ApiController(ClassService classService) {
		this.classService = classService;
	}

	@GetMapping("/reload")
	public void reload() {
		classService.reload();
		logger.debug("reload called");
	}

	@PostMapping(value = "/{service}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity api(@PathVariable String service, @RequestBody(required = false) String body) throws MalformedURLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		logger.debug("api "+ service+ " called");
		Object ob = classService.runClass(service,body);
		return ResponseEntity.ok(ob);
	}
}
