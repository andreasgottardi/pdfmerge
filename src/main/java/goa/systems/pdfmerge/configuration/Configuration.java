package goa.systems.pdfmerge.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Configuration {

	private List<PdfAction> actions;

	private String sourcedir;
	private String destdir;
	private String destfilename;

	public Configuration() {
		this.actions = new ArrayList<>();
	}

	public List<PdfAction> getActions() {
		return actions;
	}

	public void setActions(List<PdfAction> actions) {
		this.actions = actions;
	}

	public void addAction(PdfAction pa) {
		this.actions.add(pa);
	}

	public String getSourcedir() {
		return sourcedir;
	}

	public void setSourcedir(String sourcedir) {
		this.sourcedir = sourcedir;
	}

	public String getDestdir() {
		return destdir;
	}

	public void setDestdir(String destdir) {
		this.destdir = destdir;
	}

	public String getDestfilename() {
		return destfilename;
	}

	public void setDestfilename(String destfilename) {
		this.destfilename = destfilename;
	}

	public void complete() {
		for (PdfAction pdfAction : actions) {
			pdfAction.setFilename(new File(sourcedir, pdfAction.getFilename()).getPath());
		}
	}
}
