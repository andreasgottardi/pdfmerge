package goa.systems.pdfmerge.model;

import java.util.List;

public class JsonConfiguration {

	private String sourcefolder;
	private String destinationfolder;
	private String destinationfile;
	private List<JsonAction> actions;

	public String getSourcefolder() {
		return sourcefolder;
	}

	public void setSourcefolder(String sourcefolder) {
		this.sourcefolder = sourcefolder;
	}

	public String getDestinationfolder() {
		return destinationfolder;
	}

	public void setDestinationfolder(String destinationfolder) {
		this.destinationfolder = destinationfolder;
	}

	public String getDestinationfile() {
		return destinationfile;
	}

	public void setDestinationfile(String destinationfile) {
		this.destinationfile = destinationfile;
	}

	public List<JsonAction> getActions() {
		return actions;
	}

	public void setActions(List<JsonAction> actions) {
		this.actions = actions;
	}
}
