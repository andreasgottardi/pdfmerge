package goa.systems.pdfmerge;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import goa.systems.pdfmerge.control.CommandLineParser;
import goa.systems.pdfmerge.model.Configuration;
import goa.systems.pdfmerge.model.JsonAction;
import goa.systems.pdfmerge.model.JsonConfiguration;

class JsonTest {

	private static Logger logger = LoggerFactory.getLogger(JsonTest.class);

	@Test
	void test() {
		JsonConfiguration c = new JsonConfiguration();
		c.setDestinationfile("goal.pdf");
		c.setDestinationfolder("atemptdir");
		c.setSourcefolder("examples");
		List<JsonAction> actions = new ArrayList<>();
		actions.add(new JsonAction("extract", "example1.pdf", "1"));
		actions.add(new JsonAction("fill", "example2.pdf", "key1=val1,key2=val2"));
		c.setActions(actions);
		String json = new Gson().toJson(c);
		assertNotNull(json);
		logger.debug("Json generated: {}", json);
	}

	@Test
	void testLoading() {
		Gson g = new Gson();
		JsonConfiguration jc = g.fromJson(new InputStreamReader(JsonTest.class.getResourceAsStream("/example.json")),
				JsonConfiguration.class);
		CommandLineParser clp = new CommandLineParser();
		Configuration c = clp.loadJsonBasedConfiguration(g.toJson(jc));
		assertNotNull(c);
	}
}
