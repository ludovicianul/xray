package com.insidecoding.xray.sensor;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.insidecoding.xray.model.Commit;
import com.insidecoding.xray.report.Report;

public abstract class XRayFileSensor implements XRaySensor {

	@Value("${root:}")
	private String root;

	protected Logger LOG = LoggerFactory.getLogger(getClass());

	@PostConstruct
	private void init() {
		if (root.isEmpty()) {
			LOG.error("You must supply a root file!");
			System.exit(0);
		}
	}

	public Report analyse(List<Commit> commits) {
		return this.analyse(commits, root);
	}

	public abstract Report analyse(List<Commit> commits, String file);
}
