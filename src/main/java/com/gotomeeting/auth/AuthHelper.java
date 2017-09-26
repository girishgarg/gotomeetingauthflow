package com.gotomeeting.auth;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import org.springframework.web.util.UriComponentsBuilder;
public class AuthHelper {
	public static String getLoginUrl(UUID state) {
		String authurl= "https://api.getgo.com/oauth/authorize?client_id=NgzPn48YEARvjKgdCtFNNbcjpnX1yK6z&redirect_uri=http://localhost:8080/gotomeeting1/authorize.html&state=";
	    UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(authurl);
	    

	    return urlBuilder.toUriString();
	  }
}
