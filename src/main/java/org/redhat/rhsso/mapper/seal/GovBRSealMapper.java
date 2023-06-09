package org.redhat.rhsso.mapper.seal;

import org.keycloak.OAuthErrorException;
import org.keycloak.models.ClientSessionContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.protocol.ProtocolMapperUtils;
import org.keycloak.protocol.oidc.mappers.*;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.representations.IDToken;
import org.keycloak.services.ErrorResponseException;
import org.redhat.rhsso.mapper.common.Utils;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;


/**
* 
*/
public class GovBRSealMapper extends AbstractOIDCProtocolMapper implements OIDCAccessTokenMapper{

    protected final Logger logger = Logger.getLogger(GovBRSealMapper.class);

    public static final String PROVIDER_ID = "govbr-confiabilidade-mapper";

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<>();

    private static final Utils utils = new Utils();

    private static final SealService sealService = new SealService();

    static {
        OIDCAttributeMapperHelper.addTokenClaimNameConfig(configProperties);
        OIDCAttributeMapperHelper.addIncludeInTokensConfig(configProperties, GovBRSealMapper.class);
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
        return "GovBR - Confiabilidade (Selos)";
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getHelpText() {
        return "Inclui no token a confiabilidade importada do GOV.BR";
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
        this.logger.info("Iniciando SPI GovBRSealMapper");
                            
        this.logger.info("Configurando Mapper");
        mappingModel = Utils.inCludeConfigMapper(mappingModel);

        this.logger.info("Obtendo o CPF do usuário");
        UserModel user = userSession.getUser();
        String cpfUser = user.getUsername();

        this.logger.info("Obtendo o token do GOV.BR");
        String tokenBK = utils.getTokenIDP(userSession, keycloakSession);

        var urlGovBr = Utils.getURLGovBR(keycloakSession);

        this.logger.info("Obtendo CONFIABILIDADE no GOV.BR ("+urlGovBr+")");
        var response = sealService.getSeal(urlGovBr,tokenBK, cpfUser);
        
        Object respFormated = Utils.converterResponse(response);

        this.logger.info("Resultado da busca de CONFIABILIDADE obtido no GOV.BR = "+response.toString());
        this.logger.info("Incluindo resultado ao token");
        OIDCAttributeMapperHelper.mapClaim(token, mappingModel, respFormated);
    }

    
    @Override
    public void close() {
        logger.info("SPI GovBRSealMapper - finished");
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