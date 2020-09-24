package com.chua.utils.tools.compiler.magaer;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 * @author CH
 */
public class ClassFileObject extends SimpleJavaFileObject {


    private String content;
    private ByteArrayOutputStream byteArrayOutputStream = null;

    public String getContent() {
        return content;
    }

    public ClassFileObject(URI uri, Kind kind, String content) {
        super(uri, kind);
        this.content = content;
    }

    public ClassFileObject(String className, Kind kind) {
        super(URI.create(className), kind);
        this.byteArrayOutputStream = new ByteArrayOutputStream();
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return this.content;
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return this.byteArrayOutputStream;
    }

    public byte[] getBytes() {
        return this.byteArrayOutputStream.toByteArray();
    }

}
