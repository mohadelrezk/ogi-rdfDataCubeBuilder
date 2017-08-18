package eu.opengov.cubebuilder.webservice;

import static spark.Spark.*;
import spark.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.io.*;
import java.nio.file.*;

import eu.opengov.cubebuilder.lqboperations.LqbQuerying;
import eu.opengov.cubebuilder.tarqlservices.TarqlFormulator;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 *
 * @author moh.adelrezk@gmail.com
 *
 */
public class OgiWebService {

    static String csvFilePath_in;
    static String qbSchmeaPath_in;
    static String serialization_in;
    static String serialization_out;
    static String qbFilePath_out;
    static String qbFileName_out;
    static TarqlFormulator tarqlformulator;
    static LqbQuerying lqbquerying;
    static String SparqlQuery;
    // static String fusekiPort;
    static String limit;
//	static ImplRESTapi implRESTapi;
    static JSONObject JsonResponse;

    public static void main(String[] args) {
        //enableDebugScreen();
//

        tarqlformulator = new TarqlFormulator();
        lqbquerying = new LqbQuerying();
//		implRESTapi = new ImplRESTapi();

        File uploadDir = new File("upload");
        uploadDir.mkdir(); // create the upload directory if it doesn't exist

        staticFiles.externalLocation("upload");

        get("/", "application/json", (request, response) -> {

            return "Welcome to OGI Cube Builder Webservice! !";
        });

//		 * (0) Building Linked Cubes
        //get 
        get("cubeBuilderAPI/cubeBuilderArgs", "application/json", (request,
                response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Content-Type", "application/json");
            if (request.queryParams("csv")
                    != null && request.queryParams("schema") != null && request.queryParams("serializationIn") != null && request.queryParams("serializationOut") != null && request.queryParams("qbPath") != null && request.queryParams("qbName") != null) {
                String path = new File("").getAbsolutePath().substring(0, new File("").getAbsolutePath().length() - 16);
                csvFilePath_in = path + request.queryParams("csv");
                qbSchmeaPath_in = path + request.queryParams("schema");
                serialization_in = request.queryParams("serializationIn");
                serialization_out = request.queryParams("serializationOut");
                qbFilePath_out = path + request.queryParams("qbPath");
                qbFileName_out = request.queryParams("qbName");

                System.out.println(csvFilePath_in + "\n" + qbSchmeaPath_in + "\n" + serialization_in + "\n" + serialization_out + "\n" + qbFilePath_out + "\n" + qbFileName_out);
                return run();
            } else {
//                return "";
                JsonResponse = new JSONObject();
                JsonResponse.put("success", false);
                JsonResponse.put("error message", "Please check missing or incorrect arguments!");
                return JsonResponse.toString();
            }

        });

        //post if a post request is used to call 
        post("cubeBuilderAPI/cubeBuilderArgs", "application/json", (request,
                response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Content-Type", "application/json");
            if (request.queryParams("csv")
                    != null && request.queryParams("schema") != null && request.queryParams("serializationIn") != null && request.queryParams("serializationOut") != null && request.queryParams("qbPath") != null && request.queryParams("qbName") != null) {
                String path = new File("").getAbsolutePath().substring(0, new File("").getAbsolutePath().length() - 16);
                csvFilePath_in = path + request.queryParams("csv");
                qbSchmeaPath_in = path + request.queryParams("schema");
                serialization_in = request.queryParams("serializationIn");
                serialization_out = request.queryParams("serializationOut");
                qbFilePath_out = path + request.queryParams("qbPath");
                qbFileName_out = request.queryParams("qbName");

                System.out.println(csvFilePath_in + "\n" + qbSchmeaPath_in + "\n" + serialization_in + "\n" + serialization_out + "\n" + qbFilePath_out + "\n" + qbFileName_out);
                return run();
            } else {
//                return "Please check missing or incorrect arguments!";
                JsonResponse = new JSONObject();
                JsonResponse.put("success", false);
                JsonResponse.put("error_message", "Please check missing or incorrect arguments!");
                return JsonResponse.toString();
            }
        });
        /* NOT IN USE AFTER JSON CUBE API IS READY
        
//		 * (1) List available Linked Cubes
         
        get("cubeQueryingAPI/listLqbs", "application/json",
                (request, response) -> {
                    response.header("Access-Control-Allow-Origin", "*");
                    response.header("Content-Type", "application/json");

                    // fusekiPort = request.queryParams("fuseki");
                    limit = request.queryParams("limit");

                    return lqbquerying.LqbQueryingForLqbSpaces(limit);
                });

        
//		 * (2) List Linked Cube metadata
         
        get("cubeQueryingAPI/LqbMeta", "application/json",
                (request, response) -> {
                    response.header("Access-Control-Allow-Origin", "*");
                    response.header("Content-Type", "application/json");
                    // String url = request.splat()[0];
                    String url = request.splat().toString();

                    marineDatasetURI = request.queryParams("dsuri");
                    // fusekiPort = request.queryParams("fuseki");
                    // limit=request.queryParams("limit");
                    System.out.println(marineDatasetURI);
                    System.out.println(request.raw().getRequestURL().toString()
                            + "---" + request.raw().getQueryString() + "----");

                    return lqbquerying.LqbQueryingForDimAndMeasures(request
                            .queryParams("dsuri"));
                });
        
//		 * (3) Retrieve Data of certain Linked Cube
         
        get("cubeQueryingAPI/listdataofLqb", "application/json", (request,
                response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Content-Type", "application/json");

            marineDatasetURI = request.queryParams("dsuri");
            // fusekiPort = request.queryParams("fuseki");
            limit = request.queryParams("limit");

            return lqbquerying.LqbQueryingForLqbData(marineDatasetURI,
                    limit);
        });

        
//		 * (4) Send Sparql Query over Linked Cubes
         
        get("cubeQueryingAPI/cubeQueryingArgs", "application/json", (request,
                response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Content-Type", "application/json");

            SparqlQuery = request.queryParams("query");
            // fusekiPort = request.queryParams("fuseki");
            // limit=request.queryParams("limit");

            return lqbquerying.LqbDirectQuerying(SparqlQuery);
        });

         */

// TODO: FILE UPLOAD for loose coupling
//		 * (5) FILE UPLOAD for loose coupling
        get("/upload", (req, res)
                -> "<form method='post' enctype='multipart/form-data'>" // note the enctype
                + "    <input type='file' name='uploaded_file' accept='.png'>" // make sure to call getPart using the same "name" in the post
                + "    <button>Upload picture</button>"
                + "</form>"
        );

        post("/upload", "multipart/form-data", (req, res) -> {

            Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");

            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/upload"));

            try (InputStream input = req.raw().getPart("uploaded_file").getInputStream()) { // getPart needs to use same "name" as input field in form
                Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            logInfo(req, tempFile);
            return "<h1>You uploaded this image:<h1><img src='" + tempFile.getFileName() + "'>";

        });

    }

    public static String run() {

        try {

            System.out.print("calling tarql service for conversion");
            tarqlformulator.tarqlAsLibraryExecution(csvFilePath_in, qbSchmeaPath_in, qbFilePath_out, qbFileName_out, serialization_in,
                    serialization_out);
//				return "Success: Cube Created check distination folder!";

            JsonResponse = new JSONObject();
            JsonResponse.put("success", true);
            return JsonResponse.toString();

        } catch (Exception ex) {
//			return "Error: " + ex.getMessage();

            JsonResponse = new JSONObject();
            JsonResponse.put("success", false);
            JsonResponse.put("error_message", ex.getMessage().toString());
            return JsonResponse.toString();
        }

    }
// TODO: Logging file
// methods used for logging

    private static void logInfo(Request req, Path tempFile) throws IOException, ServletException {
        System.out.println("Uploaded file '" + getFileName(req.raw().getPart("uploaded_file")) + "' saved as '" + tempFile.toAbsolutePath() + "'");
    }

    private static String getFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
