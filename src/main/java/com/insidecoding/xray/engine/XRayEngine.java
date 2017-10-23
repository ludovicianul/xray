package com.insidecoding.xray.engine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

import com.insidecoding.xray.model.Commit;
import com.insidecoding.xray.report.Report;

@Component
public class XRayEngine {
	private static final Logger LOG = LoggerFactory.getLogger(XRayEngine.class);

	@Autowired
	private LogParser logParser;

	@Autowired
	private AutowireCapableBeanFactory factory;

	@Value("${mode:}")
	private String mode;

	@PostConstruct
	public void valid() {
		if (mode.isEmpty()) {
			LOG.warn("No mode supplied! xray will exit");
			System.exit(0);
		}
	}

	/**
	 * TODO: The sensor chaining logic will be chain based and depending on user
	 * config.
	 * 
	 * @param logFile
	 */
	public void start(String logFile) {
		try {
			long start = System.currentTimeMillis();
			LOG.info("Starting...");
			List<Commit> commits = logParser.parse(logFile);
			Mode xrayMode = Mode.fromString(mode);
			LOG.info("Mode '{}' found!", mode);
			Report report = factory.createBean(xrayMode.sensor()).analyse(commits);

			String reportContent = report.prettyFormat();
			LOG.info("Report :: {}", reportContent);
			LOG.info("\n Saving report to file {}.xray", mode);
			Files.write(Paths.get(mode + ".xray"), reportContent.getBytes());
			LOG.info("Recommendations :: {}", report.recommendations());
			LOG.info(
					"Done. Finished parsing {} commits in {} milliseconds. You can check the report and recommendations now ::",
					commits.size(), (System.currentTimeMillis() - start));
		} catch (IOException e) {
			LOG.warn("Something went wrong: {} ", e.getMessage());
		}
	}
}