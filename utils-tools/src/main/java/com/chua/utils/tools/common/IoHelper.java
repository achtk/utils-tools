package com.chua.utils.tools.common;

import com.google.common.base.Splitter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chua.utils.tools.common.FileHelper.LINE_SEPARATOR;
import static com.chua.utils.tools.constant.NumberConstant.DEFAULT_BUFFER_SIZE;
import static com.chua.utils.tools.constant.NumberConstant.EOF;

/**
 * IO工具类
 *
 * @author CH
 */
public class IoHelper {
    /**
     * 获取文件流
     *
     * @param file 文件
     * @return FileInputStream
     * @throws IOException IOException
     */
    public static FileInputStream openStream(final File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canRead()) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }

    /**
     * 获取流
     *
     * @param source 字符串
     * @return StringReader
     */
    public static StringReader openStream(final String source) {
        return new StringReader(source);
    }

    /**
     * 获取流
     *
     * @param url url
     * @return FileInputStream
     * @throws IOException IOException
     */
    public static InputStream openStream(final URL url) throws IOException {
        return null != url ? url.openStream() : null;
    }

    /**
     * 获取流
     *
     * @param uri url
     * @return FileInputStream
     * @throws IOException IOException
     */
    public static InputStream openStream(final URI uri) throws IOException {
        return null != uri ? uri.toURL().openStream() : null;
    }

    /**
     * 读取流
     *
     * @param input  流
     * @param buffer 字节
     * @return int
     * @throws IOException IOException
     */
    public static int read(final InputStream input, final byte[] buffer) throws IOException {
        return read(new InputStreamReader(input), ByteHelper.bytesToChars(buffer), 0, buffer.length);
    }

    /**
     * 读取流
     *
     * @param input  流
     * @param buffer 字节
     * @param offset 位置
     * @param length 长度
     * @return int
     * @throws IOException IOException
     */
    public static int read(final InputStream input, final byte[] buffer, final int offset, final int length) throws IOException {
        return read(new InputStreamReader(input), ByteHelper.bytesToChars(buffer), offset, length);
    }

    /**
     * 读取流
     *
     * @param input  流
     * @param buffer 缓冲
     * @return int
     * @throws IOException IOException
     */
    public static int read(final ReadableByteChannel input, final ByteBuffer buffer) throws IOException {
        final int length = buffer.remaining();
        while (buffer.remaining() > 0) {
            final int count = input.read(buffer);
            if (EOF == count) {
                break;
            }
        }
        return length - buffer.remaining();
    }

    /**
     * 读取流
     *
     * @param input  流
     * @param buffer 字节
     * @return int
     * @throws IOException IOException
     */
    public static int read(final Reader input, final char[] buffer) throws IOException {
        return read(input, buffer, 0, buffer.length);
    }

    /**
     * 读取流
     *
     * @param input  流
     * @param buffer 字节
     * @param offset 位置
     * @param length 长度
     * @return int
     * @throws IOException IOException
     */
    public static int read(final Reader input, final char[] buffer, final int offset, final int length)
            throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Length must not be negative: " + length);
        }
        int remaining = length;
        while (remaining > 0) {
            final int location = length - remaining;
            final int count = input.read(buffer, offset + location, remaining);
            if (EOF == count) {
                break;
            }
            remaining -= count;
        }
        return length - remaining;
    }

    /**
     * 读取所有字节
     *
     * @param input  流
     * @param buffer 字节
     * @param offset 位置
     * @param length 长度
     * @throws IOException IOException
     */
    public static void readFully(final Reader input, final char[] buffer, final int offset, final int length)
            throws IOException {
        final int actual = read(input, buffer, offset, length);
        if (actual != length) {
            throw new EOFException("Length to read: " + length + " actual: " + actual);
        }
    }

    /**
     * 读取所有字节
     *
     * @param input  流
     * @param buffer 字节
     * @throws IOException IOException
     */
    public static void readFully(final InputStream input, final byte[] buffer) throws IOException {
        readFully(input, buffer, 0, buffer.length);
    }

    /**
     * 读取所有字节
     *
     * @param input  流
     * @param buffer 字节
     * @param offset 位置
     * @param length 长度
     * @throws IOException IOException
     */
    public static void readFully(final InputStream input, final byte[] buffer, final int offset, final int length)
            throws IOException {
        final int actual = read(input, buffer, offset, length);
        if (actual != length) {
            throw new EOFException("Length to read: " + length + " actual: " + actual);
        }
    }

    /**
     * 读取所有字节
     *
     * @param input    流
     * @param encoding 编码
     * @throws IOException IOException
     */
    public static List<String> readLines(final InputStream input, final Charset encoding) throws IOException {
        final InputStreamReader reader = new InputStreamReader(input, toCharset(encoding));
        return readLines(reader);
    }

    /**
     * 读取所有字节
     *
     * @param input 流
     * @throws IOException IOException
     */
    public static List<String> readLines(final Reader input) throws IOException {
        final BufferedReader reader = toBufferedReader(input);
        final List<String> list = new ArrayList<>();
        String line = reader.readLine();
        while (line != null) {
            list.add(line);
            line = reader.readLine();
        }
        return list;
    }

    /**
     * Reader 转 BufferedReader
     *
     * @param reader Reader
     * @return BufferedReader
     */
    public static BufferedReader toBufferedReader(final Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    /**
     * 关闭URLConnection.
     *
     * @param conn the connection to close.
     * @since 2.4
     */
    public static void close(final URLConnection conn) {
        if (conn instanceof HttpURLConnection) {
            ((HttpURLConnection) conn).disconnect();
        }
    }

    /**
     * 关闭
     *
     * @param closeable 可关闭
     */
    public static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException ignore) {
            // ignore
        }
    }

    /**
     * 流转字节
     *
     * @param input 输入流
     * @return byte[]
     * @throws IOException IOException
     */
    public static byte[] toByteArray(final InputStream input) throws IOException {
        try (final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            copy(input, output);
            return output.toByteArray();
        }
    }

    /**
     * 流转字节
     *
     * @param input 输入流
     * @param size  数组长度
     * @return byte[]
     * @throws IOException IOException
     */
    public static byte[] toByteArray(final InputStream input, final long size) throws IOException {

        if (size > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + size);
        }
        return toByteArray(input, (int) size);
    }

    /**
     * 流转字节
     *
     * @param input 输入流
     * @param size  数组长度
     * @return byte[]
     * @throws IOException IOException
     */
    public static byte[] toByteArray(final InputStream input, final int size) throws IOException {
        if (size < 0) {
            throw new IllegalArgumentException("Size must be equal or greater than zero: " + size);
        }

        if (size == 0) {
            return new byte[0];
        }

        final byte[] data = new byte[size];
        int offset = 0;
        int read;

        while (offset < size && (read = input.read(data, offset, size - offset)) != EOF) {
            offset += read;
        }

        if (offset != size) {
            throw new IOException("Unexpected read size. current: " + offset + ", expected: " + size);
        }

        return data;
    }

    /**
     * 流转数组
     *
     * @param input 输入流
     * @return byte
     * @throws IOException IOException
     */
    public static byte[] toByteArray(final Reader input) throws IOException {
        return toByteArray(input, Charset.defaultCharset());
    }

    /**
     * URL转字节
     *
     * @param url url
     * @return byte[]
     * @throws IOException IOException
     */
    public static byte[] toByteArray(final URL url) throws IOException {
        final URLConnection conn = url.openConnection();
        try {
            return toByteArray(conn);
        } finally {
            close(conn);
        }
    }

    /**
     * URI转字节
     *
     * @param uri uri
     * @return byte
     * @throws IOException IOException
     */
    public static byte[] toByteArray(final URI uri) throws IOException {
        return toByteArray(uri.toURL());
    }

    /**
     * URLConnection转字节
     *
     * @param urlConnection urlConnection
     * @return byte
     * @throws IOException IOException
     */
    public static byte[] toByteArray(final URLConnection urlConnection) throws IOException {
        try (InputStream inputStream = urlConnection.getInputStream()) {
            return toByteArray(inputStream);
        }
    }

    /**
     * 字符串转流
     *
     * @param input    输入
     * @param encoding 编码
     * @return InputStream
     */
    public static InputStream toInputStream(final String input, final Charset encoding) {
        return new ByteArrayInputStream(input.getBytes(toCharset(encoding)));
    }

    /**
     * 获取mmap
     *
     * @param path 文件
     * @return MappedByteBuffer
     * @throws IOException IOException
     */
    public static MappedByteBuffer toMmapBuffer(final Path path) throws IOException {
        RandomAccessFile rw = new RandomAccessFile(path.toFile(), "rw");
        FileChannel channel = rw.getChannel();
        return channel.map(FileChannel.MapMode.READ_WRITE, 0, channel.size());
    }

    /**
     * 字符串转流
     *
     * @param input    输入
     * @param encoding 编码
     * @return InputStream
     */
    public static InputStream toInputStream(final String input, final String encoding) {
        final byte[] bytes = input.getBytes(toCharset(encoding));
        return new ByteArrayInputStream(bytes);
    }

    /**
     * 字节数组转字符串
     *
     * @param input 字节数组
     * @return String
     */
    public static String toString(final byte[] input) {
        return new String(input, Charset.defaultCharset());
    }

    /**
     * url转字符串
     *
     * @param url      字节数组
     * @param encoding 编码
     * @return String
     * @throws IOException IOException
     */
    public static String toString(final URL url, final String encoding) throws IOException {
        return toString(url, toCharset(encoding));
    }

    /**
     * 字节数组转字符串
     *
     * @param input    字节数组
     * @param encoding 编码
     * @return String
     */
    public static String toString(final byte[] input, final String encoding) {
        return new String(input, toCharset(encoding));
    }

    /**
     * 流转字符串
     *
     * @param input    流
     * @param encoding 编码
     * @return String
     * @throws IOException IOException
     */
    public static String toString(final InputStream input, final Charset encoding) throws IOException {
        try (final Writer sw = new StringWriter()) {
            copy(input, sw, encoding);
            return sw.toString();
        }
    }

    /**
     * 流转字符串
     *
     * @param input    流
     * @param encoding 编码
     * @return String
     * @throws IOException IOException
     */
    public static String toString(final InputStream input, final String encoding) throws IOException {
        return toString(input, toCharset(encoding));
    }

    /**
     * 流转字符串
     *
     * @param input 流
     * @return String
     * @throws IOException IOException
     */
    public static String toString(final Reader input) throws IOException {
        try (final Writer sw = new StringWriter()) {
            copy(input, sw);
            return sw.toString();
        }
    }

    /**
     * URI 转字符串
     *
     * @param uri URI
     * @return String
     * @throws IOException IOException
     */
    public static String toString(final URI uri) throws IOException {
        return toString(uri, Charset.defaultCharset());
    }

    /**
     * URI 转字符串
     *
     * @param uri      URI
     * @param encoding 编码
     * @return String
     * @throws IOException IOException
     */
    public static String toString(final URI uri, final Charset encoding) throws IOException {
        return toString(uri.toURL(), toCharset(encoding));
    }

    /**
     * URL 转字符串
     *
     * @param url      URL
     * @param encoding 编码
     * @return String
     * @throws IOException IOException
     */
    public static String toString(final URL url, final Charset encoding) throws IOException {
        try (InputStream inputStream = url.openStream()) {
            return toString(inputStream, encoding);
        }
    }

    /**
     * 流转数组
     *
     * @param input    输入流
     * @param encoding 编码
     * @return byte
     * @throws IOException IOException
     */
    public static byte[] toByteArray(final Reader input, final Charset encoding) throws IOException {
        try (final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            copy(input, output, encoding);
            return output.toByteArray();
        }
    }

    /**
     * 输入流拷贝到输出流
     *
     * @param input   输入流
     * @param output  输出流
     * @param charset 编码
     * @throws IOException IOException
     */
    public static void copy(final InputStream input, final Writer output, final Charset charset)
            throws IOException {
        final InputStreamReader in = new InputStreamReader(input, toCharset(charset));
        copy(in, output);
    }

    /**
     * 输入流拷贝到输出流
     *
     * @param input  输入流
     * @param output 输出流
     * @return int
     * @throws IOException IOException
     */
    public static int copy(final InputStream input, final OutputStream output) throws IOException {
        final long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    /**
     * 拷贝文件
     *
     * @param input   输入流
     * @param output  输出流
     * @param charset 编码
     * @throws IOException IOException
     */
    public static void copy(final Reader input, final OutputStream output, final Charset charset)
            throws IOException {
        final OutputStreamWriter out = new OutputStreamWriter(output, toCharset(charset));
        copy(input, out);
        out.flush();
    }

    /**
     * 编码设置
     *
     * @param charset 编码
     * @return Charset
     */
    public static Charset toCharset(final Charset charset) {
        return charset == null ? Charset.defaultCharset() : charset;
    }

    /**
     * 编码设置
     *
     * @param charset 编码
     * @return Charset
     */
    public static Charset toCharset(final String charset) {
        return charset == null ? Charset.defaultCharset() : Charset.forName(charset);
    }

    /**
     * 拷贝文件
     *
     * @param input  输入流
     * @param output 输出流
     * @return int
     * @throws IOException IOException
     */
    public static int copy(final Reader input, final Writer output) throws IOException {
        final long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    /**
     * 拷贝文件
     *
     * @param input  输入流
     * @param output 输出流
     * @return long
     * @throws IOException IOException
     */
    public static long copy(final InputStream input, final OutputStream output, final int bufferSize) throws IOException {
        return copyLarge(input, output, new byte[bufferSize]);
    }

    /**
     * 拷贝大文件
     *
     * @param input  输入流
     * @param output 输出流
     * @return long
     * @throws IOException IOException
     */
    public static long copyLarge(final Reader input, final Writer output) throws IOException {
        return copyLarge(input, output, new char[DEFAULT_BUFFER_SIZE]);
    }

    /**
     * 拷贝大文件
     *
     * @param input  输入流
     * @param output 输出流
     * @param buffer 数组
     * @return long
     * @throws IOException IOException
     */
    public static long copyLarge(final Reader input, final Writer output, final char[] buffer) throws IOException {
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * 拷贝大文件
     *
     * @param input  输入流
     * @param output 输出流
     * @param buffer 数组
     * @return long
     * @throws IOException IOException
     */
    public static long copyLarge(final InputStream input, final OutputStream output, final byte[] buffer) throws IOException {
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * 拷贝大文件
     *
     * @param input  输入流
     * @param output 输出流
     * @return long
     * @throws IOException IOException
     */
    public static long copyLarge(final InputStream input, final OutputStream output) throws IOException {
        return copy(input, output, DEFAULT_BUFFER_SIZE);
    }

    /**
     * 写数据
     *
     * @param data     数据
     * @param output   流
     * @param encoding 编码
     * @throws IOException IOException
     */
    public static void write(final String data, final OutputStream output, final Charset encoding) throws IOException {
        if (data != null) {
            output.write(data.getBytes(toCharset(encoding)));
        }
    }

    /**
     * 写数据
     *
     * @param data   数据
     * @param output 流
     * @throws IOException IOException
     */
    public static void write(final byte[] data, final OutputStream output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }


    /**
     * 写数据
     *
     * @param data     数据
     * @param output   流
     * @param encoding 编码
     * @throws IOException IOException
     */
    public static void write(final String data, final OutputStream output, final String encoding) throws IOException {
        write(data, output, toCharset(encoding));
    }

    /**
     * 写数据
     *
     * @param data   数据
     * @param output 流
     * @throws IOException IOException
     */
    public static void write(final StringBuffer data, final Writer output) throws IOException {
        if (data != null) {
            output.write(data.toString());
        }
    }

    /**
     * 写数据
     *
     * @param data   数据
     * @param output 流
     * @throws IOException IOException
     */
    public static void write(final StringBuffer data, final OutputStream output) throws IOException {
        write(data, output, null);
    }

    /**
     * 写数据
     *
     * @param data     数据
     * @param output   流
     * @param encoding 编码
     * @throws IOException IOException
     */
    public static void write(final StringBuffer data, final OutputStream output, final String encoding) throws IOException {
        if (data != null) {
            output.write(data.toString().getBytes(toCharset(encoding)));
        }
    }

    /**
     * 写数据
     *
     * @param lines      数据
     * @param output     流
     * @param lineEnding 结尾
     * @throws IOException IOException
     */
    public static void writeLines(final Collection<?> lines, final String lineEnding, final OutputStream output) throws IOException {
        writeLines(lines, lineEnding, output, Charset.defaultCharset());
    }

    /**
     * 写数据
     *
     * @param lines      数据
     * @param output     流
     * @param lineEnding 结尾
     * @throws IOException IOException
     */
    public static void writeLines(final Collection<?> lines, String lineEnding, final OutputStream output,
                                  final Charset encoding) throws IOException {
        if (lines == null) {
            return;
        }
        if (lineEnding == null) {
            lineEnding = LINE_SEPARATOR;
        }
        final Charset cs = toCharset(encoding);
        for (final Object line : lines) {
            if (line != null) {
                output.write(line.toString().getBytes(cs));
            }
            output.write(lineEnding.getBytes(cs));
        }
    }

    /**
     * url 转 list
     *
     * @param url     类
     * @return List
     */
    public static List<String> toList(URL url) throws IOException {
        return Splitter.on("\r\n").omitEmptyStrings().trimResults().splitToList(toString(url, StandardCharsets.UTF_8));
    }
    /**
     * url 转 list
     *
     * @param url     类
     * @param charset 编码
     * @return List
     */
    public static List<String> toList(URL url, String charset) throws IOException {
        String s = toString(url, charset);
        return Splitter.on("\r\n").omitEmptyStrings().trimResults().splitToList(s);
    }

    /**
     * is转 reader
     *
     * @param inputStream 流
     * @return InputStreamReader
     */
    public static InputStreamReader toUtf8InputStreamReader(InputStream inputStream) {
        return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    }

    /**
     * is转 reader
     *
     * @param file 文件
     * @return InputStreamReader
     */
    public static InputStreamReader toUtf8InputStreamReader(String file) throws FileNotFoundException {
        return new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
    }

    /**
     * 流转字节
     *
     * @param openInputStream 输入流
     * @return ByteBuffer
     * @throws IOException IOException
     */
    public static ByteBuffer toByteBuffer(InputStream openInputStream) throws IOException {
        return ByteBuffer.wrap(toByteArray(openInputStream));
    }

    /**
     * 流拷贝
     *
     * @param inputStream 流
     * @return 流
     */
    public static ByteBuffer copyInputStream(InputStream inputStream) throws IOException {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // 定义一个缓存数组，临时存放读取的数组
            //经过测试，4*1024是一个非常不错的数字，过大过小都会比较影响性能
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > -1) {
                stream.write(buffer, 0, length);
            }
            stream.flush();
            return ByteBuffer.wrap(stream.toByteArray());
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
