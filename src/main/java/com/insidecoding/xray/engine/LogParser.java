package com.insidecoding.xray.engine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.insidecoding.xray.model.Commit;
import com.insidecoding.xray.model.XRayFile;

@Component
public class LogParser {
	private static final Logger LOG = LoggerFactory.getLogger(LogParser.class);
	private static final Pattern pattern = Pattern.compile("--(.)+\n[^--]*");

	public List<Commit> parse(String logFile) throws IOException {
		LOG.info("Start parsing: " + logFile);
		List<Commit> commits = new ArrayList<>();

		String content = new String(Files.readAllBytes(Paths.get(logFile)));
		Matcher matcher = pattern.matcher(content);

		while (matcher.find()) {
			String commit = matcher.group().trim();
			commits.add(this.parseCommit(commit));
		}
		
		return commits;
	}

	private Commit parseCommit(String commitItem) {
		String[] lines = commitItem.split("\n");
		Commit commit = Commit.fromLogLine(lines[0]);
		if (lines.length > 1) {
			for (int i = 1; i < lines.length; i++) {
				commit.addFile(XRayFile.fromLogLine(lines[i]));
			}
		}

		return commit;
	}

}
