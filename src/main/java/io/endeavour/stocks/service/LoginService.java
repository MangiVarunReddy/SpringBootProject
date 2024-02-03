package io.endeavour.stocks.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LoginService {
    private String loginURL;
    private RestTemplate restTemplate;

    //@Value annotation reads value from application.properties and injects it into the java variables
    public LoginService(@Value("${client.stock-calculations.url}") String baseURL,
                        @Value("${client.login.username}") String userName,
                        @Value("${client.login.password}") String password){
        loginURL=baseURL+"/login";

        //Rest Template is a client that can generate a HttpRequest to be sent to another webservive and read the response that is sent
        restTemplate=new RestTemplate();

        //Add an interseptor to the restTemplate to ensure that any call going through this restTemplate this restTemplate
        //will have a  Basic Authentication header added
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(
                userName,password
        ));
    }

    /**
     * This method generates the barer token to be used by the cumulativeReturn Calculation API
     * @return String
     */
    public String getBearerToken(){
        //Rest Template exchange method will fire the actual webservice call to the remote API.
        //Its input parameters include:
        //1)The url of the webservice call to hit
        //2) The Http method used to hit the remote API
        //3) The Request object to be sent to the remote API (null in our case)
        //4) How should the response received back to be converted to. (String in our case)
        ResponseEntity<String> response = restTemplate.exchange(loginURL, HttpMethod.POST, null, String.class);
        String token=response.getBody();
        return token;
    }
}
