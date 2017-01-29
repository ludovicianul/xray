package com.insidecoding.xray.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Commit {

	private String author;
	private String hash;
	private String date;
	private List<XRayFile> files = new ArrayList<>();

	public String author() {
		return this.author;
	}

	public String hash() {
		return this.hash;
	}

	public String date() {
		return this.date;
	}

	public long dateAsDays() {
		return LocalDate.parse(date, DateTimeFormatter.ISO_DATE).toEpochDay();
	}

	public List<XRayFile> files() {
		return this.files;
	}

	public Commit withAuthor(String auth) {
		this.author = auth;
		return this;
	}

	public Commit withHash(String hash) {
		this.hash = hash;
		return this;
	}

	public Commit withDate(String date) {
		this.date = date;
		return this;
	}

	public Commit addFile(XRayFile file) {
		this.files.add(file);
		return this;
	}

	public static Commit fromLogLine(String line) {
		String[] data = line.split("--");
		Commit c = new Commit().withHash(data[1]).withDate(data[2]);
		if (data.length == 4) {
			c.withAuthor(data[3]);
		} else {
			c.withAuthor("Unknown");
		}

		return c;
	}

	@Override
	public String toString() {
		return "Commit [author=" + author + ", hash=" + hash + ", date=" + date + ", files=" + files + "]";
	}

	public static class CommitDateComparator implements Comparator<Commit> {
		@Override
		public int compare(Commit c1, Commit c2) {
			return LocalDate.of(1, 2, 2).compareTo(LocalDate.of(2, 2, 2));
		}
	}

}
