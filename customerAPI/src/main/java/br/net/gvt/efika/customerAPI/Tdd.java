package br.net.gvt.efika.customerAPI;

import br.net.gvt.efika.customerAPI.model.service.certificator.impl.CertifierYouboraCertificationImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.apache.http.HttpHost;

/**
 * Tiago Henrique Iwamoto
 * tiago.iwamoto@telefonica.com
 * System Analyst
 * 41 9 9513-0230
 **/
public class Tdd {
    public static void main(String[] args) {
        try{
            String url =
                    String.format("https://qos-commands-api.youbora.com/%s/certification/execute_certifications/UserID/%s",
                            "1",
                            "asdfasdf");
            Unirest.setProxy(new HttpHost("192.168.25.89", 8080));
            HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                    .header("accept", "application/json")
                    .asJson();
            System.out.println(jsonResponse.getBody());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
