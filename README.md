[![img](https://img.shields.io/badge/Lifecycle-Experimental-339999)](https://github.com/bcgov/repomountie/blob/master/doc/lifecycle-badges.md)

This repository contains implementation of a SPI(Service Provider Interfaces) for RHSSO or Keycloak that adds extra claims, origined of the GOV.BR, to an OIDC token retrieved from an external REST API.


## Installation

1. Build the project by running `mvn package`.
2. Copy the resulting JAR to Keycloak's `standalone/deployments/` directory.

The JAR will be hot deployed, so Keycloak can be running or not.

Tested with:
* Keycloak 18.0 (Docker/Podman image quay.io/keycloak/keycloak:18.0)
* RHSSO 7.6 (Docker/Podman image registry.redhat.io/rh-sso-7/sso76-openshift-rhel8:7.6)
* Maven 3.6.3

## Configuration

The plugin is a custom OIDC Protocol Mapper, and it is configured like other mappers.

1. Create a new client in Keycloak or use an existing one.
2. Go to the "Mappers" tab.
3. Click the "Create" button.
4. In the "Mapper Type" drop-down, choose "<NameSetIn-getDisplayType>".
5. Configure the mapper, referring to the onscreen tooltips as needed.

## Demoing

The plugin will execute whenever a token is generated. A fast way to get a token is to enable service accounts on the Keycloak client and then execute:  
  
```
curl -s http://<your-keycloak-url>/auth/realms/master/protocol/openid-connect/token -d grant_type=client_credentials -d client_id=<client_id> -d client_secret=<client_secret>
```

The resulting JWT token can be decoded at https://jwt.io, but remember that a token is a credential and this is a public third-party website.

Another option is to deploy an example application capable of obtaining and inspecting tokens. I recommend Keycloak's demo [JavaScript Example](https://github.com/keycloak/keycloak/tree/master/examples/js-console). You don't need to follow the steps given there, you just need to grab the `index.html` and `keycloak.json` files and deploy them anywhere handy. You will of course need to configure a public client on Keycloak and enter your settings in `keycloak.json`.

## Get Data in Gov.br
The data that will be inserted in the token comes from gov.br. They are: Reliability, Company and Seals. This data will be obtained by external request to RUL made available by the [documentation](https://manual-roteiro-integracao-login-unico.servicos.gov.br/pt/stable/iniciarintegracao.html#resultado-esperado-do-acesso-ao-servico-de-confiabilidade-cadastral-selos).


## Other documentation

* If you need examples, the [source code for the built-in mappers](https://github.com/keycloak/keycloak/tree/master/services/src/main/java/org/keycloak/protocol/oidc/mappers) is on GitHub.
* Keycloak's documentation on Server Development, in particular for [Service Provider Interfaces](https://www.keycloak.org/docs/latest/server_development/index.html#_providers).
* [Education's implementation of a custom protocol mapper](https://github.com/bcgov/EDUC-KEYCLOAK-SOAM/blob/master/extensions/services/src/main/java/ca/bc/gov/educ/keycloak/soam/mapper/SoamProtocolMapper.java). Similar to this proof-of-concept, it also retrieves attributes from an external API. Unlike this simpler example, Education's external API is accessed using the client credentials grant.
* The SSO team's implementation of [a custom authenticator to match new users to existing ones](https://github.com/bcgov/ocp-sso/blob/master/extensions/services/src/main/java/com/github/bcgov/keycloak/IdpCreateUserIfUniqueAuthenticator.java).
* [A blog post describing how to implement a custom protocol mapper](https://medium.com/@pavithbuddhima/how-to-add-custom-claims-to-jwt-tokens-from-an-external-source-in-keycloak-52bd1ff596d3).
* [Integration of documentation with gov.br](https://manual-roteiro-integracao-login-unico.servicos.gov.br/pt/stable/iniciarintegracao.html).



