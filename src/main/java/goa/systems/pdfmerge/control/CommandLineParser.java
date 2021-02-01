package goa.systems.pdfmerge.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import goa.systems.pdfmerge.control.actions.ExtractAction;
import goa.systems.pdfmerge.control.actions.FillAction;
import goa.systems.pdfmerge.control.actions.PdfAction;
import goa.systems.pdfmerge.model.Configuration;
import goa.systems.pdfmerge.model.JsonAction;
import goa.systems.pdfmerge.model.JsonConfiguration;

public class CommandLineParser {

	private static final Logger logger = LoggerFactory.getLogger(CommandLineParser.class);

	public Configuration parseCommandline(String[] args) {
		Configuration c = null;

		if (args.length == 0 || (args.length == 1 && "-g".compareTo(args[0]) == 0)) {
			c = loadGuiConfiguration();
		} else if (args.length == 2 && "-j".compareTo(args[0]) == 0) {
			c = loadJsonBasedConfiguration(args);
		} else {
			c = loadParamBasedConfiguration(args);
		}
		if (c != null) {
			c.complete();
			return c;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param args
	 * @return null, if not all values are set. Else configuration object.
	 */
	private Configuration loadParamBasedConfiguration(String[] args) {

		Configuration c = new Configuration();
		boolean[] crtlvals = { false, false, false, false };
		for (int i = 0; i < args.length; i++) {

			if ("-s".compareTo(args[i]) == 0) {
				c.setSourcedir(args[i + 1]);
				crtlvals[0] = true;
			} else if ("-a".compareTo(args[i]) == 0) {
				c.addAction(loadCommandLineAction(args[i + 1]));
				crtlvals[1] = true;
			} else if ("-d".compareTo(args[i]) == 0) {
				c.setDestdir(args[i + 1]);
				crtlvals[2] = true;
			} else if ("-f".compareTo(args[i]) == 0) {
				c.setDestfilename(args[i + 1]);
				crtlvals[3] = true;
			} else if ("-g".compareTo(args[i]) == 0) {
				c.setDestfilename(args[i + 1]);
				crtlvals[3] = true;
			}
		}
		return requiredValuesSetOnCommandline(crtlvals) ? c : null;
	}

	private boolean requiredValuesSetOnCommandline(boolean[] values) {
		boolean retval = true;
		for (boolean b : values) {
			retval &= b;
		}
		return retval;
	}

	/**
	 * 
	 * 
	 * @param args
	 * @return null in case of error. Else configuration.
	 */
	public Configuration loadJsonBasedConfiguration(JsonConfiguration jc) {

		Configuration c = new Configuration();

		try {
			c.setDestdir(jc.getDestinationfolder());
			c.setDestfilename(jc.getDestinationfile());
			c.setSourcedir(jc.getSourcefolder());

			for (JsonAction ja : jc.getActions()) {
				PdfAction pa;
				switch (ja.getAction()) {
				case "fill":
					FillAction fa = new FillAction();
					fa.setValues(parseParameters(ja.getParameters()));
					pa = fa;
					break;
				case "extract":
					ExtractAction ea = new ExtractAction();
					ea.setPagenumber(Integer.parseInt(ja.getParameters()));
					pa = ea;
					break;
				default:
					pa = null;
					break;
				}
				if (pa != null) {
					pa.setFilename(ja.getFilename());
				}
				c.addAction(pa);
			}

		} catch (JsonSyntaxException | JsonIOException e) {
			logger.error("Err", e);
			return null;
		}
		return c;
	}

	/**
	 * 
	 * 
	 * @param args
	 * @return null in case of error. Else configuration.
	 */
	public Configuration loadJsonBasedConfiguration(String[] args) {
		try {
			JsonConfiguration jc = new Gson().fromJson(new InputStreamReader(new FileInputStream(new File(args[1]))),
					JsonConfiguration.class);
			return loadJsonBasedConfiguration(jc);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			logger.error("Err", e);
			return null;
		}

	}

	/**
	 * 
	 * 
	 * @param args
	 * @return null in case of error. Else configuration.
	 */
	public Configuration loadJsonBasedConfiguration(String json) {
		try {
			JsonConfiguration jc = new Gson().fromJson(json, JsonConfiguration.class);
			return loadJsonBasedConfiguration(jc);
		} catch (JsonSyntaxException | JsonIOException e) {
			logger.error("Err", e);
			return null;
		}

	}

	/**
	 * 
	 * 
	 * @param args
	 * @return null in case of error. Else configuration.
	 */
	public Configuration loadGuiConfiguration() {
		Configuration c = new Configuration();
		c.setIsgui(true);
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
