package goa.systems.pdfmerge;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import goa.systems.pdfmerge.configuration.CommandLineParser;
import goa.systems.pdfmerge.configuration.Configuration;

class AppTest {

	@Test
	void test() {

		CommandLineParser clp = new CommandLineParser();
		//@formatter:off
		String[] params = new String[] {
				"-s", "src/test/resources",
				// "-a", "e;e1.pdf;2",
				"-a", "f;e2.pdf;field1=Hugo1234",
				"-d", "src/test/resources",
				"-f", "goal.pdf"};
		//@formatter:on

		Configuration c = clp.parseCommandline(params);
		assertNotNull(c);

		App a = new App();
		assertTrue(a.execute(c));
	}

}
