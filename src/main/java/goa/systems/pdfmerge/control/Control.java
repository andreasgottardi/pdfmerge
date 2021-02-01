package goa.systems.pdfmerge.control;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import goa.systems.pdfmerge.control.actions.PdfAction;
import goa.systems.pdfmerge.model.Configuration;
import goa.systems.pdfmerge.view.View;

public class Control {

	private static final Logger logger = LoggerFactory.getLogger(Control.class);

	private Configuration configuration;

	private View view;

	public Control(Configuration configuration) {
		this.configuration = configuration;
		if (this.configuration.isIsgui()) {
			this.view = new View(this, this.configuration);
		}
	}

	/**
	 * Executes the defined PdfActions into a temporary working directory.
	 * 
	 * @return
	 */
	public boolean execute() {
		logger.debug("Applying {} actions.", this.configuration.getActions().size());
		File workdir = getTemporaryWorkdir();
		workdir.mkdirs();
		int i = 0;
		List<Integer> sorteditems = new ArrayList<>();
		for (PdfAction pa : this.configuration.getActions()) {
			pa.apply(new File(workdir, Integer.toString(i)));
			sorteditems.add(i);
			i++;
		}

		merge(sorteditems, workdir, new File(this.configuration.getDestdir(), this.configuration.getDestfilename()));
		deleteDir(workdir);
		return true;
	}

	/**
	 * Securely generates a working directory that does not exist.
	 * 
	 * @return File object that does not exist on disk.
	 */
	public File getTemporaryWorkdir() {
		File file = new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
		while (file.exists()) {
			file = new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
		}
		return file;
	}

	public void deleteWorkDir(File workdir) {
		try (Stream<Path> files = Files.walk(workdir.toPath())) {
			files.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::deleteOnExit);
		} catch (NoSuchFileException e) {
			logger.error("Directory {} does not exist.", workdir.getAbsolutePath(), e);
		} catch (IOException e) {
			logger.error("Deletion of workdir was not possible. See stack trace.", e);
		} catch (Exception e) {
			logger.error("An unexpected error happend. See stack trace.", e);
		}
	}

	public void deleteDir(File dir) {
		File[] files = dir.listFiles();
		if (files != null) {
			for (final File file : files) {
				deleteDir(file);
			}
		}
		try {
			Files.delete(dir.toPath());
		} catch (IOException e) {
			logger.error("Error deleting.", e);
		}
	}

	/**
	 * Merges generated documents together.
	 * 
	 * @param ids
	 * @param directory
	 * @param destination
	 */
	public void merge(List<Integer> ids, File directory, File destination) {

		File pdfparent = destination.getParentFile();

		if (!pdfparent.exists()) {
			pdfparent.mkdirs();
		}

		List<PDDocument> subdocs = new ArrayList<>();
		PDDocument doc = generateDoc();

		if (pdfparent.exists()) {
			try {
				for (Integer integer : ids) {
					PDDocument subdoc = PDDocument.load(new File(directory, integer.toString()));
					subdocs.add(subdoc);
					for (int i = 0; i < subdoc.getNumberOfPages(); i++) {
						PDPage sdpage = subdoc.getPages().get(i);
						doc.addPage(sdpage);
					}
				}
				doc.save(destination);
				subdocs.add(doc);
			} catch (IOException e) {
				logger.error("Error merging document.", e);
			} finally {

				for (PDDocument subdoc : subdocs) {
					try {
						logger.debug("Closing document {}.", subdoc);
						subdoc.close();
					} catch (IOException e) {
						logger.error("Error_occured.", e);
					}
				}
			}
		} else {
			logger.error("Directory {} not creatable.", destination.getAbsolutePath());
		}
	}

	private PDDocument generateDoc() {
		return new PDDocument();
	}
}
