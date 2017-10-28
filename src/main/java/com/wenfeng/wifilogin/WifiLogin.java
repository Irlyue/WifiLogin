package com.wenfeng.wifilogin;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WifiLogin {
    private static final String LOGIN_URL = "https://s.scut.edu.cn:801/eportal/?c=ACSetting&a=Login&wlanuserip=172.21.151.199&wlanacip=172.21.255.250&wlanacname=&redirect=&session=&vlanid=scut-student&port=&iTermType=1&protocol=https:";
    private static final String FORM_FILE = "form.properties";
    private static final String ACCOUNT = "DDDDD";
    private static final String PASSWORD = "upass";
    private static final String R_ONE = "R1";
    private static final String R_TWO = "R2";
    private static final String R_SIX = "R6";
    private static final String PARAMETER = "para";
    private static final String OMMKEY = "OMKKey";
    private static final String LOGIN_SUCCESS = "成功登录";

    public void loginScutStudentWifi(){

        HttpEntity<MultiValueMap<String, String>> requestBody = generateBody();
        RestTemplate template = new RestTemplate();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        HttpClient client = HttpClientBuilder.create()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
        factory.setHttpClient(client);
        template.setRequestFactory(factory);
        ResponseEntity<String> response = template.postForEntity(LOGIN_URL, requestBody, String.class);
        System.out.println(response.getStatusCode());
        // System.out.println(response.getBody());
        printHelpMessage(response.getBody());
    }

    private void printHelpMessage(String body){
        if (body.contains(LOGIN_SUCCESS)) {
            System.out.println("Wifi login successful!!!!");
        }else{
            System.out.println("<<ERROR>>!!!");
        }
    }

    private HttpEntity<MultiValueMap<String, String>> generateBody(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        InputStream is = getClass().getClassLoader().getResourceAsStream(FORM_FILE);
        Properties form = new Properties();
        try{
            form.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add(ACCOUNT, form.getProperty(ACCOUNT));
        map.add(PASSWORD, form.getProperty(PASSWORD));
        map.add(R_ONE, form.getProperty(R_ONE));
        map.add(R_TWO, form.getProperty(R_TWO));
        map.add(R_SIX, form.getProperty(R_SIX));
        map.add(PARAMETER, form.getProperty(PARAMETER));
        map.add(OMMKEY, form.getProperty(OMMKEY));
        return new HttpEntity<MultiValueMap<String, String>>(map, headers);
    }

    public static void main(String[] args){
        new WifiLogin().loginScutStudentWifi();
    }
}
