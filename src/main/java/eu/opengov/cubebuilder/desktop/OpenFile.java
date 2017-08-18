package eu.opengov.cubebuilder.desktop;

import java.util.Scanner;

import javax.swing.JFileChooser;

import org.apache.commons.io.FilenameUtils;

import eu.opengov.cubebuilder.util.ExampleFileFilter;

public class OpenFile {

	// Declare Variable
	JFileChooser chooser;
	StringBuilder sb = new StringBuilder();
	ExampleFileFilter filter;
	// String filePath;
	String extension;

	public OpenFile() {

	

	}

	public void PickCsvFile() throws Exception {
		chooser = new JFileChooser();
		chooser.setBounds(295, 320, 149, 20);
		// // Note: source for ExampleFileFilter can be found in FileChooserDemo,
		// // under the demo/jfc directory in the JDK.
		filter = new ExampleFileFilter();
		filter.addExtension("csv");
		// filter.addExtension("gif");
		filter.setDescription("CSV Datasets");
		chooser.setFileFilter(filter);
		// int returnVal = chooser.showOpenDialog(getParent());//parent;
		// if(returnVal == JFileChooser.APPROVE_OPTION) {
		// System.out.println("You chose to open this file: " +
		// chooser.getSelectedFile().getName());
		// }

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

			// get the file contetnts
			// java.io.File file = chooser.getSelectedFile();
			// create a scanner for the file
			// Scanner input = new Scanner(file);
			// read text from file
			// while(input.hasNext()){
			// sb.append(input.nextLine());
			// sb.append("\n");;
			// }
			//
			// input.close();
			// }

			// get the file path
			sb.append(chooser.getSelectedFile());

		} else {
			sb.append("");
		}
	}

	public void PickRDFFile() throws Exception {
		chooser = new JFileChooser();
		chooser.setBounds(295, 320, 149, 20);
		// // Note: source for ExampleFileFilter can be found in FileChooserDemo,
		// // under the demo/jfc directory in the JDK.
		filter = new ExampleFileFilter();
		filter.addExtension("ttl");
		filter.addExtension("nt");
		filter.addExtension("nq");
		filter.addExtension("trig");
		filter.addExtension("rdf");
		filter.addExtension("owl");
		filter.addExtension("jsonld");
		filter.addExtension("trdf");
		filter.addExtension("rt");
		filter.addExtension("rj");
		filter.addExtension("trix");
		filter.addExtension("xml");
		filter.setDescription("RDF Schema Formats");
		chooser.setFileFilter(filter);
		// int returnVal = chooser.showOpenDialog(getParent());//parent;
		// if(returnVal == JFileChooser.APPROVE_OPTION) {
		// System.out.println("You chose to open this file: " +
		// chooser.getSelectedFile().getName());
		// }

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

			// get the file contetnts
			// java.io.File file = chooser.getSelectedFile();
			// create a scanner for the file
			// Scanner input = new Scanner(file);
			// read text from file
			// while(input.hasNext()){
			// sb.append(input.nextLine());
			// sb.append("\n");;
			// }
			//
			// input.close();
			// }

			// get the file path
			sb.append(chooser.getSelectedFile());
			extension = FilenameUtils.getExtension(chooser.getSelectedFile().toString());

		} else {
			sb.append("");
		}
	}

	public void PickFolder() throws Exception {
		chooser = new JFileChooser();
		chooser.setBounds(295, 320, 149, 20);
		
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

			// get the file contetnts
			// java.io.File file = chooser.getSelectedFile();
			// create a scanner for the file
			// Scanner input = new Scanner(file);
			// read text from file
			// while(input.hasNext()){
			// sb.append(input.nextLine());
			// sb.append("\n");;
			// }
			//
			// input.close();
			// }

			// get the file path
			sb.append(chooser.getSelectedFile());

		} else {
			sb.append("");
		}
	}

}