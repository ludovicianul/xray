package com.insidecoding.xray.sensor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.insidecoding.xray.model.Commit;
import com.insidecoding.xray.model.XRayFile;
import com.insidecoding.xray.report.Report;
import com.insidecoding.xray.report.RunawayReport;

/**
 * Sensor used to monitor the Runaway Syndrome.
 * 
 * @author ludovicianul
 *
 */

public class RunawaySensor extends XRayTeamSensor {

	private Map<String, List<String>> extensionGroups = new HashMap<>();

	@Autowired
	private RunawayReport report;

	@PostConstruct
	private void init() {
		this.loadExtensionGroups();
	}

	private void loadExtensionGroups() {
		try {
			extensionGroups = Files.lines(Paths.get("extensions.properties")).map(line -> line.split("="))
					.collect(Collectors.toMap(p -> p[0], p -> Arrays.asList(p[1].split(",")).stream()
							.map(ext -> ext.trim()).collect(Collectors.toList())));
			LOG.info("Extension groups: " + extensionGroups);
		} catch (IOException e) {
			LOG.warn(
					"Error reading extensions file. Will only report unit tests, intergration tests, number of commits and commit age'");
		}
	}

	@Override
	public Report analyse(List<Commit> commits, Set<String> authors) {
		Map<String, List<Commit>> commitsByAuthor = commits.stream().filter(c -> authors.contains(c.author()))
				.collect(Collectors.groupingBy(Commit::author, Collectors.toList()));

		for (Entry<String, List<Commit>> commitData : commitsByAuthor.entrySet()) {
			String author = commitData.getKey();

			LongSummaryStatistics statistics = this.getCommitDateStatistics(commitData);
			List<XRayFile> files = this.collectFilesFromAllCommits(commitData);
			long merges = this.getNumberOfMerges(commitData);
			Map<String, Long> byExtensionGroup = this.groupByExtensionGroup(files);

			report.author(author).collect(author, RunawayReport.COMMIT_AGE, statistics.getMax() - statistics.getMin())
					.collect(author, RunawayReport.TOTAL_COMMITS, (long) commitData.getValue().size())
					.collect(author, RunawayReport.MERGES, merges)
					.collect(author, RunawayReport.UNIT_TESTS, this.analyseByNamePattern(files, "test"))
					.collect(author, RunawayReport.INTEGRATION_TESTS, this.analyseByNamePattern(files, "it"))
					.collect(author, byExtensionGroup);
		}

		return report;
	}

	private Map<String, Long> groupByExtensionGroup(List<XRayFile> files) {
		Map<String, Long> byExtension = files.stream()
				.collect(Collectors.groupingBy(XRayFile::extension, Collectors.counting()));
		Map<String, Long> byExtensionGroup = new HashMap<>();

		extensionGroups.entrySet().stream().forEach(item -> {
			long total = byExtension.entrySet().stream().filter(extEntry -> item.getValue().contains(extEntry.getKey()))
					.collect(Collectors.summarizingLong(it -> it.getValue())).getSum();
			byExtensionGroup.put(item.getKey(), total);
		});
		return byExtensionGroup;
	}

	private long getNumberOfMerges(Entry<String, List<Commit>> commitData) {
		long merges = commitData.getValue().stream().filter(c -> c.files().isEmpty()).collect(Collectors.counting());
		return merges;
	}

	private List<XRayFile> collectFilesFromAllCommits(Entry<String, List<Commit>> commitData) {
		List<XRayFile> files = commitData.getValue().stream().map(c -> c.files()).collect(ArrayList::new, List::addAll,
				List::addAll);
		return files;
	}

	private LongSummaryStatistics getCommitDateStatistics(Entry<String, List<Commit>> commitData) {
		LongSummaryStatistics statistics = commitData.getValue().stream()
				.collect(Collectors.summarizingLong(Commit::dateAsDays));
		return statistics;
	}

	private Long analyseByNamePattern(List<XRayFile> files, String pattern) {
		return files.stream()
				.filter(f -> f.name().toLowerCase().startsWith(pattern)
						|| f.name().toLowerCase().endsWith(pattern + "." + f.extension()))
				.collect(Collectors.counting());
	}

}
