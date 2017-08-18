package eu.opengov.cubebuilder.desktop;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;
import javax.swing.filechooser.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import eu.opengov.cubebuilder.tarqlservices.TarqlFormulator;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.UIManager;
import java.awt.Font;

import eu.opengov.cubebuilder.desktop.OpenFile;
import javax.swing.JList;

import java.awt.Container;

/**
 * 
 * @author moh.adelrezk@gmail.com
 */
public class OgiFront extends javax.swing.JFrame {
	private JFrame frame;

	/*
	 * Declare formulator
	 */
	public TarqlFormulator tarqlformulator;

	// Components
	private JTextArea dataSet;
	private JTextArea schema;
	private JTextArea outputDir;

	private JButton clear;
	private JButton exit;
	private JTextField rdfCubeName;

	private JList rdfFormatlist;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// TODO remove prints after testing phases

		// System.out.println("new File(\"\").getAbsolutePath()" + "==>"
		// + new File("").getAbsolutePath());
		// System.out.println("System.getProperty('user.home')" + "==>"
		// + System.getProperty("user.home"));
		// System.out.println("System.getProperty('user.dir')" + "==>"
		// + System.getProperty("user.dir"));
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OgiFront OGIApp = new OgiFront();
					OGIApp.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Create the application.
	 */
	public OgiFront() {
		initialize();
	}

	private void initialize() {

		/*
		 * initiate formulator on the constructor
		 */

		frame = new JFrame();
		frame.setBounds(100, 100, 450, 450);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		// frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// contentPane = new JPanel();
		// contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		// setContentPane(contentPane);

		tarqlformulator = new TarqlFormulator();
		// schema = "embty";
		// serialization = "turtle";

		JLabel appTitle = new JLabel("OGI Cube Builder Desktop Application");
		appTitle.setFont(new Font("Lucida Grande", Font.BOLD, 22));
		appTitle.setForeground(new Color(238, 130, 238));
		appTitle.setBackground(UIManager.getColor("Label.background"));
		appTitle.setBounds(13, 6, 431, 35);
		frame.getContentPane().add(appTitle);

		JButton generateCube = new JButton("Generate Cube");

		generateCube.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // Getting the user's entred marine dataset
				// name

				if (dataSet.getText() != null && outputDir.getText() != null && rdfCubeName.getText() != null
						&& schema.getText() != null && rdfFormatlist.getSelectedValue().toString() != null) {
					System.out.print(dataSet.getText() + "\n" + outputDir.getText() + "\n" + rdfCubeName.getText()
							+ "\n" + schema.getText() + "\n" + rdfFormatlist.getSelectedValue().toString());
					String extension = "";
					if (rdfFormatlist.getSelectedValue().toString().equalsIgnoreCase("turtle"))
						extension = ".ttl";
					if (rdfFormatlist.getSelectedValue().toString().equalsIgnoreCase("ntriples"))
						extension = ".nt";

					try {

						tarqlformulator.tarqlAsLibraryExecution(dataSet.getText(),  URLDecoder.decode(schema.getText(), "UTF-8"),
								outputDir.getText(), rdfCubeName.getText() + extension, "TURTLE",
								rdfFormatlist.getSelectedValue().toString());

					} catch (Exception e1) {

						System.out.println(e1.getMessage());
					}

					JOptionPane.showMessageDialog(null,
							"Transformation is in process check Cube destination folder!" + "\r\n " + "Source:"
									+ dataSet.getText() + "\r\n " + "Destination:" + outputDir.getText()
									+ rdfCubeName.getText() + "\r\n" + rdfFormatlist.getSelectedValue().toString());

				}
			}

		});

		generateCube.setBounds(152, 354, 146, 25);
		frame.getContentPane().add(generateCube);

		clear = new JButton("Clear");
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dataSet.setText("");
				schema.setText("");
				outputDir.setText("");
				rdfCubeName.setText("");
				// textField_4.setText("");
			}
		});

		clear.setBounds(13, 356, 117, 20);
		frame.getContentPane().add(clear);

		exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Container f = exit.getParent();
				do
					f = f.getParent();
				while (!(f instanceof JFrame));
				((JFrame) f).dispose();
			}

		});
		exit.setBounds(310, 354, 117, 25);
		frame.getContentPane().add(exit);

		dataSet = new JTextArea();
		dataSet.setToolTipText("No Space Allowed in File Path");
		dataSet.setBounds(13, 80, 223, 26);
		dataSet.setColumns(10);
		dataSet.setText("");
		frame.getContentPane().add(dataSet);

		JButton btnChooseDataset = new JButton("Choose Dataset");
		btnChooseDataset.setToolTipText("No Space Allowed in File Path");
		btnChooseDataset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				OpenFile of = new OpenFile();
				try {

					of.PickCsvFile();

				} catch (Exception ex) {

					ex.printStackTrace();
				}

				dataSet.setText(of.sb.toString());

			}
		});
		btnChooseDataset.setBounds(295, 79, 149, 29);
		frame.getContentPane().add(btnChooseDataset);

		schema = new JTextArea();
		schema.setToolTipText("No Space Allowed in File Path");
		schema.setBounds(13, 120, 223, 26);
		schema.setColumns(10);
		schema.setText("");
		frame.getContentPane().add(schema);

		JButton btnChooseSchma = new JButton("Choose Schema");
		btnChooseSchma.setToolTipText("No Space Allowed in File Path");
		btnChooseSchma.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				OpenFile of = new OpenFile();
				try {

					of.PickRDFFile();

				} catch (Exception ex) {

					ex.printStackTrace();
				}

				schema.setText(of.sb.toString());

			}
		});
		btnChooseSchma.setBounds(295, 119, 149, 29);
		frame.getContentPane().add(btnChooseSchma);

		outputDir = new JTextArea();
		outputDir.setBounds(13, 158, 223, 26);
		frame.getContentPane().add(outputDir);

		JButton btnOutputDir = new JButton("Choose Output Dir.");
		btnOutputDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				OpenFile of = new OpenFile();
				try {

					of.PickFolder();

				} catch (Exception ex) {

					ex.printStackTrace();
				}

				outputDir.setText(of.sb.toString());

			}

		});
		btnOutputDir.setBounds(295, 157, 149, 29);
		frame.getContentPane().add(btnOutputDir);

		rdfCubeName = new JTextField();
		rdfCubeName.setBounds(13, 199, 223, 26);
		frame.getContentPane().add(rdfCubeName);
		rdfCubeName.setColumns(10);

		JLabel lblRdfDataCube = new JLabel("RDF Data Cube Name");
		lblRdfDataCube.setBounds(295, 198, 149, 29);
		frame.getContentPane().add(lblRdfDataCube);

		JLabel lblRdfDataCube_1 = new JLabel("RDF Data Cube Format");
		lblRdfDataCube_1.setBounds(295, 236, 149, 29);
		frame.getContentPane().add(lblRdfDataCube_1);
		// Data to select from
		DefaultListModel rdfFormates = new DefaultListModel();
		rdfFormates.addElement("turtle");
		rdfFormates.addElement("ntriples");

		rdfFormatlist = new JList(rdfFormates);
		rdfFormatlist.setBounds(13, 237, 223, 26);
		rdfFormatlist.setLayoutOrientation(JList.VERTICAL);
		rdfFormatlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane listScroller = new JScrollPane(rdfFormatlist);
		// listScroller.setPreferredSize(new Dimension(250, 80));
		listScroller.setBounds(13, 237, 223, 40);

		frame.getContentPane().add(listScroller);

	}
}
