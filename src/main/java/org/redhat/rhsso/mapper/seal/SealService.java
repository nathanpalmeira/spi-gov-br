package org.redhat.rhsso.mapper.seal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.ws.rs.core.HttpHeaders;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jboss.logging.Logger;

import org.redhat.rhsso.mapper.common.ConnectionUtils;
import org.redhat.rhsso.mapper.common.Utils;


public class SealService {
	private static final Logger logger = Logger.getLogger(SealService.class);

	public static final String TOKEN_PREFIX = "Bearer ";

	public String getSeal(String urlGovBr, String originalToken, String cpf) {

		try {
			originalToken = Utils.getAccesTokenInStringResponse(originalToken);
			logger.info("Trying to obtain original token for " + cpf);
			if (originalToken != null) {
				HttpGet get = new HttpGet(
					urlGovBr + "/confiabilidades/v3/contas/" + cpf + "/confiabilidades?response-type=ids");
				CloseableHttpClient httpClient = ConnectionUtils.getHttpClient();
				ConnectionUtils.configureDefaultHeaders(get);
				get.addHeader(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + originalToken);
				CloseableHttpResponse response = httpClient.execute(get);
				String jsonResp = EntityUtils.toString(response.getEntity());
				return jsonResp;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	
	// public static void main(String[] args) {


    //     ObjectMapper mapper = new ObjectMapper();
    //     // String json = "{\"access_token\":\"eyJraWQiOiJyc2ExIiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiIxMjE2ODcwNDcwOCIsImF1ZCI6ImxvZ2luLnJqLmdvdi5iciIsInNjb3BlIjpbImVtYWlsIiwiZ292YnJfY29uZmlhYmlsaWRhZGVzIiwiZ292YnJfZW1wcmVzYSIsIm9wZW5pZCIsInBob25lIiwicHJvZmlsZSJdLCJhbXIiOlsicGFzc3dkIl0sImlzcyI6Imh0dHBzOlwvXC9zc28uYWNlc3NvLmdvdi5iclwvIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiMTIxNjg3MDQ3MDgiLCJleHAiOjE2ODUxMzQ2MzMsImlhdCI6MTY4NTEzMTAzMywianRpIjoiMGExZGYxZDAtMmE5Ny00N2NlLWEyZGItMjJhMjRkMDYwYmNmIn0.hbpNml1y8TOR3CX7ryNJSEtbsQbcyAkNxhUIU8JyTwsGuhWm94GRDKpDwOn78ZC0MONzqZ5-nDAH3ca7wqqxRGXpdjVJzzZtcxnMHG_dMS0uDjq0dXIuD_tMqS-mzlaGz7Rb0rO__6ISoAtkxx0hnIE8zWEGJM9DgS4GbWlqME8W-g6koeXja4m6U4zqtKWpuYukdPY23snn4zlUNRjhwVngCS-3yEPuYQ3JJgJ2zgTfGrmumj2MVwXK_n8zevIm8KylbTUhaEGRpm-hd8H9q1TEVLGMt9invbMFo9FtE8x1nXpKayBxGQBAYcwgUjQBkFCfcxJjjCnskWpDogHnDA\",\"expires_in\":3599,\"refresh_expires_in\":0,\"token_type\":\"Bearer\",\"id_token\":\"eyJraWQiOiJyc2ExIiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiIxMjE2ODcwNDcwOCIsImVtYWlsX3ZlcmlmaWVkIjoidHJ1ZSIsImFtciI6WyJwYXNzd2QiXSwicHJvZmlsZSI6Imh0dHBzOlwvXC9zZXJ2aWNvcy5hY2Vzc28uZ292LmJyXC8iLCJraWQiOiJyc2ExIiwiaXNzIjoiaHR0cHM6XC9cL3Nzby5hY2Vzc28uZ292LmJyXC8iLCJwaG9uZV9udW1iZXJfdmVyaWZpZWQiOiJ0cnVlIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiMTIxNjg3MDQ3MDgiLCJub25jZSI6IlQ2WnB0R3BtWXdNVG1iVjdyYWxRWWciLCJwaWN0dXJlIjoiaHR0cHM6XC9cL3Nzby5hY2Vzc28uZ292LmJyXC91c2VyaW5mb1wvcGljdHVyZSIsImF1ZCI6ImxvZ2luLnJqLmdvdi5iciIsImF1dGhfdGltZSI6MTY4NTEyOTY3Miwic2NvcGUiOlsiZW1haWwiLCJnb3Zicl9jb25maWFiaWxpZGFkZXMiLCJnb3Zicl9lbXByZXNhIiwib3BlbmlkIiwicGhvbmUiLCJwcm9maWxlIl0sIm5hbWUiOiJOYXRoYW4gUGFsbWVpcmEgRmVycmVpcmEiLCJwaG9uZV9udW1iZXIiOiI2MTk5NDEyOTI1NiIsImV4cCI6MTY4NTEzMTYzMywiaWF0IjoxNjg1MTMxMDMzLCJqdGkiOiIwYTU4ZDJiMy0wMDljLTRlNWUtOWQyZi0zNTMwNDZiNWNkOTgiLCJlbWFpbCI6Im5hdGhhbnBmQGdtYWlsLmNvbSJ9.Iv4ub_zYJRZcLPQbOHhuG43tQjbGnbN6wK-OpZG9R5jBBw-mZ7b4BGOufQe2GiWI2-3iq1rBLd6ddpwoNRdjboXiny6l0KUVXwJ-nkGYZrEWi_AKD59lwWfIa8lCDYv2ncpMXZ8g9qkzi7rRrgNfiAdnujVhZO0SDmGc5wSmRz2a4hBfYPZIOc-9S16t_kwMRGCsSYJBAAxCa86F-RsNiXkcm7cC_SkBGrFywHpPGkr-FTF32RzOyVroZaCSxBt6ZItoBmlPVM0iUKDj5C37DVB3Gz-ZzU8QBRVbXszy8RrExEIMyAVtYxGZefV4Ga5Cb0aXa5O-FeKdiZxc8c6KiA\",\"not-before-policy\":0,\"scope\":\"govbr_empresa phone email openid govbr_confiabilidades profile\",\"accessTokenExpiration\":1685134633}";
	// 	String json = "[{\"id\":\"801\",\"dataAtualizacao\":\"2023-04-26T16:00:20.030-0300\"},{\"id\":\"401\",\"dataAtualizacao\":\"2022-03-11T18:35:12.448-0300\"},{\"id\":\"101\",\"dataAtualizacao\":\"2019-07-16T21:17:11.051-0300\"},{\"id\":\"201\",\"dataAtualizacao\":\"2018-07-20T19:37:37.717-0300\"}]";
        
	// 	try {

    //         // convert JSON string to Map
	// 		List<Map<String, Object>> myObjects = 
    //       mapper.readValue(json , new TypeReference<List<Map<String, Object>>>(){});
	// 	  Object obj = mapper.readValue(json, new TypeReference<Object>() {});
    //         // Map<String, String> map = mapper.readValue(json, Map.class);
    //         // List<Map> ppl2 = Arrays.asList(mapper.readValue(json, Map.class));
			
	// 		// it works
    //         //Map<String, String> map = mapper.readValue(json, new TypeReference<Map<String, String>>() {});

    //         // System.out.println(map.get("access_token"));
    //         System.out.println(myObjects.get(0).get("id"));
    //         System.out.println(myObjects.get(0).get("dataAtualizacao"));
    //         System.out.println(myObjects.get(1).get("id"));
    //         System.out.println(myObjects.get(1).get("dataAtualizacao"));

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }

    // }
}
