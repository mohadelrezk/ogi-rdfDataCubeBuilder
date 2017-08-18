package eu.opengov.cubebuilder.tarqlservices;

import com.hp.hpl.jena.rdf.model.Model;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Resource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.stream.*;
import java.io.*;

/**
 * Tarql query formulation, and RDF Cube Schema building functions.
 *
 * @author moh.adelrezk@gmail.com
 */
public class TarqlQueryBuilder {

    public static OntModel model;
// public static Model model;
    public static String tarql_query;

    public TarqlQueryBuilder() {

    }

    public static void main(String[] args) {

        /**
         * A:
         */
        model = read_cube_schema("/Users/mohade/Desktop/TEMP-WORKSPACE/ogi-cubebuilder/src/main/resources/IWaveBNetowrk_spectral_output.ttl", "TURTLE");
//        get_cube_prefixes(model);
        //query_Cube_Schema_for_DSet(model);
        //query_Cube_Schema_for_ComponentProperties(model, ""); // default is all componentProperties
        //query_Cube_Schema_for_ComponentProperties(model, "Dimensions");
        //query_Cube_Schema_for_ComponentProperties(model, "Measures");
        //query_Cube_Schema_for_ComponentProperties(model, "Attributes");

        /**
         * B:
         */
        tarql_query = build_tarql_construct_query(model);

    }

    //public static Model read_cube_schema(String cube_schema_file_path, String rdf_serialization){
    public static OntModel read_cube_schema(String cube_schema_file_path, String rdf_serialization) {
        model = ModelFactory.createOntologyModel();
        try{
        //create  new Model
//        model = ModelFactory.createDefaultModel();
        
        //Read Schema
        //Use FileManager to read the file and add it to the Jena model
//        FileManager.get().readModel(model, cube_schema_file_path, rdf_serialization);// "/Users/mohade/Desktop/TEMP-WORKSPACE/ogi-cubebuilder/src/main/resources/IWaveBNetowrk_spectral_output.ttl"
//        Read a file of RDF into a model. Guesses the syntax of the file based on filename extension, defaulting to RDF/XML
        FileManager.get().readModel(model, cube_schema_file_path);
        //model.read("/Users/mohade/Desktop/TEMP-WORKSPACE/ogi-cubebuilder/src/main/resources/IWaveBNetowrk_spectral_output.ttl") ;
        //model.read("data.foo", "TURTLE") ;
        //model.write(System.out, "TURTLE");
       
        }catch(Exception ex){
            System.out.println(ex.getMessage().toString());
        
        
        }
         return model;
    }

    public static List<QuerySolution> query_Cube_Schema_for_ComponentProperties(Model model_to_query_on, String Component_Type) {
        String ComponentProperty = "qb:ComponentProperty";

        if (Component_Type != null) {
            if (Component_Type == "Measures") {
                ComponentProperty = "qb:MeasureProperty";
            }
            if (Component_Type == "Attributes") {
                ComponentProperty = "qb:AttributeProperty";
            }
            if (Component_Type == "Dimensions") {
                ComponentProperty = "qb:DimensionProperty";
            }
        }

        String LqbQueryingForMetaData_queryCommand = "PREFIX qb:  <http://purl.org/linked-data/cube#> \n"
                + "PREFIX OGI:  <http://ogi.eu/#> \n"
                + "PREFIX dct:      <http://purl.org/dc/terms/> \n"
                + "PREFIX rdfs:     <http://www.w3.org/2000/01/rdf-schema#> \n"
                + "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> \n"
                + "SELECT  ?Component_URI ?Label ?DataType ?OriginalName\n"
                + "WHERE{  \n"
                + "?Component_URI a " + ComponentProperty + ". \n"
                + "?Component_URI rdfs:label ?Label. "
                + "?Component_URI rdfs:range ?DataType. "
                + "?Component_URI dbpedia-owl:originalName ?OriginalName. "
                + "\n" + "} \n";

        Query qry = QueryFactory.create(LqbQueryingForMetaData_queryCommand);
        QueryExecution qe = QueryExecutionFactory.create(qry, model_to_query_on);
        ResultSet rs = qe.execSelect();

        //Model results = rs.
        //String s = ResultSetFormatter.asText(rs);
        //System.out.print(s);
        List<QuerySolution> qs_ls_comp = new ArrayList();

        qs_ls_comp = ResultSetFormatter.toList(rs);
        qe.close();
        return qs_ls_comp;

        /*
        //Model results = rs.
        System.out.println(ResultSetFormatter.asText(rs));
        //System.out.println(resultsAsString);

        // write to a ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ResultSetFormatter.outputAsJSON(outputStream, rs);

        // and turn that into a String
        String json = new String(outputStream.toByteArray());

        //System.out.println(json);
        
        JSONObject jsonObj = new JSONObject(json);
        JSONObject results = (JSONObject) jsonObj.get("results");
        JSONArray jsonArr = (JSONArray) results.get("bindings");
        List<JSONObject> jsonItems = IntStream.range(0, jsonArr.length())
                .mapToObj(index -> (JSONObject) jsonArr.get(index))
                .collect(Collectors.toList());
        System.out.println("items");
        jsonItems.forEach(item -> {
            System.out.println(item);
            System.out.println("items");
        });
            
        /**
            IntStream.range(0, array.length())
            .mapToObj(index -> (JSONObject) array.get(index))
            .forEach(item -> {
               System.out.println(item);
            });
         */
    }

    public static List<QuerySolution> query_Cube_Schema_for_DSet(Model model_to_query_on) {

        String LqbQueryingForDataSet_queryCommand = "PREFIX qb:  <http://purl.org/linked-data/cube#> \n"
                + "PREFIX OGI:  <http://ogi.eu/#> \n"
                + "PREFIX dct:      <http://purl.org/dc/terms/> \n"
                + "PREFIX rdfs:     <http://www.w3.org/2000/01/rdf-schema#> \n"
                + "SELECT ?Ds_URI \n"
                + "WHERE{  \n"
                + "?Ds_URI a qb:DataSet. \n"
                + " \n" + "} \n";

        Query qry = QueryFactory.create(LqbQueryingForDataSet_queryCommand);
        QueryExecution qe = QueryExecutionFactory.create(qry, model_to_query_on);
        //ResultSet rs = ResultSetFactory.copyResults(qe.execSelect());
        ResultSet rs = qe.execSelect();
        //String s = ResultSetFormatter.asText(rs);
        //System.out.print(s);
        List<QuerySolution> qs_ls_ds = new ArrayList();
        qs_ls_ds = ResultSetFormatter.toList(rs);
        /*
        for (QuerySolution binding : qs_ls ){
            System.out.println("Ok");
            
            Resource subj = (Resource) binding.get("Ds_URI");
            String temp = subj.getURI().toString();
            System.out.println(temp);
            //Object ds_uri = org.apache.commons.lang.SerializationUtils.clone(temp);
            
        }
         */
        qe.close();

        return qs_ls_ds;

    }

    public static String get_cube_prefixes(Model model_to_get_from) {
        Map<String, String> prefixes = new HashMap<String, String>();
        StringBuilder tarql_construct_query_prefixes = new StringBuilder();
        String line = "";
        String base_ns = "";

        // get prefixes
        prefixes = model_to_get_from.getNsPrefixMap();

        //System.out.println("base:" +base_ns);
        // put prefixes into construct query
        for (Map.Entry<String, String> prefix : prefixes.entrySet()) {
            line = "PREFIX " + prefix.getKey() + ": <" + prefix.getValue() + "> \n";
            // System.out.print(line);
            tarql_construct_query_prefixes.append(line);
        }

        return tarql_construct_query_prefixes.toString();

    }

    public static String build_tarql_construct_query(Model model_to_construct_query_based_on) {
        StringBuilder tarql_construct_query = new StringBuilder();
        //set prefixes
        String prefixes = get_cube_prefixes(model_to_construct_query_based_on);
        tarql_construct_query.append(prefixes);

        // set construct statement header (fixed)
        tarql_construct_query.append("CONSTRUCT{ \n");
        //set observation line (fixed)
        tarql_construct_query.append("?observation a qb:Observation; \n");
        //set dataset property
        List<QuerySolution> datasets;
        datasets = query_Cube_Schema_for_DSet(model_to_construct_query_based_on);
        String dataSet_URI = "http://www.opengovintelligence.eu/CHECKBUILDER";

        //System.out.println("Ok");
        for (QuerySolution binding : datasets) {
            Resource URI = (Resource) binding.get("Ds_URI");
            dataSet_URI = URI.getURI().toString();
        }
        tarql_construct_query.append("qb:dataSet <" + dataSet_URI + ">; \n");
        //set dimensions properties

        List<QuerySolution> components;
        components = query_Cube_Schema_for_ComponentProperties(model_to_construct_query_based_on, ""); // default is all components
        for (QuerySolution binding : components) {

            //Resource URI 
            String uri = binding.get("Component_URI").toString();
            //String uri = URI.getURI().toString();
            //Resource original_name
            String Orig_name = binding.get("OriginalName").toString();
            //String Orig_name = original_name.getURI().toString();
            tarql_construct_query.append("<" + uri + ">" + " ?" + Orig_name + "_tempValueWaitingBINDStmt" + "; \n");

        }
        // set Where statement header (fixed)
        tarql_construct_query.append(". \n } \n WHERE { \n");

        //set random observation ids BIND
        String base_ns = model_to_construct_query_based_on.getNsPrefixURI("ogi");
        //  System.out.println(base_ns);
        tarql_construct_query.append("BIND (uri(CONCAT('" + base_ns + "observations_',StrUUID())) AS ?observation).\n");

        //Setting BIND statemnt to define data types for components
        for (QuerySolution binding : components) {

            //Resource URI 
            String xsdType = binding.get("DataType").toString();
            //String uri = URI.getURI().toString();
            //Resource original_name
            String Orig_name = binding.get("OriginalName").toString();
            //String Orig_name = original_name.getURI().toString();
            tarql_construct_query.append("BIND (<" + xsdType + ">(?" + Orig_name + ") AS ?" + Orig_name + "_tempValueWaitingBINDStmt" + ").\n"
            );
        }

        // closing the Where Clause
        tarql_construct_query.append("}\n");

//        System.out.print(tarql_construct_query.toString());
        return tarql_construct_query.toString();

    }

    public String get_tarql_query(String schema_path, String rdf_serlization) {

        /**
         * A:
         */
        model = read_cube_schema(schema_path, "TURTLE");
//        get_cube_prefixes(model);
        //query_Cube_Schema_for_DSet(model);
        //query_Cube_Schema_for_ComponentProperties(model, ""); // default is all componentProperties
        //query_Cube_Schema_for_ComponentProperties(model, "Dimensions");
        //query_Cube_Schema_for_ComponentProperties(model, "Measures");
        //query_Cube_Schema_for_ComponentProperties(model, "Attributes");

        /**
         * B:
         */
        tarql_query = build_tarql_construct_query(model);
        return tarql_query;

    }
}
