package goa.systems.pdfmerge.model;

public class JsonAction {

	private String action;
	private String filename;
	private String parameters;

	public JsonAction(String action, String filename, String parameters) {
		this.action = action;
		this.filename = filename;
		this.parameters = parameters;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
}
