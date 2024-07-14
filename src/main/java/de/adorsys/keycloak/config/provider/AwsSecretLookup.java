package de.adorsys.keycloak.config.provider;

import de.adorsys.keycloak.config.properties.ImportConfigProperties;
import org.apache.commons.text.lookup.StringLookup;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

public class AwsSecretLookup implements StringLookup {

    private final SecretsManagerClient secretsClient;

    public AwsSecretLookup(ImportConfigProperties.ImportVarAwsSecretsSubstitutionProperties properties) {
        this.secretsClient = SecretsManagerClient.builder()
                .region(Region.of(properties.getRegion()))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Override
    public String lookup(String secretId) {
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretId)
                .build();
        GetSecretValueResponse getSecretValueResponse = secretsClient.getSecretValue(getSecretValueRequest);
        return getSecretValueResponse.secretString();
    }
}
