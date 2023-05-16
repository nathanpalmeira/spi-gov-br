package org.redhat.rhsso.mapper.common;

import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserSessionModel;

public class KeycloakToken {
    

    public String getTokenIDP(UserSessionModel userSession, KeycloakSession session){
        try {
            RealmModel realm = userSession.getRealm();
            UserModel user = userSession.getUser();
            System.out.println(session.users().getFederatedIdentity(realm, user, "google").getToken());
            System.out.println(session.users().getFederatedIdentity(realm, user, "google").getUserId());
            System.out.println(session.users().getFederatedIdentity(realm, user, "google").getIdentityProvider());
            String token = session.users().getFederatedIdentity(realm, user, "google").getToken();
            return token;
        } catch (Exception e) {
            return null;
        }
        

    }
}
