package com.insidecoding.xray.model;

public class XRayFile {

	private String fullName;
	private String extension = "generic";
	private int added;
	private int removed;
	private String name;

	public String name() {
		return this.name;
	}

	public String fullName() {
		return this.fullName;
	}

	public String extension() {
		return this.extension;
	}

	public int added() {
		return this.added;
	}

	public int removed() {
		return this.removed;
	}

	public XRayFile withName(String name) {
		this.fullName = name;
		if (this.fullName.indexOf(".") != -1) {
			this.extension = this.fullName.substring(this.fullName.lastIndexOf(".") + 1);
		}
		if (this.fullName.indexOf("/") != -1) {
			this.name = this.fullName.substring(this.fullName.lastIndexOf("/"));
		} else {
			this.name = fullName;
		}
		return this;
	}

	public XRayFile withAdded(int added) {
		this.added = added;
		return this;
	}

	public XRayFile withRemoved(int removed) {
		this.removed = removed;
		return this;
	}

	public static XRayFile fromLogLine(String line) {
		String[] data = line.split("(\\s)+");
		return new XRayFile().withAdded(Integer.parseInt(data[0])).withRemoved(Integer.parseInt(data[1]))
				.withName(data[2]);
	}

	@Override
	public String toString() {
		return "XRayFile [name=" + fullName + ", extension=" + extension + ", added=" + added + ", removed=" + removed
				+ "]";
	}
}
