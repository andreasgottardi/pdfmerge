package goa.systems.pdfmerge.control.actions;

import java.io.File;

public abstract class PdfAction {

	protected String filename;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public abstract void apply(File dest);
}
