package example0121;

import javax.xml.ws.Endpoint;

@javax.jws.WebService(name="webService")
public class WebService {
	
	public void show(String field){
		System.out.println("------------"+field);
	}
	
	public static void main(String[] args) {
		String addre = "http://localhost:8080/webService";
		 Endpoint.publish(addre, new WebService());  
	}
}
