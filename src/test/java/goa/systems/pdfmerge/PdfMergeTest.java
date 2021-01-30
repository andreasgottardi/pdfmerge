package goa.systems.pdfmerge;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

import goa.systems.pdfmerge.control.CommandLineParser;
import goa.systems.pdfmerge.control.Control;
import goa.systems.pdfmerge.model.Configuration;

class PdfMergeTest {

	@Test
	void test() {
		//@formatter:off
		String[] params = new String[] {
				"-s", "src/test/resources",
				"-a", "e;e1.pdf;2",
				"-a", "f;e2.pdf;name=CONV04711,year=2021",
				"-d", new File(System.getProperty("user.home"),"Destkop").getAbsolutePath(),
				"-f", "goal.pdf"};
		//@formatter:on

		CommandLineParser clp = new CommandLineParser();
		Configuration c = clp.parseCommandline(params);
		assertNotNull(c);
		Control a = new Control(c);
		assertTrue(a.execute());
	}

	@Test
	void testTemporaryWorkDir() {
		Control a = new Control(null);
		File f = a.getTemporaryWorkdir();
		assertNotNull(f);
		assertFalse(f.exists());
	}

}
