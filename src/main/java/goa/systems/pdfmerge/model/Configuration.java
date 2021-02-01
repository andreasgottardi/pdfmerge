package goa.systems.pdfmerge.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import goa.systems.pdfmerge.control.actions.PdfAction;

public class Configuration {

	private List<PdfAction> actions;

	private String sourcedir;
	private String destdir;
	private String destfilename;

	private boolean isgui;

	public Configuration() {
		this.actions = new ArrayList<>();
		this.sourcedir = "";
		this.destdir = "";
		this.destfilename = "";
		this.isgui = false;
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

	public boolean isIsgui() {
		return isgui;
	}

	public void setIsgui(boolean isgui) {
		this.isgui = isgui;
	}
}
