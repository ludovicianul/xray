package com.insidecoding.xray.engine;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.insidecoding.xray.model.Commit;
import com.insidecoding.xray.report.Report;
import com.insidecoding.xray.sensor.RunawaySensor;

@Component
public class XRayEngine {
	private static final Logger LOG = LoggerFactory.getLogger(XRayEngine.class);

	@Autowired
	private LogParser logParser;

	@Autowired
	private RunawaySensor runSensor;

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
			Report runAwayReport = runSensor.analyse(commits);
			// build sensor chain
			// run sensor chain
			// print the reports depending on format
			LOG.info("Runaway report :: " + runAwayReport.prettyFormat());
			LOG.info("Runaway recommendations :: " + runAwayReport.recommendations());
			LOG.info("Done. Finished parsing " + commits.size() + " commits in " + (System.currentTimeMillis() - start)
					+ " milliseconds. You can check the reports now ::");
		} catch (IOException e) {
			LOG.warn("Something went wrong: " + e.getMessage());
		}
	}
}