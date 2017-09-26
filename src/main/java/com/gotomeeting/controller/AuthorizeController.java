package com.gotomeeting.controller;
import java.util.UUID;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {
	@RequestMapping(value="/authorize", method=RequestMethod.GET)
	  public String authorize(
	      @RequestParam("code") String code, 
	     // @RequestParam("state") UUID state,
	      HttpServletRequest request) throws ClientProtocolException, IOException  { {
	    
	    HttpSession session = request.getSession();
	    //UUID expectedState = (UUID) session.getAttribute("expected_state");
	    //if (state.equals(expectedState)) {
	      session.setAttribute("authCode", code);
	  
	    accesstokenget(code);
	    return "mail";
	  }
	  
}
	
	//function to fetch accesstoken
	public void accesstokenget(String code) throws ClientProtocolException, IOException   {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		StringBuilder   builder = new StringBuilder();
		HttpGet postRequest = new HttpGet(
			"https://api.getgo.com/oauth/access_token?grant_type=authorization_code&code="+code+"&client_id=NgzPn48YEARvjKgdCtFNNbcjpnX1yK6z");
		
		HttpResponse response = httpClient.execute(postRequest);
		

		
		  BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		  String line = "";
		  while ((line = rd.readLine()) != null) {
		    builder.append(line);
		  }
		  String name = builder.toString();
		  String[] accesstoken = name.split(":");
		  String[] accesstoken1 = accesstoken[1].split(","); 
		  String[] accesstoken2 = accesstoken1[0].split("");
		  String accesstoken3 = "";
		 for(int i=1;i<accesstoken2.length-1;i++){
			 accesstoken3 = accesstoken3 + accesstoken2[i];
		 }
		  meetingget(accesstoken3);
		  
	}
	
	//function to fetch meetings
	public void meetingget(String code) throws ClientProtocolException, IOException   {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		//StringBuilder   builder = new StringBuilder();
		HttpGet postRequest = new HttpGet(
			"https://api.getgo.com/G2M/rest/upcomingMeetings");
		postRequest.addHeader("Authorization", "Bearer "+code);
		HttpResponse response = httpClient.execute(postRequest);
		BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		  String line = "";
		  System.out.println("------total meetings are as follows------");
		  while ((line = rd.readLine()) != null) {
		    System.out.println(line);
		    //builder.append(line);
		  }
		  createmeeting(code);
		  
	}
	
	//function to create a meeting and get meeting link
	public void createmeeting(String code) throws ClientProtocolException, IOException   {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		//StringBuilder   builder = new StringBuilder();
		HttpPost postRequest = new HttpPost(
			"https://api.getgo.com/G2M/rest/meetings");
		postRequest.addHeader("Authorization", "Bearer "+code);
		StringEntity input = new StringEntity("{\"subject\":\"sadafssa\",\"starttime\":\"2017-09-18T13:00:00Z\",\"endtime\":\"2017-09-18T14:00:00Z\",\"passwordrequired\":\"true\",\"conferencecallinfo\":\"video\",\"timezonekey\":\"string\",\"meetingtype\":\"immediate\"}");
		input.setContentType("application/json");
		postRequest.setEntity(input);
		HttpResponse response = httpClient.execute(postRequest);
		BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		  String line = "";
		  System.out.println("-----created meeting and link for that is as follows-----");
		  while ((line = rd.readLine()) != null) {
		    System.out.println(line);

		  }
		  
		  
	}
	
}


