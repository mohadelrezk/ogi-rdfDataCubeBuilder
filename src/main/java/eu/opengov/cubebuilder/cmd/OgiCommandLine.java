package eu.opengov.cubebuilder.cmd;

import java.util.logging.Logger;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import eu.opengov.cubebuilder.desktop.OgiFront;
import eu.opengov.cubebuilder.tarqlservices.TarqlFormulator;
import eu.opengov.cubebuilder.webservice.OgiWebService;

/**
 *
 * @author moh.adelrezk@gmail.com
 *
 */
@Parameters(separators = ":")
public class OgiCommandLine {

    private static final Logger LOGGER = Logger.getLogger(OgiCommandLine.class
            .getName());
    public static JCommander Jcomm;
    public static TarqlFormulator tarqlformulator;

    public static OgiWebService webservice;
    public static OgiFront ogifront;
    public static OgiCommandLine commandline;

    @Parameter(names = {"--help", "-help", "-h"}, description = "Help", help = true)
    private boolean help;
    @Parameter(names = {"--csvFilePath", "-csv"}, description = "CSV input file Location and name")
    String csvFilePath_in;
    @Parameter(names = {"--dataSetSchema", "-schema"}, description = "Data Set Schema")
    String qbSchmeaPath_in;// Data Set schema
    @Parameter(names = {"--serialization_in", "-format_in"}, description = "Input Cube Schema serlization format (gussed if not provided)")
    String serialization_in = "turtle";// space if ttl
    @Parameter(names = {"--serialization_out", "-format_out"}, description = "Output Cube RDF serlization format (turtle or ntriples)")
    String serialization_out = "turtle";// space if ttl
    @Parameter(names = {"--qbFilePath_out", "-qbpath"}, description = "Cube output file Location")
    String qbFilePath_out;
    @Parameter(names = {"--qbFileName_out", "-qbN"}, description = "Cube output file name")
    String qbFileName_out;

    @Parameter(names = {"--run", "-run", "-r", "-play"}, description = "Which main class to Run!")
    String mainclass;

    // @DynamicParameter(names = "-D", description =
    // "Dynamic parameters go here")
    // public Map<String, String> dynamicParams = new HashMap<String, String>();
    public static void main(String[] args) {

        OgiCommandLine cmd = new OgiCommandLine();
        tarqlformulator = new TarqlFormulator();
//		webservice = new OgiWebService();
//		ogifront = new OgiFront();
//		commandline = new OgiCommandLine();

        Jcomm = new JCommander(cmd, args);
        Jcomm.setProgramName("RDF Data Cube Builder");
        try {
            cmd.run();
        } catch (Exception ex) {
            Jcomm.usage();

            System.out.println("Error:" + ex.getMessage());

        }

    }

    public void run() {

        String[] args = {"", ""};

        if (help == true) {
            Jcomm.usage();
        } else if (mainclass.equalsIgnoreCase("desktop")) {
            OgiFront.main(args);
        } else if (mainclass.equalsIgnoreCase("webservice")) {
            OgiWebService.main(args);
        } else if (mainclass.equalsIgnoreCase("cmd")) {
            if (csvFilePath_in != null && qbSchmeaPath_in != null && /*serialization_in != null
                    &&*/ serialization_out != null && qbFilePath_out != null && qbFileName_out != null) {
                try {

                    if (serialization_out.equalsIgnoreCase("turtle") || serialization_out.equalsIgnoreCase("ntriples")) {
                        tarqlformulator.tarqlAsLibraryExecution(csvFilePath_in, qbSchmeaPath_in, qbFilePath_out, qbFileName_out, serialization_in,
                                serialization_out);
                    } else {
                        System.out.println("Serilaization not supported : " + serialization_out);
                    }
                } catch (Exception ex) {
                    Jcomm.usage();
                    System.out.println("Error:" + ex.getMessage());
                }

                System.out.println("Check Cube output location:" + qbFilePath_out
                        + qbFileName_out);
            } else {
                System.out
                        .println("No cube building arguments supplied, check readme file!");
                Jcomm.usage();
            }
        }
    }
}
