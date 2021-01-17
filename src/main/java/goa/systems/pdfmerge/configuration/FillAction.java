package goa.systems.pdfmerge.configuration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FillAction extends PdfAction {

	private static final Logger logger = LoggerFactory.getLogger(FillAction.class);

	private Map<String, String> values;

	public Map<String, String> getValues() {
		return values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}

	@Override
	public void apply(File dest) {
		File file = new File(this.filename);
		PDDocument document = null;
		try {
			document = PDDocument.load(file);
			PDDocumentCatalog docCatalog = document.getDocumentCatalog();
			PDAcroForm acroForm = docCatalog.getAcroForm();
			for (Map.Entry<String, String> entry : this.values.entrySet()) {
				PDField field = acroForm.getField(entry.getKey());
				if (field != null) {
					field.setValue(entry.getValue());
				}
			}
			document.save(dest);
		} catch (IOException e) {
			logger.error("Error in pdf filling.", e);
		} finally {
			if (document != null) {
				logger.debug("Closing document {}", document);
				try {
					document.close();
				} catch (IOException e) {
					logger.error("Error_occured.", e);
				}
			}
		}
	}
}
