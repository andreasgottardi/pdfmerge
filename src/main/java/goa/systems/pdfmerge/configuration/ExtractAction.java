package goa.systems.pdfmerge.configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtractAction extends PdfAction {

	private static final Logger logger = LoggerFactory.getLogger(ExtractAction.class);

	private int pagenumber;

	public int getPagenumber() {
		return pagenumber;
	}

	public void setPagenumber(int pagenumber) {
		this.pagenumber = pagenumber;
	}

	@Override
	public void apply(File dest) {
		File file = new File(this.filename);

		PDDocument document = null;
		PDDocument desiredpage = null;

		try {
			document = PDDocument.load(file);
			Splitter splitter = new Splitter();
			List<PDDocument> pages = splitter.split(document);
			desiredpage = pages.get(pagenumber - 1);
			desiredpage.save(dest);
			for (PDDocument page : pages) {
				page.close();
			}
		} catch (IOException e) {
			logger.error("Error in pdf extraction.", e);
		} finally {
			if (document != null) {
				logger.debug("Closing document {}", document);
				try {
					document.close();
				} catch (IOException e) {
					logger.error("Error_occured.", e);
				}
			}
			if (desiredpage != null) {
				logger.debug("Closing document {}", document);
				try {
					desiredpage.close();
				} catch (IOException e) {
					logger.error("Error_occured.", e);
				}
			}
		}
	}
}
