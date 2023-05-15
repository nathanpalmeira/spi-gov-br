package org.redhat.rhsso.mapper.empresa;

import org.keycloak.OAuthErrorException;
import org.keycloak.models.ClientSessionContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.protocol.oidc.mappers.*;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.representations.IDToken;
import org.keycloak.services.ErrorResponseException;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;


/**
* 
*/
public class GovBREmpresaMapper extends AbstractOIDCProtocolMapper implements OIDCAccessTokenMapper{

    protected final Logger logger = Logger.getLogger(GovBREmpresaMapper.class);

    public static final String PROVIDER_ID = "govbr-empresa-mapper";

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<>();

    static {
        OIDCAttributeMapperHelper.addTokenClaimNameConfig(configProperties);
        //TODO: configs properties
        OIDCAttributeMapperHelper.addIncludeInTokensConfig(configProperties, GovBREmpresaMapper.class);
    }

    
    /** 
     * @return List<ProviderConfigProperty>
     */
    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    @Override
    public String getDisplayCategory() {
        return TOKEN_MAPPER_CATEGORY;
    }

    @Override
    public String getDisplayType() {
        return "GovBR - Empresa";
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getHelpText() {
        return "Inclui no token a empresa importada do GOV.BR";
    }
    
    
    /** 
     * Method that is called in the construction of the token, it triggers the 
     * other methods that capture the rights and insert the result in the token
     * 
     * @param token 
     * @param mappingModel
     * @param userSession
     * @param keycloakSession
     * @param clientSessionCtx
     * @throws ErrorResponseException
     */
    @Override
    protected void setClaim(IDToken token, ProtocolMapperModel mappingModel, UserSessionModel userSession, KeycloakSession keycloakSession,
                            ClientSessionContext clientSessionCtx) throws ErrorResponseException {
        this.logger.info("Iniciando SPI GovBREmpresaMapper");


        //TODO: GET PROPERTIES

        //TODO: Consult GOV.br 

        //TODO: Format data

        String params = "GovBREmpresaMapper";
        this.logger.info("Incluindo resultado ao token");
        OIDCAttributeMapperHelper.mapClaim(token, mappingModel, params);
       
    }

    

    @Override
    public void close() {
        logger.info("SPI GovBREmpresaMapper - finished");
    }
    

    
    /** 
     * Default Error Return
     * 
     * @param error
     * @return ErrorResponseException
     */
    public ErrorResponseException errorHandling(String error) {
        return new ErrorResponseException(
            OAuthErrorException.SERVER_ERROR, 
            "Error fetching infos in Gov.br rights", 
            Response.Status.BAD_REQUEST);
    }


}