package goa.systems.pdfmerge;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

import goa.systems.pdfmerge.configuration.CommandLineParser;
import goa.systems.pdfmerge.configuration.Configuration;

class AppTest {

	@Test
	void test() {
		//@formatter:off
		String[] params = new String[] {
				"-s", "src/test/resources",
				"-a", "e;e1.pdf;2",
				"-a", "f;e2.pdf;field1=Hugo1234",
				"-d", new File(System.getProperty("user.home"),"Destkop").getAbsolutePath(),
				"-f", "goal.pdf"};
		//@formatter:on

		CommandLineParser clp = new CommandLineParser();
		Configuration c = clp.parseCommandline(params);
		assertNotNull(c);
		App a = new App();
		assertTrue(a.execute(c));
	}

	@Test
	void testTemporaryWorkDir() {
		App a = new App();
		File f = a.getTemporaryWorkdir();
		assertNotNull(f);
		assertFalse(f.exists());
	}

}
