package goa.systems.pdfmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import goa.systems.pdfmerge.configuration.ExtractAction;

class RangeTest {

	@Test
	void test() {

		ExtractAction ea = new ExtractAction();
		ea.setRange("1,2,7-10,4");
		List<Integer> pages = ea.generatePages();
		assertEquals(7, pages.size());
	}
}
