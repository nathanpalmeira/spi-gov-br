package org.redhat.rhsso.mapper.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.protocol.ProtocolMapperUtils;
import org.keycloak.protocol.oidc.mappers.OIDCAttributeMapperHelper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
    
    public static ObjectMapper mapper = new ObjectMapper();
    

    public String getTokenIDP(UserSessionModel userSession, KeycloakSession session){
        try {
            RealmModel realm = userSession.getRealm();
            UserModel user = userSession.getUser();
            String token = session.users().getFederatedIdentity(realm, user, "govbr").getToken();
            return token;
        } catch (Exception e) {
            return null;
        }
        
    }
   
    public static String getAccesTokenInStringResponse (String token) {
        try {
            final Map<String, String> map = mapper.readValue(token, Map.class);
		    return map.get("access_token");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}

    public static Object converterResponse(String data) {
        try {
            final List<Map<String, Object>> obj = 
              mapper.readValue(data , new TypeReference<List<Map<String, Object>>>(){});
            Map<String, Object> resp = new HashMap<String, Object>();
            resp.put("value", obj);
        return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

    public static String getURLGovBR(KeycloakSession keycloakSession) {
        
        var urlGovBr = keycloakSession.getContext().getRealm().getIdentityProviderByAlias("govbr").getConfig().get("tokenUrl");
        
        return urlGovBr.split("/tok")[0].replace("/sso", "/api");
    }
    public static ProtocolMapperModel inCludeConfigMapper(ProtocolMapperModel mappingModel) {
        Map<String, String> config = new HashMap<>(); 
        config.put(ProtocolMapperUtils.MULTIVALUED, Boolean.TRUE.toString());
        config.put(OIDCAttributeMapperHelper.INCLUDE_IN_ACCESS_TOKEN, "true");
        config.put(OIDCAttributeMapperHelper.INCLUDE_IN_ID_TOKEN, "true");
        config.put(OIDCAttributeMapperHelper.INCLUDE_IN_USERINFO, "true");
        config.put(OIDCAttributeMapperHelper.TOKEN_CLAIM_NAME,  mappingModel.getConfig().get(OIDCAttributeMapperHelper.TOKEN_CLAIM_NAME));
        mappingModel.setConfig(config);
        return mappingModel;
    }

}
