/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package goa.systems.pdfmerge;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import goa.systems.pdfmerge.configuration.CommandLineParser;
import goa.systems.pdfmerge.configuration.Configuration;

class CommandLineParserTest {

	@Test
	void testSplit() {
		CommandLineParser clp = new CommandLineParser();
		String extractaction = "f;filen\\;a,=me.pdf;asdf=csdf,csdf=bsfd";
		assertNull(clp.loadCommandLineAction(extractaction));
	}

	@Test
	void testConfigLoader() {

		CommandLineParser clp = new CommandLineParser();
		//@formatter:off
		String[] params = new String[] {
				"-s", "src/test/resources",
				// "-a", "e;e1.pdf;2",
				"-a", "f;e2.pdf;field1=value1",
				"-d", "src/test/resources",
				"-f", "goal.pdf"};
		//@formatter:on

		Configuration c = clp.parseCommandline(params);
		assertNotNull(c);
	}
}
