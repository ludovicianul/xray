package com.insidecoding.xray.sensor;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.insidecoding.xray.model.Commit;
import com.insidecoding.xray.report.Report;

public abstract class XRayTeamSensor implements XRaySensor {

	@Value("${filter:}")
	private String filter;

	private Set<String> authors;

	protected Logger LOG = LoggerFactory.getLogger(getClass());

	@PostConstruct
	private void init() {
		this.loadAuthors();
	}

	private void loadAuthors() {
		if (filter.isEmpty()) {
			LOG.error("You must supply a list of authors");
			System.exit(0);
		}
		authors = new TreeSet<String>(
				Arrays.asList(filter.split(",")).stream().map(item -> item.trim()).collect(Collectors.toList()));

		LOG.info("Filtering for: " + authors);
	}

	public Report analyse(List<Commit> commits) {
		return this.analyse(commits, authors);
	}

	public abstract Report analyse(List<Commit> commits, Set<String> authors);
}
