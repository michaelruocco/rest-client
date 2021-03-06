package uk.co.mruoc.rest.client.test;

import uk.co.mruoc.rest.client.header.Headers;
import uk.co.mruoc.rest.client.response.Response;
import uk.co.mruoc.rest.client.response.Response.ResponseBuilder;

import java.util.ArrayDeque;
import java.util.Deque;

public class FakeMultipleResponseRestClient extends DefaultFakeRestClient {

    private final Deque<Response> responseStack = new ArrayDeque<>();

    @Override
    public Response post(String endpoint, String entity, Headers headers) {
        super.cannedResponse(responseStack.pop());
        return super.post(endpoint, entity, headers);
    }

    @Override
    public Response put(String endpoint, String entity, Headers headers) {
        super.cannedResponse(responseStack.pop());
        return super.put(endpoint, entity, headers);
    }

    @Override
    public Response patch(String endpoint, String entity, Headers headers) {
        super.cannedResponse(responseStack.pop());
        return super.patch(endpoint, entity, headers);
    }

    @Override
    public Response get(String endpoint, Headers headers) {
        super.cannedResponse(responseStack.pop());
        return super.get(endpoint, headers);
    }

    @Override
    public Response delete(String endpoint, Headers headers) {
        super.cannedResponse(responseStack.pop());
        return super.delete(endpoint, headers);
    }

    @Override
    public void cannedResponse(int status) {
        Response response = new ResponseBuilder()
                .setStatusCode(status)
                .build();
        cannedResponse(response);
    }

    @Override
    public void cannedResponse(int status, String entity) {
        Response response = new ResponseBuilder()
                .setStatusCode(status)
                .setBody(entity)
                .build();
        cannedResponse(response);
    }

    @Override
    public void cannedResponse(int status, String entity, Headers headers) {
        Response response = new ResponseBuilder()
                .setStatusCode(status)
                .setBody(entity)
                .setHeaders(headers)
                .build();
        cannedResponse(response);
    }

    @Override
    public void cannedResponse(Response response) {
        responseStack.addLast(response);
    }

}
