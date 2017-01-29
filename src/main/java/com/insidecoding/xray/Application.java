package com.insidecoding.xray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.insidecoding.xray.engine.XRayEngine;

@SpringBootApplication
public class Application implements CommandLineRunner {
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);
	@Value("${log:empty}")
	private String log;

	@Autowired
	private XRayEngine engine;

	public static void main(String... args) throws Exception {
		new SpringApplicationBuilder(Application.class).bannerMode(Banner.Mode.CONSOLE).build().run(args);

	}

	@Override
	public void run(String... args) throws Exception {
		if (!"empty".equals(log)) {
			engine.start(log);
		} else {
			LOG.error("!!! You must supply a log file !!!");
		}
	}
}
