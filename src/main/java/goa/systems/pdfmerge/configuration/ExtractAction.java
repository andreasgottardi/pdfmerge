package goa.systems.pdfmerge.configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtractAction extends PdfAction {

	private static final Logger logger = LoggerFactory.getLogger(ExtractAction.class);

	private String range;

	public String getPagenumber() {
		return range;
	}

	public void setPagenumber(String range) {
		this.range = range;
	}

	@Override
	public void apply(File dest) {
		File file = new File(this.filename);

		PDDocument document = null;
		PDDocument desiredpage = null;

		List<Integer> pagelist = generatePages();

		try (PDDocument destination = new PDDocument()) {
			document = PDDocument.load(file);
			Splitter splitter = new Splitter();
			List<PDDocument> pages = splitter.split(document);

			for (Integer pagenum : pagelist) {
				desiredpage = pages.get(pagenum);
				destination.addPage(desiredpage.getPage(0));
				destination.save(dest);
			}
			destination.save(dest);
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

	/**
	 * Takes the stored parameter and generates a array of pages.
	 * 
	 * @return Integer list of pages.
	 */
	public List<Integer> generatePages() {

		String[] pagedefs = this.range.split(",");

		Pattern singlepage = Pattern.compile("\\d+");
		Pattern pagerange = Pattern.compile("\\d+-\\d+");

		List<Integer> pages = new ArrayList<>();

		for (String def : pagedefs) {
			if (singlepage.matcher(def).matches()) {
				pages.add(Integer.parseInt(def) - 1);
			} else if (pagerange.matcher(def).matches()) {
				String[] r = def.split("-");
				int start = Integer.parseInt(r[0]);
				int end = Integer.parseInt(r[1]);
				for (int i = start; i <= end; i++) {
					pages.add(i - 1);
				}
			} else {
				logger.error("Error parsing definition {}", def);
			}
		}
		return pages;
	}
}
