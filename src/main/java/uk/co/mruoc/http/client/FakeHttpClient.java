package uk.co.mruoc.http.client;

import java.util.List;

public interface FakeHttpClient extends HttpClient {

    List<Request> allRequests();

    Method lastRequestMethod();

    String lastRequestBody();

    String lastRequestHeader(String name);

    Headers lastRequestHeaders();

    Request lastRequest();

    String lastRequestUri();

    void cannedResponse(int status);

    void cannedResponse(int status, String entity);

    void cannedResponse(int status, String entity, Headers headers);

    void cannedResponse(Response response);

    void throwsException(RuntimeException exception);

    void throwsIoException();

}
