package uk.co.mruoc.rest.client.header;

public class DefaultBasicAuthCredentials implements BasicAuthCredentials {

    private final String key;
    private final String secret;

    public DefaultBasicAuthCredentials(String key, String secret) {
        this.key = key;
        this.secret = secret;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getSecret() {
        return secret;
    }

}
