package uk.co.mruoc.rest.client.test;

import org.junit.Before;
import org.junit.Test;
import uk.co.mruoc.rest.client.header.DefaultHeaders;
import uk.co.mruoc.rest.client.header.Headers;
import uk.co.mruoc.rest.client.response.Response;
import uk.co.mruoc.rest.client.response.Response.ResponseBuilder;

import java.io.IOException;
import java.io.UncheckedIOException;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static uk.co.mruoc.rest.client.Method.*;

public class FakeRestClientTest {

    private static final String ENDPOINT = "http://localhost:8080/endpoint";

    private final FakeRestClient client = new DefaultFakeRestClient();

    @Before
    public void setUp() {
        client.cannedResponse(200, "");
    }

    @Test
    public void recordsRequestMethod() {
        client.post(ENDPOINT, "body");
        assertThat(client.lastRequestMethod()).isEqualTo(POST);

        client.put(ENDPOINT, "body");
        assertThat(client.lastRequestMethod()).isEqualTo(PUT);

        client.get(ENDPOINT);
        assertThat(client.lastRequestMethod()).isEqualTo(GET);

        client.delete(ENDPOINT);
        assertThat(client.lastRequestMethod()).isEqualTo(DELETE);
    }

    @Test
    public void recordsPostRequestBody() {
        client.put(ENDPOINT, "body");

        assertThat(client.lastRequestBody()).isEqualTo("body");
    }

    @Test
    public void accessingGetBodyReturnsEmptyString() {
        client.get(ENDPOINT);

        assertThat(client.lastRequestBody()).isEmpty();
    }

    @Test
    public void accessingDeleteBodyReturnsEmptyString() {
        client.delete(ENDPOINT);

        assertThat(client.lastRequestBody()).isEmpty();
    }

    @Test
    public void allowsPullingOutSingleHeader() {
        Headers headers = new DefaultHeaders();
        headers.set("Custom-Header1", "headerValue1");

        client.delete(ENDPOINT, headers);

        assertThat(client.lastRequestHeader("Custom-Header1")).isEqualTo("headerValue1");
    }

    @Test
    public void recordsEndpoint() {
        client.get(ENDPOINT);

        assertThat(client.lastRequestUri()).isEqualTo(ENDPOINT);
    }

    @Test
    public void recordsAllRequests() {
        client.get(ENDPOINT);
        client.post(ENDPOINT, "");
        client.put(ENDPOINT, "");
        client.delete(ENDPOINT);

        assertThat(client.allRequests().size()).isEqualTo(4);
        assertThat(client.allRequests().get(0).getMethod()).isEqualTo(GET);
        assertThat(client.allRequests().get(1).getMethod()).isEqualTo(POST);
        assertThat(client.allRequests().get(2).getMethod()).isEqualTo(PUT);
        assertThat(client.allRequests().get(3).getMethod()).isEqualTo(DELETE);
    }

    @Test
    public void returnsCannedResponseStatus() {
        client.cannedResponse(201);
        assertThat(client.get(ENDPOINT).getStatusCode()).isEqualTo(201);
    }

    @Test
    public void returnsCannedResponseBody() {
        client.cannedResponse(201, "CannedBody");
        assertThat(client.get(ENDPOINT).getBody()).isEqualTo("CannedBody");
    }

    @Test
    public void returnsCannedResponseHeaders() {
        Headers headers = new DefaultHeaders();
        headers.set("Response-Header", "responseHeaderValue");

        client.cannedResponse(201, "CannedBody", headers);

        assertThat(client.get(ENDPOINT).getHeader("Response-Header")).isEqualTo("responseHeaderValue");
    }

    @Test(expected = RuntimeException.class)
    public void allowsSettingUpException() {
        client.throwsException(new RuntimeException());

        client.get("dummy-endpoint");
    }

    @Test(expected = UncheckedIOException.class)
    public void allowsSettingUpAnyRuntimeException() {
        client.throwsException(new UncheckedIOException(new IOException()));

        client.get("dummy-endpoint");
    }

    @Test(expected = UncheckedIOException.class)
    public void allowsThrowingOfIoException() {
        client.throwsIoException();

        client.get("dummy-endpoint");
    }

    @Test
    public void usingResponseReturnsCannedResponseStatusCode() {
        int statusCode = 201;
        Response response = new ResponseBuilder().setStatusCode(statusCode).build();

        client.cannedResponse(response);

        assertThat(client.get(ENDPOINT).getStatusCode()).isEqualTo(statusCode);
    }

    @Test
    public void usingResponseReturnsCannedResponseBody() {
        String body = "body";
        Response response = new ResponseBuilder().setBody(body).build();

        client.cannedResponse(response);

        assertThat(client.get(ENDPOINT).getBody()).isEqualTo(body);
    }

    @Test
    public void usingResponseReturnsCannedResponseHeaders() {
        Headers headers = new DefaultHeaders();
        headers.set("Response-Header", "responseHeaderValue");
        Response response = new ResponseBuilder().setHeaders(headers).build();

        client.cannedResponse(response);

        assertThat(client.get(ENDPOINT).getHeaders().hasSameValues(headers)).isTrue();
    }

}
