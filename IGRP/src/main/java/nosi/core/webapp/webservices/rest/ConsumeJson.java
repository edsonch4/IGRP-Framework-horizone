package nosi.core.webapp.webservices.rest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author Isaias.Nunes
 *
 */
public class ConsumeJson {
	public String getObjectFromJson(String url, String json_data) throws IOException {
		
		byte[] postData       = json_data.getBytes(StandardCharsets.UTF_8);
		int    postDataLength = postData.length;
		URL    url_request          = new URL( url );
		HttpURLConnection conn= (HttpURLConnection) url_request.openConnection();           
		conn.setDoOutput( true );
		conn.setInstanceFollowRedirects( false );
		conn.setRequestMethod( "POST" );
		conn.setRequestProperty( "Content-Type", "application/json"); 
		conn.setRequestProperty( "charset", "utf-8");
		conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
		conn.setUseCaches( false );

		try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
		  wr.write( postData );
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		
		
		StringBuffer response = new StringBuffer();
		String inputline;
		while ((inputline = in.readLine()) != null) {
		     response.append(inputline);
		}
		
		return response.toString();
	}
	
	public String getJsonFromUrl(String url) throws IOException {
		
		URL    url_request          = new URL( url );
		HttpURLConnection conn= (HttpURLConnection) url_request.openConnection();           
		conn.setDoOutput( true );
		conn.setInstanceFollowRedirects( false );
		conn.setRequestProperty("Authorization", "Bearer 18dacc19-f73b-3600-ab37-9fac8eb4f60f"); 
		conn.setRequestMethod( "POST" );
		conn.setRequestProperty( "Content-Type", "application/json"); 
		conn.setRequestProperty( "charset", "utf-8");
		conn.setUseCaches( false );
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		StringBuffer response = new StringBuffer();
		String inputline;
		while ((inputline = in.readLine()) != null) {
		     response.append(inputline);
		}
		
		return response.toString();
	}
	
}
