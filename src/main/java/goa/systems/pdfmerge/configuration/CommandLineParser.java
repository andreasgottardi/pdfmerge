package goa.systems.pdfmerge.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;

public class CommandLineParser {

	private static final Logger logger = LoggerFactory.getLogger(CommandLineParser.class);

	public Configuration parseCommandline(String[] args) {
		Configuration c = new Configuration();
		for (int i = 0; i < args.length; i++) {
			if ("-a".compareTo(args[i]) == 0) {
				c.addAction(loadCommandLineAction(args[i + 1]));
			} else if ("-s".compareTo(args[i]) == 0) {
				c.setSourcedir(args[i + 1]);
			} else if ("-d".compareTo(args[i]) == 0) {
				c.setDestdir(args[i + 1]);
			} else if ("-f".compareTo(args[i]) == 0) {
				c.setDestfilename(args[i + 1]);
			}
		}
		c.complete();
		return c;
	}

	public PdfAction loadCommandLineAction(String parameter) {

		Iterable<String> elems = Splitter.on(Pattern.compile("(?<!\\\\);")).split(parameter);
		int i = 0;
		String actiontype = "";
		String filename = "";
		String parameters = "";

		for (String string : elems) {
			string = string.replace("\\;", ";");
			switch (i) {
			case 0:
				actiontype = string;
				break;
			case 1:
				filename = string;
				break;

			case 2:
				parameters = string;
				break;

			default:
				break;
			}
			i++;
		}

		PdfAction pa = null;

		switch (actiontype) {
		case "f":
			FillAction fa = new FillAction();
			fa.setFilename(filename);
			fa.setValues(parseParameters(parameters));
			pa = fa;
			break;
		case "e":
			ExtractAction ea = new ExtractAction();
			ea.setFilename(filename);
			ea.setPagenumber(Integer.parseInt(parameters));
			pa = ea;
			break;
		default:
			logger.debug("Action type {} unknown.", actiontype);
			break;
		}

		return pa;
	}

	public Map<String, String> parseParameters(String paramlist) {
		Map<String, String> cleanedvalues = new HashMap<>();
		Map<String, String> result = Splitter.on(Pattern.compile("(?<!\\\\),")).trimResults()
				.withKeyValueSeparator(Splitter.on(Pattern.compile("(?<!\\\\)=")).trimResults()).split(paramlist);
		for (Map.Entry<String, String> entry : result.entrySet()) {
			String key = entry.getKey();
			String val = entry.getValue();
			cleanedvalues.put(key.replace("\\,", ","), val.replace("\\,", ","));
		}
		return cleanedvalues;
	}
}
