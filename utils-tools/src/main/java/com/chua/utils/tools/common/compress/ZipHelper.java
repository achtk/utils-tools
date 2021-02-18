package com.chua.utils.tools.common.compress;

import com.chua.utils.tools.common.ByteHelper;
import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.common.IoHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.*;

/**
 * zip
 *
 * @author CH
 */
public class ZipHelper {

    /**
     * 缓冲字节--1M
     */
    private static final int BUFF_SIZE = 1024 * 1024;

    private static final int DEFAULT_BYTE_ARRAY_LENGTH = 32;

    /**
     * 批量压缩文件(文件夹)
     *
     * @param resFileList 要压缩的文件(夹)列表
     * @param zipFile     生成的压缩文件
     * @throws IOException 当压缩过程出错时抛出
     */
    public static void zipFiles(Collection<File> resFileList, File zipFile) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(
                new BufferedOutputStream(new FileOutputStream(zipFile), BUFF_SIZE));
        for (File resFile : resFileList) {
            zipFile(resFile, zipOut, "");
        }
        zipOut.close();
    }

    /**
     * 批量压缩文件(文件夹)
     *
     * @param resFileList 要压缩的文件(夹)列表
     * @param zipFile     生成的压缩文件
     * @param comment     压缩文件的注释
     * @throws IOException 当压缩过程出错时抛出
     */
    public static void zipFiles(Collection<File> resFileList, File zipFile, String comment) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(
                new BufferedOutputStream(new FileOutputStream(zipFile), BUFF_SIZE));
        for (File resFile : resFileList) {
            zipFile(resFile, zipOut, "");
        }
        zipOut.setComment(comment);
        zipOut.close();
    }

    /**
     * 解压缩一个文件
     *
     * @param zipFile    压缩文件
     * @param folderPath 解压缩的目标目录
     * @throws IOException
     * @throws ZipException
     */
    public static void upZipFile(File zipFile, String folderPath) throws ZipException, IOException {
        // 根据路径创建一个文件
        File desDir = new File(folderPath);
        // 判断文件是否存在，如果不存在则创建
        if (!desDir.exists()) {
            desDir.mkdirs();
        }

        // 创建一个压缩文件
        try (ZipFile zFile = new ZipFile(zipFile)) {
            // 循环遍历
            for (Enumeration<?> entries = zFile.entries(); entries.hasMoreElements(); ) {
                InputStream in = null;
                OutputStream out = null;
                try {
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    in = zFile.getInputStream(entry);
                    String str = folderPath + File.separator + entry.getName();
                    str = new String(str.getBytes("8859_1"), "GB2312");
                    File desFile = new File(str);
                    // 判断文件是否存在
                    if (!desFile.exists()) {
                        File fileParentDir = desFile.getParentFile();
                        // 判断父文件夹是否存在
                        if (!fileParentDir.exists()) {
                            fileParentDir.mkdirs();
                        }
                        // 创建新文件
                        desFile.createNewFile();
                    }
                    out = new FileOutputStream(desFile);
                    byte[] buffer = new byte[BUFF_SIZE];
                    int realLength;
                    while ((realLength = in.read(buffer)) > 0) {
                        out.write(buffer, 0, realLength);
                    }
                } finally {
                    if (null != in) {
                        in.close();
                    }
                    if (null != out) {
                        out.close();
                    }
                }
            }

        }
    }

    /**
     * 解压文件名包含传入文字的文件
     *
     * @param zipFile      压缩文件
     * @param folderPath   目标文件夹
     * @param nameContains 传入的文件匹配名
     * @throws ZipException 压缩格式有误时抛出
     * @throws IOException  IO错误时抛出
     */
    public static ArrayList<File> upZipSelectedFile(File zipFile, String folderPath,
                                                    String nameContains) throws ZipException, IOException {
        ArrayList<File> fileList = new ArrayList<File>();

        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdir();
        }

        try (ZipFile zf = new ZipFile(zipFile)) {
            for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
                InputStream in = null;
                OutputStream out = null;
                try {
                    ZipEntry entry = ((ZipEntry) entries.nextElement());
                    if (entry.getName().contains(nameContains)) {
                        in = zf.getInputStream(entry);
                        String str = folderPath + File.separator + entry.getName();
                        str = new String(str.getBytes("8859_1"), "GB2312");
                        // str.getBytes("GB2312"),"8859_1" 输出
                        // str.getBytes("8859_1"),"GB2312" 输入
                        File desFile = new File(str);
                        if (!desFile.exists()) {
                            File fileParentDir = desFile.getParentFile();
                            if (!fileParentDir.exists()) {
                                fileParentDir.mkdirs();
                            }
                            desFile.createNewFile();
                        }
                        out = new FileOutputStream(desFile);
                        byte[] buffer = new byte[BUFF_SIZE];
                        int realLength;
                        while ((realLength = in.read(buffer)) > 0) {
                            out.write(buffer, 0, realLength);
                        }
                        fileList.add(desFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IoHelper.closeQuietly(in);
                    IoHelper.closeQuietly(out);
                }
            }
        }

        return fileList;
    }


    /**
     * 压缩文件
     *
     * @param resFile  需要压缩的文件(夹)
     * @param zipOut   压缩的目的文件
     * @param rootPath 压缩的文件路径
     * @throws IOException
     */
    private static void zipFile(File resFile, ZipOutputStream zipOut, String rootPath) throws IOException {
        // 判断文件路径长度是否大于0， 大于0时string为"/", 等于0时为""
        String string = rootPath.trim().length() == 0 ? "" : File.separator;
        // 压缩文件生成的路径
        rootPath = rootPath + string + resFile.getName();
        // 路径转码
        rootPath = new String(rootPath.getBytes("8859_1"), "GB2312");
        // 判断压缩的是否是路径
        if (resFile.isDirectory()) {
            // 获取当前路径下的所有文件
            File[] listFiles = resFile.listFiles();
            if (null != listFiles) {
                for (File file : listFiles) {
                    zipFile(file, zipOut, rootPath);
                }
            }
        } else {
            byte[] buffer = new byte[BUFF_SIZE];
            BufferedInputStream in = new BufferedInputStream(
                    new FileInputStream(resFile), BUFF_SIZE);
            zipOut.putNextEntry(new ZipEntry(rootPath));
            int realLength;
            while ((realLength = in.read(buffer)) != -1) {
                zipOut.write(buffer, 0, realLength);
            }
            in.close();
            zipOut.flush();
            zipOut.closeEntry();
        }
    }

    /**
     * 获得压缩文件内文件列表
     *
     * @param zipFile 压缩文件
     * @return 压缩文件内文件名称
     * @throws IOException
     * @throws ZipException
     */
    public static ArrayList<String> getEntriesNames(File zipFile) throws ZipException, IOException {
        ArrayList<String> entryNames = new ArrayList<>();
        Enumeration<?> entries = getEntriesEnumeration(zipFile);
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            entryNames.add(new String(
                    getEntryName(entry).getBytes("GB2312"), "8859_1"));
        }
        return entryNames;
    }

    /**
     * 获得压缩文件对象的名称
     *
     * @param entry 压缩文件对象
     * @return 压缩文件对象的名称
     * @throws UnsupportedEncodingException
     */
    private static String getEntryName(ZipEntry entry) throws UnsupportedEncodingException {
        return new String(entry.getName().getBytes("GB2312"), "8859_1");
    }

    /**
     * 取得压缩文件对象的注释
     *
     * @param entry 压缩文件对象
     * @return 压缩文件对象的注释
     * @throws UnsupportedEncodingException
     */
    public static String getEntryComment(ZipEntry entry) throws UnsupportedEncodingException {
        return new String(entry.getComment().getBytes("GB2312"), "8859_1");
    }

    /**
     * 获得压缩文件内压缩文件对象以取得其属性
     *
     * @param zipFile 压缩文件
     * @return 返回一个压缩文件列表
     * @throws IOException
     * @throws ZipException
     */
    private static Enumeration<?> getEntriesEnumeration(File zipFile) throws ZipException, IOException {
        try (ZipFile zf = new ZipFile(zipFile)) {
            return zf.entries();
        }
    }

    // ----------------------------------------------------------------------------- Gzip

    /**
     * Gzip压缩处理
     *
     * @param content 被压缩的字符串
     * @param charset 编码
     * @return 压缩后的字节流
     * @throws Exception IO异常
     */
    public static byte[] gzip(String content, String charset) throws Exception {
        return gzip(content.getBytes(charset));
    }

    /**
     * Gzip压缩处理
     *
     * @param buf 被压缩的字节流
     * @return 压缩后的字节流
     * @throws Exception IO异常
     */
    public static byte[] gzip(byte[] buf) throws Exception {
        return gzip(new ByteArrayInputStream(buf), buf.length);
    }

    /**
     * Gzip压缩文件
     *
     * @param file 被压缩的文件
     * @return 压缩后的字节流
     * @throws Exception IO异常
     */
    public static byte[] gzip(File file) throws Exception {
        BufferedInputStream in = null;
        try {
            in = FileHelper.getInputStream(file);
            return gzip(in, (int) file.length());
        } finally {
            IoHelper.closeQuietly(in);
        }
    }

    /**
     * Gzip压缩文件
     *
     * @param in 被压缩的流
     * @return 压缩后的字节流
     * @throws Exception IO异常
     * @since 4.1.18
     */
    public static byte[] gzip(InputStream in) throws Exception {
        return gzip(in, DEFAULT_BYTE_ARRAY_LENGTH);
    }

    /**
     * Gzip压缩文件
     *
     * @param in     被压缩的流
     * @param length 预估长度
     * @return 压缩后的字节流
     * @throws Exception IO异常
     * @since 4.1.18
     */
    public static byte[] gzip(InputStream in, int length) throws Exception {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
        GZIPOutputStream gos = null;
        try {
            gos = new GZIPOutputStream(bos);
            IoHelper.copy(in, gos);
        } catch (IOException e) {
            throw new Exception(e);
        } finally {
            IoHelper.closeQuietly(gos);
        }
        // 返回必须在关闭gos后进行，因为关闭时会自动执行finish()方法，保证数据全部写出
        return bos.toByteArray();
    }

    /**
     * Gzip解压缩处理
     *
     * @param buf     压缩过的字节流
     * @param charset 编码
     * @return 解压后的字符串
     * @throws Exception IO异常
     */
    public static String unGzip(byte[] buf, String charset) throws Exception {
        return ByteHelper.toString(unGzip(buf), charset);
    }

    /**
     * Gzip解压处理
     *
     * @param buf buf
     * @return bytes
     * @throws Exception IO异常
     */
    public static byte[] unGzip(byte[] buf) throws Exception {
        return unGzip(new ByteArrayInputStream(buf), buf.length);
    }

    /**
     * Gzip解压处理
     *
     * @param in Gzip数据
     * @return 解压后的数据
     * @throws Exception IO异常
     */
    public static byte[] unGzip(InputStream in) throws Exception {
        return unGzip(in, DEFAULT_BYTE_ARRAY_LENGTH);
    }

    /**
     * Gzip解压处理
     *
     * @param in     Gzip数据
     * @param length 估算长度，如果无法确定请传入{@link #DEFAULT_BYTE_ARRAY_LENGTH}
     * @return 解压后的数据
     * @throws Exception IO异常
     * @since 4.1.18
     */
    public static byte[] unGzip(InputStream in, int length) throws Exception {
        GZIPInputStream gzi = null;
        ByteArrayOutputStream bos;
        try {
            gzi = (in instanceof GZIPInputStream) ? (GZIPInputStream) in : new GZIPInputStream(in);
            bos = new ByteArrayOutputStream(length);
            IoHelper.copy(gzi, bos);
        } catch (IOException e) {
            throw new Exception(e);
        } finally {
            IoHelper.closeQuietly(gzi);
        }
        // 返回必须在关闭gos后进行，因为关闭时会自动执行finish()方法，保证数据全部写出
        return bos.toByteArray();
    }
}
