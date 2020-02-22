package uk.co.mruoc.rest.client;

import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

import static java.net.URLDecoder.decode;

public class SimpleRestClient extends AbstractSimpleRestClient {

    private static final String DEFAULT_ENCODING = "utf-8";
    private static final Logger LOG = LoggerFactory.getLogger(SimpleRestClient.class);

    private final BodyExtractor bodyExtractor = new BodyExtractor();

    public SimpleRestClient() {
        this(ApacheHttpClientFactory.build());
    }

    public SimpleRestClient(int httpTimeout) {
        this(ApacheHttpClientFactory.build(httpTimeout));
    }

    public SimpleRestClient(HttpClient client) {
        super(client);
    }

    @Override
    protected void log(HttpRequestBase request) {
        try {
            URI uri = request.getURI();
            LOG.info("performing " + request.getMethod() + " on uri " + decode(uri.toString(), DEFAULT_ENCODING));
            LOG.debug("encoded uri is " + uri.toString());
            logBody(request);
            logHeaders(request);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    protected void log(Response response) {
        LOG.info("response status code " + response.getStatusCode());
        LOG.info("response body " + response.getBody());
        logHeaders(response);
    }

    private void logBody(HttpRequest request) throws IOException {
        String body = bodyExtractor.extract(request);
        if (!body.isEmpty())
            LOG.info("request body " + body);
    }

    private void logHeaders(HttpRequest request) {
        Collection<org.apache.http.Header> headers = Arrays.asList(request.getAllHeaders());
        headers.forEach(header -> LOG.debug("response header " + header.getName() + " with value " + header.getValue()));
    }

    private void logHeaders(Response response) {
        Collection<String> headersKeys = response.getHeaderKeys();
        headersKeys.forEach(h -> LOG.debug("response header " + h + " with value " + response.getHeader(h)));
    }



}