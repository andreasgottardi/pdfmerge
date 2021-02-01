package goa.systems.pdfmerge.view;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import goa.systems.pdfmerge.control.Control;
import goa.systems.pdfmerge.model.Configuration;

public class View {

	private static final Logger logger = LoggerFactory.getLogger(View.class);

	private Control control;
	private Configuration configuration;

	private JFrame jframe;
	private Container contentpane;

	public View(Control control, Configuration configuration) {

		this.control = control;
		this.configuration = configuration;

		this.jframe = new JFrame();
		this.contentpane = this.jframe.getContentPane();
		this.contentpane.setPreferredSize(new Dimension(1280, 800));
		this.jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.jframe.pack();
		this.jframe.setVisible(true);
	}
}
