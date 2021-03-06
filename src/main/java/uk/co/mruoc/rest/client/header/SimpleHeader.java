package uk.co.mruoc.rest.client.header;

public class SimpleHeader implements Header {

    private final String name;
    private final String value;

    public SimpleHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

}
