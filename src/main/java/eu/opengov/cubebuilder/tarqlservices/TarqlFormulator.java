package eu.opengov.cubebuilder.tarqlservices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.deri.tarql.CSVOptions;
import org.deri.tarql.StreamingRDFWriter;
import org.deri.tarql.TarqlParser;
import org.deri.tarql.TarqlQuery;
import org.deri.tarql.TarqlQueryExecution;
import org.deri.tarql.TarqlQueryExecutionFactory;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

import com.hp.hpl.jena.graph.Triple;

import eu.opengov.cubebuilder.util.PropertyReader;

/**
 * Tarql query formulation, and RDF Cube Schema building functions.
 *
 * @author moh.adelrezk@gmail.com
 */
public class TarqlFormulator {

	/**
	 * String qbSchema, is used to temporary store RDF Cube Schema formulated during
	 * the run time, till it is saved in the corresponding schema file
	 * "exampledatasetname.ttl.schema".
	 */
	// String qbSchema = "";
	// PropertyReader pr = new PropertyReader();
	public TarqlQueryBuilder tqb;
	String out_ext="ttl";

	public TarqlFormulator() {

		tqb = new TarqlQueryBuilder();

	}

	/**
	 * This function/method in used for merging and storing the dataset's
	 * corresponding Cube Schema to a schema file "example: datasetname.ttl.schema",
	 * and the observations file "example: datasetname.ttl.observations"
	 *
	 * @author moh.adelrezk@gmail.com
	 */
	private void mergingSchemaFileWithObservationFile(String qbFilePath_out, String qbFileName_out, String schemaPath) {
		/**
		 * String MergedFilePath, the final corresponding dataset's RDF Cube path and
		 * file name "example: {base.dir}/output/datasetname.ttl"
		 */
		String MergedFilePath = qbFilePath_out +"/" +qbFileName_out;

		String ObservationFilePath = qbFilePath_out + "/"+qbFileName_out + ".observations."+out_ext;
		// ".nt" and ".ttl" should revisit and make it non static

		/**
		 * Merging steps
		 */
		BufferedWriter merged = null;
		BufferedReader schema = null;
		BufferedReader observations = null;
		try {
			merged = new BufferedWriter(new FileWriter(MergedFilePath));
			schema = new BufferedReader(new FileReader(schemaPath));
			observations = new BufferedReader(new FileReader(ObservationFilePath));
			String line = null;

			while ((line = schema.readLine()) != null) {
				merged.write(line);
				merged.write("\n");
			}
			while ((line = observations.readLine()) != null) {
				merged.write(line);
				merged.write("\n");
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());

		} finally {
			if (merged != null) {
				try {
					merged.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
			if (schema != null) {
				try {
					schema.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}

		}
		if (observations != null) {
			try {
				observations.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public void tarqlAsLibraryExecution(String csvFilePath_in, String qbSchmeaPath_in, String qbFilePath_out,
			String qbFileName_out, String serialization_in, String serialization_out) throws IOException {

		/**
		 * String workingDir, used to relatively locate tarql and run tarql queries
		 *
		 */
		// String workingDir = System.getProperty("user.dir");
		CSVOptions options = new CSVOptions();
		String delimiter = "";
		String encoding = "";
		String escapeChar = "";
		String quoteChar = "";
		String headerRow = "";

		// Delimiter
		if (StringUtils.isNotBlank(delimiter)) {
			if (delimiter.equals("tab")) {
				options.setDelimiter('\t');
			} else {
				options.setDelimiter(delimiter.charAt(0));
			}
		}

		// Encoding
		if (StringUtils.isNotBlank(encoding) && (!encoding.equals("Autodetect"))) {
			options.setEncoding(encoding);
		}

		// Escape Char
		if (StringUtils.isNotBlank(escapeChar) && (!encoding.equals("None"))) {
			options.setEscapeChar(escapeChar.charAt(0));
		}

		// Quote Char
		if (StringUtils.isNotBlank(quoteChar)) {
			options.setQuoteChar(quoteChar.charAt(0));
		}

		// Header Row (default TRUE)
		if (StringUtils.isNotBlank(headerRow) && headerRow.equals("no")) {
			options.setColumnNamesInFirstRow(false);
		}
		/*
		 * Tarql execution stages
		 */
		TarqlQuery tq = new TarqlQuery();
		/**
		 * swiching between pilot pre-stored tarql construct queries and costume tarql
		 * construct queries
		 *
		 */

		// String pilot_tarql_construct = pr.getPropValues(schema + "_Query");
		String pilot_tarql_construct = tqb.get_tarql_query(qbSchmeaPath_in, serialization_in);
		System.out.println(pilot_tarql_construct);
		tq = new TarqlParser(new StringReader(pilot_tarql_construct), null).getResult();

		TarqlQueryExecution ex = TarqlQueryExecutionFactory.create(tq, csvFilePath_in, options);
		// TarqlQueryExecution ex = TarqlQueryExecutionFactory.
		Iterator<Triple> triples = ex.execTriples();

		ValueFactory factory = new ValueFactoryImpl();

		/**
		 * Printing String observation value to the new observations file
		 */
//		setting up output extensions
		if (serialization_out.equals("ntriples")) 
			out_ext="nt";
		if (serialization_out.equals("turtle")) 
			out_ext="ttl";
		
		try (OutputStream File = new FileOutputStream(qbFilePath_out+"/"+ qbFileName_out + ".observations."+out_ext)) {

			while (triples.hasNext()) {
				StreamingRDFWriter writer = new StreamingRDFWriter(File, triples);
				if (serialization_out.equalsIgnoreCase("ntriples")) {
					writer.writeNTriples();
				} else {
					writer.writeTurtle(tq.getPrologue().getBaseURI(), tq.getPrologue().getPrefixMapping());
				}

			}
			System.out.println("Waiting For Observation Capture!");
		} catch (Exception e) {

			/**
			 * Logging will be added
			 *
			 */
			System.out.println("Warning: Somthing went wrong at Observation Capture Stage!");
			System.out.println(e.getMessage());

		}
		/**
		 * creating the corresponding qbschema String
		 */
		System.out.println("Creating Cube Schema!");

		/**
		 * Creating the corresponding RDF Cube file, by merging .schema file and
		 * .observation file
		 */
		String ext = FilenameUtils.getExtension(qbSchmeaPath_in);
		System.out.println(ext);
		if (ext.equalsIgnoreCase(out_ext)) {
			System.out.println("Creating Full Cube !");
		
			mergingSchemaFileWithObservationFile(qbFilePath_out, qbFileName_out, qbSchmeaPath_in);
		}
	}

}
