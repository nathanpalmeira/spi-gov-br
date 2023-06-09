package org.redhat.rhsso.mapper.level;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.ws.rs.core.HttpHeaders;
import org.redhat.rhsso.mapper.common.ConnectionUtils;
import org.redhat.rhsso.mapper.common.Utils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jboss.logging.Logger;


public class LevelService {
	private static final Logger logger = Logger.getLogger(LevelService.class);

	public static final String TOKEN_PREFIX = "Bearer ";

	private String govBrUrl="https://api.acesso.gov.br";

	public String getLevel(String urlGovBr, String originalToken, String cpf)  {

		try {
			originalToken = Utils.getAccesTokenInStringResponse(originalToken);
			logger.debug("Trying to obtain original token for " + cpf);
			if (originalToken != null) {
				HttpGet get = new HttpGet(
					govBrUrl + "/confiabilidades/v3/contas/" + cpf + "/niveis?response-type=ids");
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
}
