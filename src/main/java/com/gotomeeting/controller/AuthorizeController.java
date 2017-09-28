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
import org.apache.http.client.methods.HttpPut;
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
	      HttpServletRequest request) throws ClientProtocolException, IOException,Exception  { 
	   
	    HttpSession session = request.getSession();
	    //UUID expectedState = (UUID) session.getAttribute("expected_state");
	    //if (state.equals(expectedState)) {
	      session.setAttribute("authCode", code);
	   accesstokenget(code);
	   
	    
	    return "mail";
}
	
	//function to fetch accesstoken
	public void accesstokenget(String code) throws ClientProtocolException, IOException,Exception   {
		
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
		  JSONObject  jsonObject = new JSONObject(builder.toString());
		  String accesstoken = jsonObject.getString("access_token");
		  String refreshtoken = jsonObject.getString("refresh_token");
		  //accesstokengetfromrefresh(refreshtoken);
		  meetingget(accesstoken);
	
	}
	
	//code to fetchaccesstoken using refresh token
public void accesstokengetfromrefresh(String code) throws ClientProtocolException, IOException,Exception   {
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		StringBuilder   builder = new StringBuilder();
		HttpGet postRequest = new HttpGet(
			"https://api.getgo.com/oauth/access_token?grant_type=refresh_token&client_id=NgzPn48YEARvjKgdCtFNNbcjpnX1yK6z&refresh_token="+code);
		HttpResponse response = httpClient.execute(postRequest);
		 BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		  String line = "";
		  while ((line = rd.readLine()) != null) {
		    builder.append(line);
		  }
		  JSONObject  jsonObject = new JSONObject(builder.toString());
		  String name = jsonObject.getString("access_token");
		  meetingget(name);
		
	}
	
	//function to fetch meetings
	public void meetingget(String code) throws ClientProtocolException, IOException,Exception  {
		try{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		StringBuilder   builder = new StringBuilder();
		HttpGet postRequest = new HttpGet(
			"https://api.getgo.com/G2M/rest/upcomingMeetings");
		postRequest.addHeader("Authorization", "Bearer "+code);
		HttpResponse response = httpClient.execute(postRequest);
		
		BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		  String line = "";
		  System.out.println("------total meetings are as follows------");
		  while ((line = rd.readLine()) != null) {
		    System.out.println(line);
		    builder.append(line);
		  }
		  /*String name = builder.toString();
		  //System.out.println(name);
		  String accesstoken3 = "";
		  for (int index = 1; index < name.length()-1;
				  index++) {
				       char aChar = name.charAt(index);
				       accesstoken3 = accesstoken3 + aChar;	  
		  }
		  System.out.println(accesstoken3);*/
		  createmeeting(code);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	//function to create a meeting and get meeting link
	public void createmeeting(String code) throws ClientProtocolException, IOException,Exception  {
		try{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		StringBuilder   builder = new StringBuilder();
		HttpPost postRequest = new HttpPost(
			"https://api.getgo.com/G2M/rest/meetings");
		postRequest.addHeader("Authorization", "Bearer "+code);
		JSONObject json = new JSONObject();
		  json.put("subject", "sadafssa");
		  json.put("starttime", "2017-09-18T13:00:00Z");
		  json.put("endtime", "2017-09-18T14:00:00Z");
		  json.put("passwordrequired", "true");
		  json.put("conferencecallinfo", "video");
		  json.put("timezonekey","string");
		  json.put("meetingtype", "immediate");
		  
		   StringEntity se = new StringEntity(json.toString());
		  postRequest.setEntity(se);
		HttpResponse response = httpClient.execute(postRequest);
		

		BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		  String line = "";
		  System.out.println("-----created meeting and link for that is as follows-----");
		  while ((line = rd.readLine()) != null) {
		    System.out.println(line);
		    builder.append(line);
		  }
		  String s = builder.toString();
		  String meetingid = "";
		  for (int index = 1; index < s.length()-1;
				  index++) {
				       char aChar = s.charAt(index);
				       meetingid = meetingid + aChar;	  
		  }
		  JSONObject  jsonObject1 = new JSONObject(meetingid);
			  int name1 = jsonObject1.getInt("meetingid");
		  System.out.println("meetingid is "+name1);
		  updateevent(code,name1);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		  
	}
	
	//code to update an meeting
	public void updateevent(String code,int id) throws ClientProtocolException, IOException,Exception  {
		try{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		String meetingid=Integer.toString(id);
		String url = "https://api.getgo.com/G2M/rest/meetings/"+meetingid;
		HttpPut postRequest = new HttpPut(url);
		postRequest.addHeader("Authorization", "Bearer "+code);
		JSONObject json = new JSONObject();
		  json.put("subject", "sadafssa");
		 json.put("starttime", "2017-09-19T13:00:00Z");
		  json.put("endtime", "2017-09-19T14:00:00Z");
		  json.put("passwordrequired", "true");
		  json.put("conferencecallinfo", "video");
		  json.put("timezonekey","string");
		  json.put("meetingtype", "immediate");
		  
		   StringEntity se = new StringEntity(json.toString());
		  postRequest.setEntity(se);
		HttpResponse response = httpClient.execute(postRequest);
		if (response.getStatusLine().getStatusCode() == 204) {
			System.out.println("------event updated-----");
		}
		
		 deleteevent(code,id);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	
	//code to delete meeting
	public void deleteevent(String code,int id) throws ClientProtocolException, IOException,Exception  {
		try{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		//StringBuilder   builder = new StringBuilder();
		String meetingid=Integer.toString(id);
		String url = "https://api.getgo.com/G2M/rest/meetings/"+meetingid;
		HttpGet postRequest = new HttpGet(
			url);
		postRequest.addHeader("Authorization", "Bearer "+code);
		HttpResponse response = httpClient.execute(postRequest);
		
		BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		  String line = "";
		  System.out.println("------Deleted meeting------");
		  while ((line = rd.readLine()) != null) {
		    System.out.println(line);
		    
		  }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
}


