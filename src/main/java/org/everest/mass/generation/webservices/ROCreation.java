package org.everest.mass.generation.webservices;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;


@Path("/")
public class ROCreation {
	private static Logger logger = LogManager.getLogger(ROCreation.class);
	@Path("/")
	@GET
	@Produces("application/json")
	public Response creatorws(@Context HttpHeaders headers) throws Exception{
		Map<String, Cookie> map = headers.getCookies();
		List <String> list = headers.getRequestHeader("Referer");
		String query ="";
		for (String referer : list){
			query = referer.replaceAll("http://everest.expertsystemlab.com/solr/EverEst/browse","")
					       .replaceAll("http://everest.expertsystemlab.com/browse","")
							.replaceAll("http://172.16.32.89:8983/solr/EverEst/browse","")
							.replaceAll("\\?", "");
		}	
//		return Response.status(200).entity(query).build();
		return Response.seeOther(URI.create("http://everest.expertsystemlab.com/rocreation/auth/"+query)).build();
	}
	
	@Path("/auth/{query}")
	@GET
	@Produces("application/json")
	public Response auth(@PathParam("query") String valueQuery) throws Exception{
		String access = "a3a0224a-2f34-415a-b0a8-0969e2129a38";
		String html = "<html>"+
		"<head>"
		+ "<meta charset=\"utf-8\">    "
		+ "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">    "
		+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">    "
		+ "<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->"
		+ "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\"> "		
		+ "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css\" integrity=\"sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp\" crossorigin=\"anonymous\"> "		
		+ "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script>"						
		+ "<style> "
		+ "        .jumbotron {position: relative; overflow: hidden; background-color:#337ab7; color:#fff;}"
		+ "        .jumbotron h2 { position: relative; z-index: 2; 	margin-left: 170px;} "
		+ "        .jumbotron p { position: relative; z-index: 2; 	margin-left: 170px;} "
		+ "        .jumbotron img {position: absolute;left: 0;top: 0;width: 100%; opacity: 0.3;} "
		+ "</style> "		
		+ "<title> Aggregated Research Object</title> " 		
		+ "</head>" 
		+ "<body>" 		 	
		+ " <div class=\"jumbotron\"> "
		+ "   <h2>Aggregated research object</h2> "
		+ "   <p>Wizard to create a new research object by aggregating existing research objects</p>"
		+ "   <img src=\"http://everest.expertsystemlab.com/home/earth-background-2.jpg\"> "
		+ " </div>"		
		+ " <div class=\"container\">"	
		+ " <form action=\"http://everest.expertsystemlab.com/rocreation/creator\" method=\"get\">"		
		+ "   <div class=\"row\" style=\"width:97.5%;\">"
		+ "		<div class=\"col-sm-6 col-md-12\">"	 		
		+ "        <div class=\"form-group\">" 
		+ "           <label for=\"inputTitle\">Research object title:</label>"
		+ "           <input type=\"text\" name=\"title\" class=\"form-control\" id=\"inputTitle\" placeholder=\"title...\"/>" 
		+ "           <label for=\"inputTitle\">Research object description:</label>"
		+ "           <input type=\"text\" name=\"description\" class=\"form-control\" id=\"inputDescription\" placeholder=\"description...\"/>" 
		+ "        </div> "
		+ "     </div> "
		+ "   </div> "		
		+ "  <div class=\"row\" style=\"width:97.5%;\">"
		+ "		<div class=\"col-sm-6 col-md-12\">"		
		+"        <div><b>Select the research objects to aggregate:</b><br></div> "
		+ "     </div>"
		+ "  </div>" ;
		
		valueQuery = valueQuery.replaceAll("q=&", "")
				 .replaceAll("^&fq=", "")
				 .replaceAll("^&q=", "")
				 .replaceAll("fq=", "")
				 .replaceAll("q=", "")
				 .replaceAll("\\?", "")
				 .replaceAll("&", " AND ")
				 .replaceAll("%3A", ":")
				 .replaceAll("%22","\"")
				 .replaceAll("\\+", " ");
		
		SolrClient solr = new HttpSolrClient.Builder("http://everest.expertsystemlab.com/solr/EverEst").build();
		SolrQuery query = new SolrQuery();

		query.setQuery(valueQuery);
		query.setFacet(true);
		query.setRows(1000);	//número de resultados máximo
		QueryResponse rs = solr.query(query);
		SolrDocumentList results_list = rs.getResults();
		for (SolrDocument results : results_list){
			html = html + " <div class=\"row\" style=\"width:97.5%;\">"
					+ "		   <div class=\"col-sm-6 col-md-12\">" 
					+ "           <div class=\"checkbox\"> "
					+ "              <label> "
					+ "                 <input type=\"checkbox\" name=\"ro\" value=\""+results.getFieldValue("id").toString()
												.replaceAll("http://sandbox.rohub.org/rodl/ROs/", "")+"\" checked=\"checked\"> "
				    +                     results.getFieldValue("id").toString()												
			        +"               </label> ";					
			        
//					+ "             <div style=\"font-size:18px\"><input type=\"checkbox\" name=\"ro\" value=\""+results.getFieldValue("id").toString()
//					.replaceAll("http://sandbox.rohub.org/rodl/ROs/", "")+"\" checked=\"checked\">"+results.getFieldValue("id").toString()+"<br></div>";
			if(results.getFieldValue("title") != null){
				html = html + "<br><b>title</b>: "+results.getFieldValue("title")+". ";
			}
			if(results.getFieldValue("description") != null){
				if (results.getFieldValue("description").toString().length()<=160){
					html = html + "<br><b>desc</b>: <i>"+results.getFieldValue("description")+"</i>";
				}
				else{
					html = html + "<br><b>desc</b>: <i>"+results.getFieldValue("description").toString().substring(0, 159)+"...</i>";
				}
			}
			html = html +"    </div><!-- /Checkbox -->"
					+ "     </div><!-- /.col-lg-6 --> "
					+"  </div><!-- /.row --> " ;
//			html = html + "<hr>";
		}
		html = html + " <div class=\"row\" style=\"width:97.5%;\">"
					+ "		   <div class=\"col-sm-6 col-md-12\">" 
					+ "           <b>Select the type of aggregation:</b><br> "
					+ "           <div class=\"checkbox\"> "
					+ "              <label> "
					+ "                <input type=\"checkbox\" name=\"relations\" value=\"true\" checked=\"checked\">Create a new Research Object with relations to the Research Objects above<br>"
		            + "                <input type=\"checkbox\" name=\"content\" value=\"true\" checked=\"checked\">Create a new Research Object with the content of the Research Objects above<br>"
		            + "              </label> "
		            + "       </div><!-- /Checkbox -->"
					+ "     </div><!-- /.col-lg-6 --> "
					+ "  </div><!-- /.row --> "
					+ " <div class=\"row\" style=\"width:97.5%;\">"
					+ "		   <div class=\"col-sm-6 col-md-12\">"
					+ " <input type=\"submit\" class=\"btn btn-default\" value=\"Generate\" />" 
					+ "</form>" 
					+ "     </div><!-- /.col-lg-6 --> "
					+ "  </div><!-- /.row --> "
					+ "  </div><!-- /.container --> "
					+ "<!-- jQuery (necessary for Bootstrap's JavaScript plugins) --> " 
					+ " <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js\"></script>" 
					+ " <!-- Include all compiled plugins (below), or include individual files as needed -->" 
					+ " <script src=\"js/bootstrap.min.js\"></script>" 
					+ "</body>" 
					+"</html>";
		
		new MediaType();
		MediaType type = MediaType.TEXT_HTML_TYPE;
		return Response.status(200).cookie(new NewCookie("accessToken", access)).entity(html).type(type).build();
	}
	
	@Path("/creator")
	@GET
	@Produces("application/json")
	public Response creatorRO(@Context HttpHeaders headers, @QueryParam("title") String title, @QueryParam("description") String description, 
															@QueryParam("ro") List <String> chosen, @QueryParam("relations") boolean relations, 
															@QueryParam("content") boolean content) throws Exception{
		logger.info("Empiezo en /creator");
				
		//Cookie cookieAccess = map.get("accessToken");
		String access = "Bearer a3a0224a-2f34-415a-b0a8-0969e2129a38";
		logger.info(access);
		
		String uriROHUB = "http://sandbox.rohub.org/rodl/ROs/";
		String aux = title.toLowerCase().replaceAll(" ", "-");
		String roID = URLEncoder.encode(aux,"UTF-8");
		
		String html = "";
		new MediaType();
		MediaType type = MediaType.TEXT_HTML_TYPE;
		
		if(content || relations){
			String roURI=createRO(uriROHUB,roID,access);
			logger.info("new RO uri {}", roURI);
			
			new Thread(new Runnable() {
			    public void run() {
			    	try {
			    		setTitle(roURI, "ann.rdf", title, description, access);
			    		if (content){
				    		process_content(uriROHUB, roURI, access, chosen);
				    	}
				    	if (relations){
				    		process_relations(uriROHUB, roURI, access, chosen);
				    	}
						HttpClient httpclient = HttpClients.createDefault();
						HttpPost request = new HttpPost(new URIBuilder("http://172.16.32.89:8080/ro/enrichment").build());
						request.setHeader("content-type", "application/json");
						request.setHeader("cache-control", "no-cache");
						String body = "{ \"ro_uri\":\""+roURI+"\"," + 
										"\"callback\":\"http://betax.rohub.com/rodl-sandbox/service/callback\","+
										"\"nonce\":\"2a2cfbd5-b32a-453b-9762-cb6f103ee34f\" }";
						StringEntity se = new StringEntity(body);
						request.setEntity(se);
						
						httpclient.execute(request);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error(e);
						e.printStackTrace();
					}
			    }
			}).start();
			
			html = "<html>"+
					"<head><title>Aggregated RO</title></head>" +
					"<body>" +
					"The petition has been sent. Your aggregated Research Object will be available soon in <a href=\""+roURI+"\">"+roURI+"</a>"+
					". Check the activity section in rohub for updates."+ 
					"Please click <a href=\"http://everest.expertsystemlab.com/browse\">here</a> to bring you back to the browser"+
					"</body>";
		}
		else {
			html = "<html>"+
					"<head><title>Aggregated RO</title></head>" +
					"<body>" +
					"Error in the petition. Please select at least one type of aggregation to process the new Research Object."+
					"</body>";
		}
		return Response.status(200).entity(html).type(type).build();
	}
	
	@Path("/list-ingv")
	@GET
	@Produces("application/json")
	public Response list_ingv() throws SolrServerException, IOException{
		SolrClient solr = new HttpSolrClient.Builder("http://172.16.32.89:8983/solr/EverEst").build();
		SolrQuery query = new SolrQuery();
		query.setQuery("report AND (flegrei OR campani OR Ischia OR Vesuvio)");
		query.setFacet(true);
		query.setRows(1000);
		query.add("sort", "title desc");
		QueryResponse response = solr.query(query);
		Map<String, String>res = new HashMap<String,String>();
		Iterator<SolrDocument> it = response.getResults().iterator();
		while (it.hasNext()){
			SolrDocument results = it.next();
			String id = results.getFieldValue("id").toString();
			String title = results.getFieldValue("title").toString();
			res.put(id, title);
		}
		String json = "{\"items\":[";
		TreeMap <String, String> map = sortByValues(res);
		for (Entry<String, String> entry : map.entrySet()) {
			json = json + "{\"id\":\"" + entry.getKey().toString() + "\",";
		    json = json + "\"title\":\"" + entry.getValue().toString();
			json = json + "\"},";
		}
		json = json.substring(0, json.length()-1);
		json = json + "]}";
		return Response.status(200).entity(json).build();
	}
	
	@Path("/list-cnr")
	@GET
	@Produces("application/json")
	public Response list_cnr() throws SolrServerException, IOException{
		SolrClient solr = new HttpSolrClient.Builder("http://172.16.32.89:8983/solr/EverEst").build();
		SolrQuery query = new SolrQuery();
		query.setQuery("cnr-biblio-resource AND creator:\"http://everest.psnc.pl/users/generation_service/\"");
		query.setFacet(true);
		query.setRows(1000);
		query.add("sort", "title desc");
		QueryResponse response = solr.query(query);
		Map<String, String>res = new HashMap<String,String>();
		Iterator<SolrDocument> it = response.getResults().iterator();
		while (it.hasNext()){
			SolrDocument results = it.next();
			String id = results.getFieldValue("id").toString();
			String description = "";
			if (results.getFieldValue("description") != null){
				 description = results.getFieldValue("description").toString();
			}
			res.put(description, id);
		}
		String json = "{\"items\":[";
		TreeMap <String, String> map = sortByValues(res);
		for (Entry<String, String> entry : map.entrySet()) {
			json = json + "{\"id\":\"" + entry.getValue().toString() + "\",";
		    json = json + "\"description\":\"" + entry.getKey().toString();
			json = json + "\"},";
		}
		json = json.substring(0, json.length()-1);
		json = json + "]}";
		return Response.status(200).entity(json).build();
	}
	
	private void process_content (String uriROHUB, String roURI, String access, List <String> chosen) throws Exception{
		SolrClient solr = new HttpSolrClient.Builder("http://everest.expertsystemlab.com/solr/EverEst").build();
		SolrQuery query = new SolrQuery();

		List <String> ros = new ArrayList<String>();
		Set <String> resources = new HashSet<String>();
		List <String> annotations = new ArrayList<String>();
		String valueQuery = "";
		for (String ro:chosen){
			valueQuery = valueQuery + "id:\""+uriROHUB+ro+"\" OR ";
		}
		
		valueQuery=valueQuery.replaceAll(" OR $","");
		logger.info("valueQuery:{}",valueQuery);
		query.setQuery(valueQuery);
		query.setFacet(true);
		query.setRows(1000);	//número de resultados máximo
		QueryResponse rs = solr.query(query);
		SolrDocumentList results_list = rs.getResults();

		for (SolrDocument results : results_list){
			if (chosen.contains(results.getFieldValue("id").toString().replaceAll(uriROHUB,""))){
				logger.info("Agregando el Research Object: " + results.getFieldValue("id").toString());
				ros.add(results.getFieldValue("id").toString());
			}
		}
		//pending add annotations relating the current RO with the aggregated ROs
		
		resources = list_of_resources(ros);
		for(String resource: resources){
			logger.info("Adding resource: " + resource);
			InputStream input_resource = getResource(resource);
			String[]resourceArray = resource.split("/");
			String resourceTitle = resourceArray[resourceArray.length-1];
			addResource(input_resource, resourceTitle, roURI, access);
		}
		annotations = list_of_annotations(ros);
		int i = 1;
		for(String ann: annotations){
			logger.info("Adding annotation: " + ann);
			InputStream input_resource = getResource(ann);
			String target = RDFReader(ann);
			String body = getBody(input_resource,roURI);
			logger.info(body);
			if (target != ""){
				String[]resourceArray = ann.split("/");
				String nameAnnotation = resourceArray[resourceArray.length-1];
				nameAnnotation = nameAnnotation.replaceAll("\\.", i+"\\.");
				annotateResource(body, nameAnnotation, roURI, access, target);
				i++;
			}
		}
	}
	public <K  extends Comparable<K>, V extends Comparable<V>> TreeMap<K, V> sortByValues(final Map<K, V> map) {
	    Comparator<K> valueComparator = new Comparator<K>() {
	        public int compare(K k1, K k2) {
	            int compare = map.get(k1).compareTo(map.get(k2));
	            if (compare == 0) {
	                return k2.compareTo(k1); // <- To sort alphabetically
	            } else {
	                return compare;
	            }
	        }
	    };
	    TreeMap<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
	    sortedByValues.putAll(map);
	    return sortedByValues;
	}
	
	private void process_relations(String uriROHUB, String roURI, String access, List<String>chosen) throws URISyntaxException, ClientProtocolException, IOException{
		logger.info("Procesamos las relaciones");
    	HttpClient httpclient = HttpClients.createDefault();
		URIBuilder builder = new URIBuilder(roURI);
		URI uri = builder.build();
		HttpPost request = new HttpPost(uri);
		
		request.setHeader("content-type", "application/rdf+xml");
		request.setHeader("slug", "relations");
		request.setHeader("authorization", access);
		request.setHeader("Link", "<"+roURI+">; rel=\"http://purl.org/ao/annotatesResource\"");

		
		String body = "<?xml version=\"1.0\"?>"+
		                    "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:prov=\"http://www.w3.org/ns/prov#\" xmlns:dct=\"http://purl.org/dc/terms/\" xmlns:j.0=\"http://www.w3.org/ns/prov#\">" +
		                    "<rdf:Description rdf:about=\""+roURI+"\">";
		for (String ro : chosen){
						body= body + 
		                    "<j.0:hadOriginalSource rdf:resource=\""+uriROHUB+ro+"\"/>";
		}
		body = body +
		                    "</rdf:Description>"+
		                    "</rdf:RDF>";
		
		request.setEntity(new StringEntity(body));
		HttpResponse response = httpclient.execute(request);
		
		HttpEntity entity = response.getEntity();
		InputStream input = entity.getContent();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		String line = null;
		while((line = in.readLine()) != null) {
			logger.info(line);
		}	
	}
	
	private static InputStream getResource(String resource) throws UnsupportedOperationException, IOException, URISyntaxException {
		logger.info("Getting resource: {}", resource);
		HttpClient httpclient = HttpClients.createDefault();
		URIBuilder builder = new URIBuilder(resource);
		URI uri = builder.build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = httpclient.execute(request);

		HttpEntity entity = response.getEntity();
		InputStream input = entity.getContent();
		return input;
	}
	
	private static Set <String> list_of_resources(List<String>urls) throws Exception{
		List <String> resources = list_of_annotations (urls);
		Set <String> res = new HashSet<String>();
		for (String resource: resources){
			logger.info("Leyendo el rdf: {}", resource);
			String target = RDFReader(resource);
			if(target != "" && !res.contains(target)){
				logger.info("Guardamos en lista de resources: {}",target);
				res.add(target);
			}
		}
		return res;
	}
	
	private static List <String> list_of_annotations(List<String>urls) throws Exception{
		String sparqlEndpoint = "http://sandbox.rohub.org/rodl/sparql";
		List <String> resources = new ArrayList<String>();
		List <String> aux = new ArrayList<String>();
		for (String url: urls){
			System.out.println("Extrayendo recursos de: "+url);
			String sparqlQuery = "SELECT ?ab " +
					"WHERE{<"+url+"> <http://www.openarchives.org/ore/terms/aggregates> ?res. " +
					"?res a <http://purl.org/ao/Annotation> . " +
					"?res <http://purl.org/ao/body> ?ab. " +
					"?res <http://purl.org/wf4ever/ro#annotatesAggregatedResource> ?agg. " +
					"FILTER (?agg != <"+url+">) " +
					"FILTER regex(str(?ab),\"^"+url+".*\")}";
			Query query = QueryFactory.create(sparqlQuery, Syntax.syntaxSPARQL) ;
			QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint,query); 
			ResultSet results = qexec.execSelect();

			// print out the predicate, subject and object of each statement
			while (results.hasNext()) {
				QuerySolution sol = results.next();
				RDFNode object = sol.get("ab");
				String res = object.asResource().getURI();

				aux.add(res);
			}
		}
		
		for (String aux_res: aux){
			String sparqlQuery = "SELECT ?res WHERE{"
					+ "?ann <http://purl.org/ao/body> <" + aux_res + "> ."
					+ "?ann <http://purl.org/wf4ever/ro#annotatesAggregatedResource> ?res ."
					+ "?res a <http://purl.org/wf4ever/ro#ResearchObject>}";
			Query query = QueryFactory.create(sparqlQuery, Syntax.syntaxSPARQL) ;
			QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint,query); 
			ResultSet results = qexec.execSelect();
			// If the resource is not a RO then addeed. 
			if (!results.hasNext()) {
				resources.add(aux_res);
				logger.info(aux_res);
			}
		}
		return resources;
	}
	
	public static String RDFReader(String url_RDF) throws Exception{
		HttpClient httpclient = HttpClients.createDefault();
		URIBuilder builder = new URIBuilder(url_RDF);
		URI uri = builder.build();		
		HttpGet request = new HttpGet(uri);
		HttpResponse response = httpclient.execute(request);
		String resultado="";
		if(response.getStatusLine().getStatusCode() != 200){
			throw new Exception("No resource found.  Annotation Body: "+ url_RDF);
		}
			HttpEntity entity = response.getEntity();
			InputStream input = entity.getContent();
		
			Model model = ModelFactory.createDefaultModel();
			try{model.read(input,"RDF/XML");}
			catch (Exception e) {System.out.println("Erroraco");}

			StmtIterator it = model.listStatements();
			
			String Sketch = "http://purl.org/wf4ever/roterms#Sketch";
			while(it.hasNext()){
				Statement statement = it.next();
				RDFNode res = statement.getObject();
				if (res.isResource()){
					if(!res.asResource().toString().equals(Sketch)){
						resultado = statement.getSubject().getURI().toString();	
					}
				}
				if (res.isLiteral()){
					resultado = statement.getSubject().getURI().toString();	
				}
			}
			model.close();
		return resultado;
	}
	
	public static String getBody(InputStream input_resource,String roURI) throws Exception{
		String body = "<?xml version=\"1.0\"?>"+
                "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:prov=\"http://www.w3.org/ns/prov#\" xmlns:dct=\"http://purl.org/dc/terms/\" >";

			Model model = ModelFactory.createDefaultModel();
			try{model.read(input_resource,"RDF/XML");}
			catch (Exception e) {System.out.println("Erroraco");}

			StmtIterator it = model.listStatements();
			
			String sujeto = "";
			String predicado = "";
			String objeto = "";
			
			while(it.hasNext()){
				Statement statement = it.next();
				sujeto = statement.getSubject().getURI().toString();
				predicado = statement.getPredicate().getURI().toString()
															.replaceAll("http://www.w3.org/1999/02/22-rdf-syntax-ns#","rdf:")
															.replaceAll("http://www.w3.org/ns/prov#", "prov:")
															.replaceAll("http://purl.org/dc/terms/", "dct:");
				RDFNode objeto_nodo = statement.getObject();
				if (objeto_nodo.isLiteral()){
					objeto = "rdf:datatype=\""+objeto_nodo.asLiteral().getDatatype().getURI()+"\">"+objeto_nodo.asLiteral().getValue()+"</"+predicado+">";
				}
				if (objeto_nodo.isResource()){
					objeto = "rdf:resource=\""+objeto_nodo.asResource().toString()+"\"/>";
				}
			}
			String[]targetArray = sujeto.split("/");
			String targetTitle = targetArray[targetArray.length-1];
			 body = body + "<rdf:Description rdf:about=\""+roURI+targetTitle+"\">"+
             "<"+predicado+" "+ objeto +
             "</rdf:Description>"+
             "</rdf:RDF>";	
		
			model.close();
			return body;
	}
	
	public static String createRO(String uriROHUB, String roID, String access) throws URISyntaxException, ClientProtocolException, IOException{
		logger.info("Entramos a createRO");
    	HttpClient httpclient = HttpClients.createDefault();
		URIBuilder builder = new URIBuilder(uriROHUB);
		URI uri = builder.build();
		HttpPost request = new HttpPost(uri);
		request.setHeader("content-type", "text/plain");
		request.setHeader("accept", "text/turtle");
		request.setHeader("slug", roID);
		request.setHeader("authorization", access);
		HttpResponse response = httpclient.execute(request);

		if (response.getStatusLine().getStatusCode()!=HttpStatus.SC_CREATED ){
	    	 logger.error("Error {} creating RO {} ",response.getStatusLine().getStatusCode(), roID );
	    }		

		Header[] headers = response.getAllHeaders();
		for (Header header : headers) {
			if (header.getName().equals("Location"))
				return header.getValue();
		}

		return null;
		
//		HttpEntity entity = response.getEntity();
//		InputStream input = entity.getContent();
//		
//		BufferedReader in = new BufferedReader(new InputStreamReader(input));
//		String line = null;
//		while((line = in.readLine()) != null) {
//			logger.info(line);
//		}
	}
	
	public static void setTitle(String roURI, String nameAnnotation, String title, String description, String access) throws URISyntaxException, ClientProtocolException, IOException{
		logger.info("Entramos en setTitle");
    	HttpClient httpclient = HttpClients.createDefault();
		URIBuilder builder = new URIBuilder(roURI);
		URI uri = builder.build();
		HttpPost request = new HttpPost(uri);
		
		request.setHeader("content-type", "application/rdf+xml");
		request.setHeader("slug", nameAnnotation);
		request.setHeader("authorization", access);
		request.setHeader("Link", "<"+roURI+">; rel=\"http://purl.org/ao/annotatesResource\"");

		
		String body = "<?xml version=\"1.0\"?>"+
		                    "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:prov=\"http://www.w3.org/ns/prov#\" xmlns:dct=\"http://purl.org/dc/terms/\" >" +
		                    "<rdf:Description rdf:about=\""+roURI+"\">"+
		                   // "<rdf:type rdf:resource=\"http://purl.org/dc/terms/BibliographicResource\"/>" +
		                    "<dct:title>"+title+"</dct:title>" +
		                    "<dct:description>"+description+"</dct:description>" +
		                    "</rdf:Description>"+
		                    "</rdf:RDF>";
		
		request.setEntity(new StringEntity(body));
		HttpResponse response = httpclient.execute(request);
		
		HttpEntity entity = response.getEntity();
		InputStream input = entity.getContent();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		String line = null;
		while((line = in.readLine()) != null) {
			logger.info(line);
		}	
	}
	
	public static void addResource(InputStream input_resource, String resourceTitle, String roURI, String access) throws URISyntaxException, ClientProtocolException, IOException{
		logger.info("Entramos en addResource");
		HttpEntity recurso = MultipartEntityBuilder.create()
				.addBinaryBody("myfile", 
						input_resource)
				.build();

		HttpClient httpclient = HttpClients.createDefault();
		URIBuilder builder = new URIBuilder(roURI);
		URI uri = builder.build();
		HttpPost request = new HttpPost(uri);
		request.setHeader("accept", "text/turtle");
		//request.setHeader("slug", URLEncoder.encode(resourceTitle,"utf-8"));
		request.setHeader("slug", resourceTitle);
		request.setHeader("authorization", access);
		request.setEntity(recurso);
		HttpResponse response = httpclient.execute(request);

		HttpEntity entity = response.getEntity();
		InputStream input = entity.getContent();

		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		String line = null;
		while((line = in.readLine()) != null) {
			logger.info(line);
		}
	}
	
public static void annotateResource(String body, String nameAnnotation, String roURI, String access, String target) throws URISyntaxException, ClientProtocolException, IOException{
		logger.info(target);
		logger.info(nameAnnotation);
		String[]targetArray = target.split("/");
		String targetTitle = targetArray[targetArray.length-1];
		logger.info(targetTitle);
		
		HttpClient httpclient = HttpClients.createDefault();
		URIBuilder builder = new URIBuilder(roURI);
		URI uri = builder.build();
		HttpPost request = new HttpPost(uri);
		
		request.setHeader("Link", "<"+roURI+targetTitle+">; rel=\"http://purl.org/ao/annotatesResource\"");
		request.setHeader("content-type", "application/rdf+xml");
		request.setHeader("slug", nameAnnotation);
		request.setHeader("authorization", access);
		
		/*String body = "";
		 try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input_resource))) {
	            body = buffer.lines().collect(Collectors.joining("\n")).replaceAll("<rdf:Description rdf:about=\"*\">", "<rdf:Description rdf:about=\""+uriROHUB+roID+"/"+targetTitle+"\">");
	        }*/
		request.setEntity(new StringEntity(body));
		HttpResponse response = httpclient.execute(request);
		
		HttpEntity entity = response.getEntity();
		InputStream input = entity.getContent();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		String line = null;
		while((line = in.readLine()) != null) {
		  logger.info(line);
		}
	}
}
