package com.chua.utils.tools.http.stream;


/**
 * url builder
 *
 * @author CHTK
 */
public class UrlClientStream implements HttpClientStream {

    @Override
    public JsonClientStream newJsonStream() {
        return null;
    }

    @Override
    public FromClientStream newFromStream() {
        return new FromClientStream() {
        };
    }


}
