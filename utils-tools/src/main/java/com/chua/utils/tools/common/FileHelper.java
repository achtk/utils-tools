package com.chua.utils.tools.common;

import com.chua.utils.tools.common.charset.CharsetHelper;
import com.chua.utils.tools.common.filecase.FileWildcard;
import com.chua.utils.tools.common.filecase.IOCase;
import com.chua.utils.tools.common.filefilter.*;
import com.chua.utils.tools.constant.StringConstant;
import com.chua.utils.tools.resource.Resource;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.io.*;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static com.chua.utils.tools.common.IoHelper.toCharset;
import static com.chua.utils.tools.constant.NumberConstant.INDEX_NOT_FOUND;
import static com.chua.utils.tools.constant.NumberConstant.TWE;
import static com.chua.utils.tools.constant.StringConstant.LETTER_A;
import static com.chua.utils.tools.constant.StringConstant.LETTER_Z;
import static com.chua.utils.tools.constant.SymbolConstant.*;
import static com.google.common.base.Charsets.UTF_8;

/**
 * 文件工具类
 *
 * @author CH
 */
public class FileHelper {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static final long ONE_KB = 1024;

    public static final BigInteger ONE_KB_BI = BigInteger.valueOf(ONE_KB);

    public static final long ONE_MB = ONE_KB * ONE_KB;

    public static final BigInteger ONE_MB_BI = ONE_KB_BI.multiply(ONE_KB_BI);

    private static final long FILE_COPY_BUFFER_SIZE = ONE_MB * 30;

    public static final long ONE_GB = ONE_KB * ONE_MB;

    public static final BigInteger ONE_GB_BI = ONE_KB_BI.multiply(ONE_MB_BI);

    public static final long ONE_TB = ONE_KB * ONE_GB;

    public static final BigInteger ONE_TB_BI = ONE_KB_BI.multiply(ONE_GB_BI);

    public static final long ONE_PB = ONE_KB * ONE_TB;

    public static final BigInteger ONE_PB_BI = ONE_KB_BI.multiply(ONE_TB_BI);

    public static final long ONE_EB = ONE_KB * ONE_PB;

    public static final BigInteger ONE_EB_BI = ONE_KB_BI.multiply(ONE_PB_BI);

    public static final BigInteger ONE_ZB = BigInteger.valueOf(ONE_KB).multiply(BigInteger.valueOf(ONE_EB));

    public static final BigInteger ONE_YB = ONE_KB_BI.multiply(ONE_ZB);

    public static final File[] EMPTY_FILE_ARRAY = new File[0];

    public static final Map<String, String> FILE_TYPE_MAP = new HashMap<>();

    private static final List<Object> IGNORE = new ArrayList<>();

    public static boolean isSystemWindows() {
        return SYMBOL_LEFT_SLASH_CHAR == SYMBOL_RIGHT_SLASH_CHAR;
    }

    /**
     * 删除文件
     *
     * @param root 目录
     * @return
     */
    public static boolean deleteRecursively(File root) {
        if (root != null && root.exists()) {
            if (root.isDirectory()) {
                File[] children = root.listFiles();
                if (children != null) {
                    for (File child : children) {
                        deleteRecursively(child);
                    }
                }
            }
            return root.delete();
        }
        return false;
    }

    /**
     * 获取文件类型
     *
     * @param file 文件  文件
     * @return fileType
     */
    public final static String getFileType(File file) {
        try {
            return getFileType(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * 获取url类型
     *
     * @param url
     * @return fileType
     */
    public final static String getFileType(URL url) {
        try {
            return getFileType(url.openStream());
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 获取is类型
     *
     * @param is
     * @return fileType
     */
    public final static String getFileType(InputStream is) {
        if (null == is) {
            return null;
        }
        byte[] b = new byte[20];
        try {
            is.read(b);
            return getFileType(b);
        } catch (IOException e) {
            return null;
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取文件类型
     *
     * @param bytes
     * @return fileType
     */
    public final static String getFileType(byte[] bytes) {
        String filetypeHex = String.valueOf(getFileHexString(bytes));
        Iterator<Map.Entry<String, String>> entryiterator = FILE_TYPE_MAP.entrySet().iterator();
        while (entryiterator.hasNext()) {
            Map.Entry<String, String> entry = entryiterator.next();
            String fileTypeHexValue = entry.getValue();
            if (filetypeHex.toUpperCase().startsWith(fileTypeHexValue)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * 获取 hex
     *
     * @param b
     * @return fileTypeHex
     */
    public final static String getFileHexString(byte[] b) {
        StringBuilder stringBuilder = new StringBuilder();
        if (b == null || b.length <= 0) {
            return null;
        }
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取temp目录
     *
     * @return
     */
    public static String getTempDirectoryPath() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * 获取temp目录
     *
     * @return
     */
    public static File getTempDirectory() {
        return new File(getTempDirectoryPath());
    }

    /**
     * 用户目录
     *
     * @return
     */
    public static String getUserDirectoryPath() {
        return System.getProperty("user.home");
    }

    /**
     * 用户目录
     *
     * @return
     */
    public static File getUserDirectory() {
        return new File(getUserDirectoryPath());
    }


    /**
     * 文件是否存在
     *
     * @param file 文件  文件 文件
     * @return
     */
    public static boolean isExist(File file) {
        return null != file && file.exists();
    }

    /**
     * 文件是否存在
     *
     * @param file 文件  文件 文件
     * @return
     */
    public static boolean isNotExist(File file) {
        return !isExist(file);
    }

    /**
     * 获取文件流
     *
     * @param file 文件  文件 文件
     * @return
     * @throws IOException
     */
    public static FileInputStream openInputStream(final File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canRead() == false) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }

    /**
     * 计算文件大小
     *
     * @param size 大小
     * @return
     */
    public static String byteCountToDisplaySize(final BigInteger size) {
        String displaySize;

        if (size.divide(ONE_EB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_EB_BI)) + " EB";
        } else if (size.divide(ONE_PB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_PB_BI)) + " PB";
        } else if (size.divide(ONE_TB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_TB_BI)) + " TB";
        } else if (size.divide(ONE_GB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_GB_BI)) + " GB";
        } else if (size.divide(ONE_MB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_MB_BI)) + " MB";
        } else if (size.divide(ONE_KB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_KB_BI)) + " KB";
        } else {
            displaySize = String.valueOf(size) + " bytes";
        }
        return displaySize;
    }

    /**
     * 计算文件大小
     *
     * @param size 大小
     * @return
     */
    public static String byteCountToDisplaySize(final long size) {
        return byteCountToDisplaySize(BigInteger.valueOf(size));
    }

    /**
     * 获取文件后缀
     *
     * @param extensions Format: {"java", "xml"}
     * @return Format: {".java", ".xml"}
     */
    public static String[] toSuffixes(final String[] extensions) {
        final String[] suffixes = new String[extensions.length];
        for (int i = 0; i < extensions.length; i++) {
            suffixes[i] = "." + extensions[i];
        }
        return suffixes;
    }

    /**
     * 获取文件
     *
     * @param url
     * @return
     */
    public static File toFile(final URL url) {
        if (url == null || !StringConstant.FILE.equalsIgnoreCase(url.getProtocol())) {
            return null;
        } else {
            String filename = url.getFile().replace('/', File.separatorChar);
            filename = decodeUrl(filename);
            return new File(filename);
        }
    }

    /**
     * 获取文件
     *
     * @param name
     * @return
     */
    public static File toFile(final String name) {
        File temp = new File(name);
        return temp.exists() && temp.isFile() ? temp : null;
    }

    /**
     * 拷贝文件到某个文件夹
     *
     * @param file      文件  文件      文件
     * @param directory 文件夹 文件夹
     * @return
     */
    public static void copyFile2Directory(final File file, File directory) throws IOException {
        if (!isFileAndExist(file)) {
            throw new FileNotFoundException("文件不存在");
        }
        isDirectoryAndCreate(directory);

        try (FileInputStream fileInputStream = new FileInputStream(file);
             FileChannel inputStreamChannel = fileInputStream.getChannel();
             FileOutputStream fos = new FileOutputStream(new File(directory.getName(), file.getName()));
             FileChannel outputStreamChannel = fos.getChannel()) {

            final long size = inputStreamChannel.size();
            long pos = 0;
            long cnt = 0;
            while (pos < size) {
                final long remain = size - pos;
                cnt = remain > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : remain;
                final long from = outputStreamChannel.transferFrom(inputStreamChannel, pos, cnt);
                if (from == 0) {
                    break;
                }
                pos += from;
            }
        }
    }

    /**
     * 是文件夹并且存在
     *
     * @param sourceDirectory 文件夹
     * @return
     */
    public static boolean isDirectoryAndExist(File sourceDirectory) {
        return null != sourceDirectory && sourceDirectory.isDirectory() && sourceDirectory.exists();
    }

    /**
     * 是文件夹不存在时创建
     *
     * @param directory 文件夹 文件夹
     */
    public static boolean isDirectoryAndCreate(File directory) throws NullPointerException, IllegalArgumentException {
        if (null == directory) {
            throw new NullPointerException("");
        }

        if (directory.isFile()) {
            throw new IllegalArgumentException("参数类型不正确, 必须为文件夹");
        }

        if (!directory.exists()) {
            directory.mkdirs();
        }
        return true;

    }

    /**
     * 文件存在并且是文件类型
     *
     * @param file 文件  文件 文件
     * @return
     */
    public static boolean isFileAndExist(File file) {
        return null != file && file.isFile() && file.exists();
    }

    /**
     * 文件存在并且是文件类型
     *
     * @param file 文件  文件 文件
     * @return
     */
    public static boolean isFileAndSuffix(File file, String suffix) {
        if (isFileAndExist(file)) {
            String extension = getExtension(file.getName());
            if (suffix.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 解析URL名称
     *
     * @param url url
     * @return
     */
    public static String decodeUrl(final String url) {
        String decoded = url;
        if (url != null && url.indexOf(SYMBOL_PER_CHAR) >= 0) {
            final int n = url.length();
            final StringBuilder buffer = new StringBuilder();
            final ByteBuffer bytes = ByteBuffer.allocate(n);
            for (int i = 0; i < n; ) {
                if (url.charAt(i) == SYMBOL_PER_CHAR) {
                    try {
                        do {
                            final byte octet = (byte) Integer.parseInt(url.substring(i + 1, i + 3), 16);
                            bytes.put(octet);
                            i += 3;
                        } while (i < n && url.charAt(i) == SYMBOL_PER_CHAR);
                        continue;
                    } catch (final RuntimeException e) {
                    } finally {
                        if (bytes.position() > 0) {
                            bytes.flip();
                            buffer.append(StandardCharsets.UTF_8.decode(bytes).toString());
                            bytes.clear();
                        }
                    }
                }
                buffer.append(url.charAt(i++));
            }
            decoded = buffer.toString();
        }
        return decoded;
    }

    /**
     * 获取指定目录下的文件
     *
     * @param path 目录
     * @param deep 是否获取子目录下的文件
     * @return
     */
    public static List<String> listFiles(final String path, final boolean deep, final FileFilter fileFilter) {
        List<String> result = new ArrayList<>();
        File temps = new File(path);
        File[] files = temps.listFiles(fileFilter);
        if (null != files) {
            for (File file : files) {
                if (file.isHidden()) {

                } else if (file.isDirectory()) {
                    if (deep) {
                        listFiles(result, path, fileFilter);
                    }
                } else if (file.isFile()) {
                    result.add(file.getPath());
                }
            }
        }
        return result;
    }

    /**
     * 获取列表
     *
     * @param directory  目录
     * @param extensions 后缀
     * @param recursive  永真
     * @return
     */
    public static Collection<File> listFiles(
            final File directory, final String[] extensions, final boolean recursive) {
        FileFilter filter;
        if (extensions == null) {
            filter = TrueFileFilter.INSTANCE;
        } else {
            final String[] suffixes = toSuffixes(extensions);
            filter = new SuffixFileFilter(suffixes);
        }
        return listFiles(directory, filter, recursive ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE);
    }

    /**
     * 获取列表
     *
     * @param directory  目录
     * @param fileFilter
     * @param dirFilter
     * @return
     */
    public static Collection<File> listFiles(
            final File directory, final FileFilter fileFilter, final FileFilter dirFilter) {
        validateListFilesParameters(directory, fileFilter);

        final FileFilter effFileFilter = setUpEffectiveFileFilter(fileFilter);
        final FileFilter effDirFilter = setUpEffectiveDirFilter(dirFilter);

        //Find files
        final Collection<File> files = new java.util.LinkedList<>();
        innerListFiles(files, directory, FileFilterHelper.or(effFileFilter, effDirFilter), false);
        return files;
    }

    /**
     * @param files
     * @param directory
     * @param filter
     * @param includeSubDirectories
     */
    private static void innerListFiles(final Collection<File> files, final File directory,
                                       final FileFilter filter, final boolean includeSubDirectories) {
        final File[] found = directory.listFiles((FileFilter) filter);

        if (found != null) {
            for (final File file : found) {
                if (file.isDirectory()) {
                    if (includeSubDirectories) {
                        files.add(file);
                    }
                    innerListFiles(files, file, filter, includeSubDirectories);
                } else {
                    files.add(file);
                }
            }
        }
    }

    /**
     * @param fileFilter
     * @return
     */
    private static FileFilter setUpEffectiveFileFilter(final FileFilter fileFilter) {
        return FileFilterHelper.and(fileFilter, FileFilterHelper.notFileFilter(DirectoryFileFilter.INSTANCE));
    }

    private static FileFilter setUpEffectiveDirFilter(final FileFilter dirFilter) {
        return dirFilter == null ? FalseFileFilter.INSTANCE : FileFilterHelper.and(dirFilter,
                DirectoryFileFilter.INSTANCE);
    }

    /**
     * 参数是否有效
     *
     * @param directory
     * @param fileFilter
     */
    private static void validateListFilesParameters(final File directory, final FileFilter fileFilter) {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Parameter 'directory' is not a directory: " + directory);
        }
        if (fileFilter == null) {
            throw new NullPointerException("Parameter 'fileFilter' is null");
        }
    }

    /**
     * 获取指定目录下的文件
     *
     * @param path     目录
     * @param fileList 文件列表
     * @return
     */
    public static void listFiles(final List<String> fileList, final String path, final FileFilter fileFilter) {
        File temps = new File(path);
        File[] files = null;
        if (null != fileFilter) {
            files = temps.listFiles(fileFilter);
        } else {
            files = temps.listFiles();
        }
        if (null != files) {
            for (File file : files) {
                if (file.isHidden()) {

                } else if (file.isDirectory()) {
                    listFiles(fileList, file.getPath(), fileFilter);
                } else if (file.isFile()) {
                    fileList.add(file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * 获取目录下的所有文件
     *
     * @param local
     * @return
     */
    public static List<String> listFiles(final String local) {
        List<String> fileList = new ArrayList<>();
        listFiles(fileList, local, null);
        return fileList;
    }


    /**
     * 获取文件虚拟目录
     *
     * @param filePath 文件路径
     * @return
     */
    public static String getParent(String filePath) {
        if (!Strings.isNullOrEmpty(filePath)) {
            filePath = filePath.replace(SYMBOL_RIGHT_SLASH, SYMBOL_LEFT_SLASH);
            int index = filePath.lastIndexOf(SYMBOL_LEFT_SLASH);
            return index > -1 ? filePath.substring(0, index + 1) : filePath;
        }
        return SYMBOL_EMPTY;
    }

    /**
     * 获取文件虚拟目录
     *
     * @param file 文件  文件 文件
     * @return
     */
    public static String getParent(File file) {
        if (null != file && file.exists()) {
            return getParent(file.getPath());
        }
        return SYMBOL_EMPTY;
    }

    /**
     * 获取父文件夹文件路径
     *
     * @param file 文件  文件 文件路径
     * @return
     */
    public static String getParentPath(File file) {
        if (null != file && file.exists()) {
            return getParentPath(file.getPath());
        }
        return SYMBOL_EMPTY;
    }

    /**
     * 获取父文件夹文件路径
     *
     * @param path 文件路径
     * @return
     */
    public static String getParentPath(String path) {
        String parent = getPath(path);
        if (!Strings.isNullOrEmpty(parent)) {
            return getPath(parent);
        }
        return SYMBOL_EMPTY;
    }

    /**
     * 获取父文件夹文件路径
     *
     * @param file 文件  文件 文件路径
     * @return
     */
    public static String getParentParent(File file) {
        if (null != file && file.exists()) {
            return getParentParent(file.getPath());
        }
        return SYMBOL_EMPTY;
    }

    /**
     * 获取父文件夹文件路径
     *
     * @param path 文件路径
     * @return
     */
    public static String getParentParent(String path) {
        String parent = getParent(path);
        if (!Strings.isNullOrEmpty(parent)) {
            return getParent(parent);
        }
        return SYMBOL_EMPTY;
    }

    /**
     * 获取文件虚拟目录
     *
     * @param file 文件  文件 文件
     * @return
     */
    public static String getPath(File file) {
        if (null != file && file.exists()) {
            return getPath(file.getPath());
        }
        return SYMBOL_EMPTY;
    }

    /**
     * 获取文件虚拟目录
     *
     * @param file 文件  文件 文件
     * @return
     */
    public static String getName(File file) {
        if (null != file && file.exists()) {
            return getName(file.getPath());
        }
        return SYMBOL_EMPTY;
    }

    /**
     * 获取真实路径
     *
     * @param path 文件路径
     * @return
     */
    public static String realyPath(String path) {
        return !Strings.isNullOrEmpty(path) ? new File(path).getPath() : SYMBOL_EMPTY;
    }

    /**
     * 文件路径模式匹配
     *
     * @param filePath 文件路径
     * @param model    匹配模式
     * @return
     */
    public static List<String> matcherPath(final URL filePath, final String model) {
        List<String> matches = new ArrayList<>();
        if (!Strings.isNullOrEmpty(model)) {
            PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + model);
            try {
                Path path = Paths.get(filePath.toURI());
                final boolean matches1 = pathMatcher.matches(path);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return matches;
    }

    /**
     * 强制删除文件夹
     *
     * @param folder 文件夹
     */
    public static void deleteForce(String folder) {
        try {
            deleteDirectory(new File(folder));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 强制删除文件
     *
     * @param url 文件
     */
    public static void deleteQuietly(final URL url) {
        if (null == url) {
            return;
        }
        File file = new File(url.getFile());
        try {
            file.delete();
        } catch (Exception e) {
        }
    }

    /**
     * 强制删除文件
     *
     * @param file 文件  文件
     * @return
     */
    public static boolean deleteQuietly(final File file) {
        if (file == null) {
            return false;
        }
        try {
            if (file.isDirectory()) {
                cleanDirectory(file);
            }
        } catch (final Exception ignored) {
        }

        try {
            return file.delete();
        } catch (final Exception ignored) {
            return false;
        }
    }

    /**
     * 强制删除
     *
     * @param file 文件  文件
     * @throws IOException
     */
    public static void forceDelete(final File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            final boolean filePresent = file.exists();
            if (!file.delete()) {
                if (!filePresent) {
                    throw new FileNotFoundException("File does not exist: " + file);
                }
                final String message =
                        "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }

    /**
     * 删除文件夹
     *
     * @param directory 文件夹
     * @throws IOException
     */
    public static void deleteDirectory(final File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        if (!isSymlink(directory)) {
            cleanDirectory(directory);
        }

        if (!directory.delete()) {
            final String message =
                    "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    /**
     * @param file 文件  文件
     * @return
     * @throws IOException
     */
    public static boolean isSymlink(final File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File must not be null");
        }
        return Files.isSymbolicLink(file.toPath());
    }

    /**
     * 清空文件夹
     *
     * @param directory 文件夹
     * @throws IOException
     */
    public static void cleanDirectory(final File directory) throws IOException {
        final File[] files = verifiedListFiles(directory);

        IOException exception = null;
        for (final File file : files) {
            try {
                forceDelete(file);
            } catch (final IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }

    /**
     * @param directory 文件夹
     * @return
     * @throws IOException
     */
    private static File[] verifiedListFiles(final File directory) throws IOException {
        if (!directory.exists()) {
            final String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            final String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }
        final File[] files = directory.listFiles();
        if (files == null) {
            throw new IOException("Failed to list contents of " + directory);
        }
        return files;
    }

    /**
     * 列举所有文件夹
     *
     * @param local 目录
     * @return
     */
    public static Collection<File> listSelfFolders(String local) {
        Collection<File> list = listFolders(local);
        list.add(file(local));
        return list;
    }

    /**
     * 列举所有文件夹
     *
     * @param local 目录
     * @return
     */
    public static Collection<File> listFolders(String local) {
        File file = file(local);
        List<File> fileList = new ArrayList<>();
        if (file.exists()) {
            File[] files = file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory();
                }
            });
            if (null != files) {
                fileList.addAll(Arrays.asList(files));
                for (File file1 : files) {
                    fileList.addAll(listFolders(file1.getPath()));
                }
            }
            return fileList;
        }
        return Collections.emptyList();
    }


    /**
     * 是否是文件夹
     *
     * @param local 路径
     * @return
     */
    public static boolean isFolder(String local) {
        return file(local).isDirectory();
    }

    /**
     * 是否是文件
     *
     * @param local 路径
     * @return
     */
    public static boolean isFile(String local) {
        return file(local).isFile();
    }


    /**
     * 是否是存在
     *
     * @param local 路径
     * @return
     */
    public static boolean isExist(String local) {
        return file(local).exists();
    }

    /**
     * 是否是存在
     *
     * @param local 路径
     * @return
     */
    public static boolean isExist(String local, final String file) {
        return new File(local, file).exists();
    }

    /**
     * 获取文件
     *
     * @param local 路径
     * @return
     */
    public static File file(String local) {
        return new File(local);
    }

    /**
     * 修改jar文件
     *
     * @param jarPath
     * @param fileName
     * @param content
     */
    public static void writeJar(final String jarPath, final String fileName, final String content) {
        /*File file = new File(jarPath + "/" + fileName);
        try (FileOutputStream fis = new FileOutputStream(file); JarFile jarFile = new JarFile(file)){
            JarOutputStream jarOutputStream = new JarOutputStream(fis);
            JarEntry jarEntry = jarFile.getJarEntry(fileName);
            if(null != jarEntry) {
                CRC32 crc32 = new CRC32();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 读取jar文件
     *
     * @param jarPath
     * @param fileName
     * @return
     */
    public static String readJar(final String jarPath, final String fileName) {
        try (JarFile jarFile = new JarFile(jarPath);) {
            if (null != jarFile) {
                JarEntry jarEntry = jarFile.getJarEntry(fileName);
                if (null != jarEntry) {
                    try (InputStream inputStream = jarFile.getInputStream(jarEntry)) {
                        return IoHelper.toString(inputStream, CharsetHelper.UTF_8);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            jarFile.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取后缀
     *
     * @param filePath
     * @return
     */
    public static String getSuffix(String filePath) {
        int lastIndexOf = filePath.lastIndexOf(".");
        return lastIndexOf > -1 ? filePath.substring(lastIndexOf + 1) : SYMBOL_EMPTY;
    }

    /**
     * 空url
     *
     * @return
     */
    public static URL emptyUrl() {
        try {
            return new URL("file:.");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取系统类型的文件路径
     *
     * @param name 文件路径
     * @return
     */
    public static String getFileName(String name) {
        return new File(name).getPath();
    }

    /**
     * 拷贝文件
     *
     * @param inputStream 流
     * @param file        文件  文件        拷贝目的文件
     * @return <p>-1 拷贝失败</p>
     */
    public static int copyFile(InputStream inputStream, File file) {
        try {
            return IoHelper.copy(inputStream, new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoHelper.closeQuietly(inputStream);
        }
        return -1;
    }

    /**
     * 获取简单的文件类型
     * <p>
     * FileHelper.getSimpleFileType("") = ""
     * FileHelper.getSimpleFileType(null) = ""
     * FileHelper.getSimpleFileType("1") = "1"
     * FileHelper.getSimpleFileType("1/test.") = ""
     * FileHelper.getSimpleFileType("1/test./") = ""
     * FileHelper.getSimpleFileType("1/test.txt") = "txt"
     * </p>
     *
     * @param name 文件名
     * @return 文件类型
     */
    public static String getSimpleFileType(String name) {
        if (StringHelper.isEmpty(name)) {
            return "";
        }
        int index = name.lastIndexOf("/");
        if (index != -1) {
            name = name.substring(index + 1);
        }
        int dotIndex = name.lastIndexOf(".");
        return dotIndex == -1 ? name : name.substring(dotIndex + 1);
    }

    /**
     * 是否为本地文件
     *
     * @param name
     * @return
     */
    public static boolean isLocalFile(String name) {
        File temp = new File(name);
        return temp.exists() && temp.isFile();
    }

    /**
     * 创建目录
     *
     * @param temp
     */
    public static void create(String temp) {
        if (null == temp) {
            return;
        }
        File tempPath = new File(temp);
        if (tempPath.isFile()) {
            File parentFile = tempPath.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
        } else {
            tempPath.mkdirs();
        }

    }

    /**
     * 创建文件夹
     *
     * @param path 文件
     */
    public static void mkFolders(final String path) {
        if (null == path) {
            return;
        }
        mkFolders(new File(path));
    }

    /**
     * 创建文件夹
     *
     * @param file 文件  文件 文件
     */
    public static void mkFolders(final File file) {
        if (null != file && !file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 创建文件父文件夹
     *
     * @param file 文件  文件 文件
     */
    public static void mkFile(final String file) {
        if (null == file) {
            return;
        }
        mkFile(new File(file));
    }

    /**
     * 创建文件父文件夹
     *
     * @param file 文件  文件 文件
     */
    public static void mkFile(final File file) {
        if (null != file) {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
        }
    }

    /**
     * 创建文件
     *
     * @param path 文件
     * @return
     */
    public static File newFile(final String path) {
        return null == path ? null : new File(path);
    }

    /**
     * 创建url
     *
     * @param path 路径
     * @param name 文件名
     * @return
     */
    public static URL newUrl(String path, String name) {
        File file = newFile(path);
        if (null == file) {
            return null;
        }
        try {
            return new File(path, name).toURI().toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * 获取目录下文件
     *
     * @param path      目录
     * @param extension 后缀
     * @return
     */
    public static Collection<String> files(String path, final String extension) {
        if (null == path) {
            return Collections.emptySet();
        }
        File file = newFile(path);
        String[] list = file.list();
        if (Strings.isNullOrEmpty(extension)) {
            return Sets.newHashSet(list);
        }

        Set<String> result = new HashSet<>();
        for (String oneFile : list) {
            if (wildcardMatch(oneFile, extension)) {
                result.add(oneFile);
            }
        }

        return result;
    }

    /**
     * 创建父目录
     *
     * @return
     */
    public static boolean createParentFolder(final File file) {
        return null == file ? false : createParentFolder(file.getAbsolutePath());
    }

    /**
     * 创建父目录
     *
     * @return
     */
    public static boolean createParentFolder(final String path) {
        if (Strings.isNullOrEmpty(path)) {
            return false;
        }
        File file = new File(path);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        return true;
    }

    /**
     * 创建当前目录
     *
     * @return
     */
    public static boolean createFolder(final File file) {
        return null == file ? false : createFolder(file.getAbsolutePath());
    }

    /**
     * 创建当前目录
     *
     * @return
     */
    public static boolean createFolder(final String path) {
        if (Strings.isNullOrEmpty(path)) {
            return false;
        }
        File file = new File(path);
        if (file.isDirectory() && !file.exists()) {
            file.mkdirs();
        }
        return true;
    }

    /**
     * 读取类相对路径内容
     *
     * @param clazz        文件
     * @param relativePath 相对路径
     * @param encoding     编码
     * @return 文件内容
     * @throws IOException 发送IO异常
     */
    public static String file2String(Class clazz, String relativePath, String encoding) throws IOException {
        Preconditions.checkArgument(null != clazz);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(relativePath));

        try (
                InputStream is = clazz.getResourceAsStream("/" + relativePath);
        ) {
            if (null == is) {
                return null;
            }
            InputStreamReader reader = new InputStreamReader(is, encoding);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder context = new StringBuilder();
            String lineText;
            while ((lineText = bufferedReader.readLine()) != null) {
                context.append(lineText).append(LINE_SEPARATOR);
            }
            return context.toString();
        }
    }

    /**
     * 删除存在的文件
     *
     * @param path
     */
    public static void deleteIfExist(String path) {
        if (Strings.isNullOrEmpty(path)) {
            throw new NullPointerException("path is not exist");
        }

        File temp = new File(path);
        if (temp.isDirectory()) {
            try {
                deleteDirectory(temp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            deleteQuietly(temp);
        }

    }

    /**
     * 获取广度文件夹/文件
     *
     * @param breadth      广度
     * @param parents      待检索目录
     * @param patternRegex 表达式
     * @param resourceSet  满足表达式结果
     * @return 广度文件夹
     */
    public static List<File> doFindFileAndPathByBreadth(int breadth, File parents, String patternRegex, Map<String, Resource> resourceSet) {
        addIntoResource(parents, patternRegex, resourceSet);
        if (breadth == 0) {
            return Lists.newArrayList(parents);
        }
        if (breadth == 1) {
            if (parents.isFile()) {
                return Collections.emptyList();
            }
            File[] files = parents.listFiles();
            if (!BooleanHelper.hasLength(files)) {
                return Collections.emptyList();
            }
            List<File> objects = Lists.newArrayList();
            for (File file : files) {
                if (ignoreFile(file)) {
                    continue;
                }
                objects.add(file);
            }

            return objects;
        }
        List<File> result = new ArrayList<>();
        File[] files = parents.listFiles();
        if (!BooleanHelper.hasLength(files)) {
            return Collections.emptyList();
        }
        for (File file : files) {
            result.addAll(doFindFileAndPathByBreadth(breadth - 1, file, patternRegex, resourceSet));
        }
        return result;
    }

    /**
     * 是否忽略文件
     *
     * @param file 文件  文件
     * @return
     */
    private static boolean ignoreFile(File file) {
        for (Object o : IGNORE) {
            if (wildcardMatch(file.toString(), o.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取深度文件夹/文件
     *
     * @param parents
     * @param parentFile   待检索深度文件夹
     * @param patternRegex 表达式
     * @param resourceSet  满足表达式结果
     */
    public static void doFindFileAndPathByDepth(File parents, List<File> parentFile, String patternRegex, Map<String, Resource> resourceSet) {
        ExecutorService executorService = ThreadHelper.newMinThreadExecutor(parentFile.size(), ThreadHelper.processor());
        CountDownLatch countDownLatch = new CountDownLatch(parentFile.size());
        final String parentName = parents.getPath().replace("\\", "/");
        final int parentLength = parentName.length();
        FileHelper.deleteQuietly(new File("D:\\test.txt"));

        for (File file : parentFile) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Path path1 = Paths.get(file.toURI());
                    try {
                        Files.walkFileTree(path1, new FileVisitor<Path>() {
                            @Override
                            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                                String dirName = dir.toString().replace("\\", "/");
                                FileHelper.write(new File("D:\\test.txt"), dir.toString() + "\r\n", UTF_8, true);
                                dirName = dirName.substring(parentLength);
                                if (StringHelper.wildcardMatch(dirName, patternRegex)) {
                                    Resource resource = Resource.getResource(dir.toUri().toURL());
                                    resourceSet.put(resource.getPath(), resource);
                                }
                                return FileVisitResult.CONTINUE;
                            }

                            @Override
                            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                                String dirName = file.toString().replace("\\", "/");
                                FileHelper.write(new File("D:\\test.txt"), file.toString() + "\r\n", UTF_8, true);
                                dirName = dirName.substring(parentLength);
                                if (StringHelper.wildcardMatch(dirName, patternRegex)) {
                                    Resource resource = Resource.getResource(file.toUri().toURL());
                                    resourceSet.put(resource.getPath(), resource);
                                }
                                return FileVisitResult.CONTINUE;
                            }

                            @Override
                            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                                return FileVisitResult.CONTINUE;
                            }

                            @Override
                            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                                return FileVisitResult.CONTINUE;
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });
        }

        try {
            countDownLatch.await();
            executorService.shutdownNow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据添加
     *
     * @param parents
     * @param patternRegex
     * @param resourceSet
     */
    private static void addIntoResource(File parents, String patternRegex, Map<String, Resource> resourceSet) {
        if (StringHelper.wildcardMatch(parents.toString(), patternRegex)) {
            Resource resource = null;
            try {
                resource = Resource.getResource(parents.toURI().toURL());
                resourceSet.put(resource.getPath(), resource);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化路径
     *
     * @param filepath
     * @return
     */
    public static String normalize(final String filepath) {
        if (null == filepath) {
            return null;
        }
        String newFilepath = filepath.replace(SYMBOL_RIGHT_SLASH, SYMBOL_LEFT_SLASH);
        return newFilepath.replace("//", SYMBOL_LEFT_SLASH)
                .replace("/./", SYMBOL_LEFT_SLASH);
    }

    /**
     * 文件后缀是否合法
     *
     * @param filename   文件名
     * @param extensions 后缀
     * @return
     */
    public static boolean isExtension(final String filename, final Collection<String> extensions) {
        if (filename == null) {
            return false;
        }
        failIfNullBytePresent(filename);

        if (extensions == null || extensions.isEmpty()) {
            return indexOfExtension(filename) == INDEX_NOT_FOUND;
        }
        final String fileExt = getExtension(filename);
        for (final String extension : extensions) {
            if (fileExt.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 文件后缀是否合法
     *
     * @param filename  文件名
     * @param extension 后缀
     * @return
     */
    public static boolean isExtension(final String filename, final String extension) {
        if (filename == null) {
            return false;
        }
        failIfNullBytePresent(filename);

        if (extension == null || extension.isEmpty()) {
            return indexOfExtension(filename) == INDEX_NOT_FOUND;
        }
        final String fileExt = getExtension(filename);
        return fileExt.equals(extension);
    }

    /**
     * 文件后缀是否合法
     *
     * @param filename   文件名
     * @param extensions 后缀
     * @return
     */
    public static boolean isExtension(final String filename, final String[] extensions) {
        if (filename == null) {
            return false;
        }
        failIfNullBytePresent(filename);

        if (extensions == null || extensions.length == 0) {
            return indexOfExtension(filename) == INDEX_NOT_FOUND;
        }
        final String fileExt = getExtension(filename);
        for (final String extension : extensions) {
            if (fileExt.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取后缀
     * <pre>
     * foo.txt      --&gt; "txt"
     * a/b/c.jpg    --&gt; "jpg"
     * a/b.txt/c    --&gt; ""
     * a/b/c        --&gt; ""
     * </pre>
     *
     * @param filename the filename to retrieve the extension of.
     * @return the extension of the file or an empty string if none exists or {@code null}
     * if the filename is {@code null}.
     */
    public static String getExtension(final String filename) {
        if (filename == null) {
            return null;
        }
        final int index = indexOfExtension(filename);
        if (index == INDEX_NOT_FOUND) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }

    /**
     * 获取基础名称
     * <pre>
     * a/b/c.txt --&gt; c
     * a.txt     --&gt; a
     * a/b/c     --&gt; c
     * a/b/c/    --&gt; ""
     * </pre>
     *
     * @param filename the filename to query, null returns null
     * @return the name of the file without the path, or an empty string if none exists. Null bytes inside string
     * will be removed
     */
    public static String getBaseName(final String filename) {
        return removeExtension(getName(filename));
    }
    //-----------------------------------------------------------------------

    /**
     * 删除后缀
     * foo.txt    --&gt; foo
     * a\b\c.jpg  --&gt; a\b\c
     * a\b\c      --&gt; a\b\c
     * a.b\c      --&gt; a.b\c
     * </pre>
     *
     * @param filename the filename to query, null returns null
     * @return the filename minus the extension
     */
    public static String removeExtension(final String filename) {
        if (filename == null) {
            return null;
        }
        failIfNullBytePresent(filename);

        final int index = indexOfExtension(filename);
        if (index == INDEX_NOT_FOUND) {
            return filename;
        } else {
            return filename.substring(0, index);
        }
    }

    /**
     * 获取全路径
     * C:\a\b\c.txt --&gt; C:\a\b\
     * ~/a/b/c.txt  --&gt; ~/a/b/
     * a.txt        --&gt; ""
     * a/b/c        --&gt; a/b/
     * a/b/c/       --&gt; a/b/c/
     * C:           --&gt; C:
     * C:\          --&gt; C:\
     * ~            --&gt; ~/
     * ~/           --&gt; ~/
     * ~user        --&gt; ~user/
     * ~user/       --&gt; ~user/
     * </pre>
     *
     * @param filename the filename to query, null returns null
     * @return the path of the file, an empty string if none exists, null if invalid
     */
    public static String getFullPath(final String filename) {
        return doGetFullPath(filename, true);
    }

    /**
     * 获取路径
     * <pre>
     * C:\a\b\c.txt --&gt; a\b\
     * ~/a/b/c.txt  --&gt; a/b/
     * a.txt        --&gt; ""
     * a/b/c        --&gt; a/b/
     * a/b/c/       --&gt; a/b/c/
     * </pre>
     *
     * @param filename the filename to query, null returns null
     * @return the path of the file, an empty string if none exists, null if invalid.
     * Null bytes inside string will be removed
     */
    public static String getPath(final String filename) {
        return doGetPath(filename, 1);
    }

    /**
     * 获取前缀
     * <pre>
     * Windows:
     * a\b\c.txt           --&gt; ""          --&gt; relative
     * \a\b\c.txt          --&gt; "\"         --&gt; current drive absolute
     * C:a\b\c.txt         --&gt; "C:"        --&gt; drive relative
     * C:\a\b\c.txt        --&gt; "C:\"       --&gt; absolute
     * \\server\a\b\c.txt  --&gt; "\\server\" --&gt; UNC
     *
     * Unix:
     * a/b/c.txt           --&gt; ""          --&gt; relative
     * /a/b/c.txt          --&gt; "/"         --&gt; absolute
     * ~/a/b/c.txt         --&gt; "~/"        --&gt; current user
     * ~                   --&gt; "~/"        --&gt; current user (slash added)
     * ~user/a/b/c.txt     --&gt; "~user/"    --&gt; named user
     * ~user               --&gt; "~user/"    --&gt; named user (slash added)
     * </pre>
     *
     * @param filename the filename to query, null returns null
     * @return the prefix of the file, null if invalid. Null bytes inside string will be removed
     */
    public static String getPrefix(final String filename) {
        if (filename == null) {
            return null;
        }
        final int len = getPrefixLength(filename);
        if (len < 0) {
            return null;
        }
        if (len > filename.length()) {
            failIfNullBytePresent(filename + SYMBOL_LEFT_SLASH);
            return filename + SYMBOL_LEFT_SLASH;
        }
        final String path = filename.substring(0, len);
        failIfNullBytePresent(path);
        return path;
    }

    /**
     * Does the work of getting the path.
     *
     * @param filename         the filename
     * @param includeSeparator true to include the end separator
     * @return the path
     */
    private static String doGetFullPath(final String filename, final boolean includeSeparator) {
        if (filename == null) {
            return null;
        }
        final int prefix = getPrefixLength(filename);
        if (prefix < 0) {
            return null;
        }
        if (prefix >= filename.length()) {
            if (includeSeparator) {
                return getPrefix(filename);
            } else {
                return filename;
            }
        }
        final int index = indexOfLastSeparator(filename);
        if (index < 0) {
            return filename.substring(0, prefix);
        }
        int end = index + (includeSeparator ? 1 : 0);
        if (end == 0) {
            end++;
        }
        return filename.substring(0, end);
    }

    /**
     * Returns the length of the filename prefix, such as <code>C:/</code> or <code>~/</code>.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * <p>
     * The prefix length includes the first slash in the full filename
     * if applicable. Thus, it is possible that the length returned is greater
     * than the length of the input string.
     * <pre>
     * Windows:
     * a\b\c.txt           --&gt; ""          --&gt; relative
     * \a\b\c.txt          --&gt; "\"         --&gt; current drive absolute
     * C:a\b\c.txt         --&gt; "C:"        --&gt; drive relative
     * C:\a\b\c.txt        --&gt; "C:\"       --&gt; absolute
     * \\server\a\b\c.txt  --&gt; "\\server\" --&gt; UNC
     * \\\a\b\c.txt        --&gt;  error, length = -1
     *
     * Unix:
     * a/b/c.txt           --&gt; ""          --&gt; relative
     * /a/b/c.txt          --&gt; "/"         --&gt; absolute
     * ~/a/b/c.txt         --&gt; "~/"        --&gt; current user
     * ~                   --&gt; "~/"        --&gt; current user (slash added)
     * ~user/a/b/c.txt     --&gt; "~user/"    --&gt; named user
     * ~user               --&gt; "~user/"    --&gt; named user (slash added)
     * //server/a/b/c.txt  --&gt; "//server/"
     * ///a/b/c.txt        --&gt; error, length = -1
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * ie. both Unix and Windows prefixes are matched regardless.
     * <p>
     * Note that a leading // (or \\) is used to indicate a UNC name on Windows.
     * These must be followed by a server name, so double-slashes are not collapsed
     * to a single slash at the start of the filename.
     *
     * @param filename the filename to find the prefix in, null returns -1
     * @return the length of the prefix, -1 if invalid or null
     */
    public static int getPrefixLength(final String filename) {
        if (filename == null) {
            return INDEX_NOT_FOUND;
        }
        final int len = filename.length();
        if (len == 0) {
            return 0;
        }
        char ch0 = filename.charAt(0);
        if (ch0 == SYMBOL_COLON_CHAR) {
            return INDEX_NOT_FOUND;
        }
        if (len == 1) {
            if (ch0 == SYMBOL_WAVY_LINE_CHAR) {
                return 2;
            }
            return isSeparator(ch0) ? 1 : 0;
        } else {
            if (ch0 == SYMBOL_WAVY_LINE_CHAR) {
                int posUnix = filename.indexOf(SYMBOL_LEFT_SLASH, 1);
                int posWin = filename.indexOf(SYMBOL_RIGHT_SLASH, 1);
                if (posUnix == INDEX_NOT_FOUND && posWin == INDEX_NOT_FOUND) {
                    return len + 1;
                }
                posUnix = posUnix == INDEX_NOT_FOUND ? posWin : posUnix;
                posWin = posWin == INDEX_NOT_FOUND ? posUnix : posWin;
                return Math.min(posUnix, posWin) + 1;
            }
            final char ch1 = filename.charAt(1);
            if (ch1 == SYMBOL_COLON_CHAR) {
                ch0 = Character.toUpperCase(ch0);
                if (ch0 >= LETTER_A && ch0 <= LETTER_Z) {
                    if (len == TWE || isSeparator(filename.charAt(TWE)) == false) {
                        return TWE;
                    }
                    return 3;
                } else if (ch0 == SYMBOL_LEFT_SLASH_CHAR) {
                    return 1;
                }
                return INDEX_NOT_FOUND;

            } else if (isSeparator(ch0) && isSeparator(ch1)) {
                int posUnix = filename.indexOf(SYMBOL_LEFT_SLASH, 2);
                int posWin = filename.indexOf(SYMBOL_RIGHT_SLASH, 2);
                if (isNotFound(posUnix, posWin)) {
                    return INDEX_NOT_FOUND;
                }
                posUnix = posUnix == INDEX_NOT_FOUND ? posWin : posUnix;
                posWin = posWin == INDEX_NOT_FOUND ? posUnix : posWin;
                return Math.min(posUnix, posWin) + 1;
            } else {
                return isSeparator(ch0) ? 1 : 0;
            }
        }
    }

    /**
     * 未找到
     *
     * @param posUnix unix
     * @param posWin  win
     * @return boolean
     */
    private static boolean isNotFound(int posUnix, int posWin) {
        return posUnix == INDEX_NOT_FOUND
                && posWin == posUnix || posUnix == TWE || posWin == posUnix;
    }

    /**
     * Checks if the character is a separator.
     *
     * @param ch the character to check
     * @return true if it is a separator character
     */
    private static boolean isSeparator(final char ch) {
        return ch == SYMBOL_LEFT_SLASH_CHAR || ch == SYMBOL_RIGHT_SLASH_CHAR;
    }

    /**
     * 获取名称
     * <pre>
     * a/b/c.txt --&gt; c.txt
     * a.txt     --&gt; a.txt
     * a/b/c     --&gt; c
     * a/b/c/    --&gt; ""
     * </pre>
     * <p>
     *
     * @param filename the filename to query, null returns null
     * @return the name of the file without the path, or an empty string if none exists.
     * Null bytes inside string will be removed
     */
    public static String getName(final String filename) {
        if (filename == null) {
            return null;
        }
        failIfNullBytePresent(filename);
        final int index = indexOfLastSeparator(filename);
        return filename.substring(index + 1);
    }

    /**
     * 后缀索引
     *
     * @param filename
     * @return
     */
    public static int indexOfExtension(final String filename) {
        if (filename == null) {
            return INDEX_NOT_FOUND;
        }
        final int extensionPos = filename.lastIndexOf(SYMBOL_DOT);
        final int lastSeparator = indexOfLastSeparator(filename);
        return lastSeparator > extensionPos ? INDEX_NOT_FOUND : extensionPos;
    }

    /**
     * 后缀索引
     *
     * @param filename
     * @return
     */
    public static int indexOfLastSeparator(final String filename) {
        if (filename == null) {
            return INDEX_NOT_FOUND;
        }
        final int lastUnixPos = filename.lastIndexOf(SYMBOL_LEFT_SLASH);
        final int lastWindowsPos = filename.lastIndexOf(SYMBOL_RIGHT_SLASH);
        return Math.max(lastUnixPos, lastWindowsPos);
    }

    /**
     * @param path
     */
    private static void failIfNullBytePresent(final String path) {
        final int len = path.length();
        for (int i = 0; i < len; i++) {
            if (path.charAt(i) == 0) {
                throw new IllegalArgumentException("Null byte present in file/path name. There are no " +
                        "known legitimate use cases for such data, but several injection attacks may use it");
            }
        }
    }

    private static String doGetPath(final String filename, final int separatorAdd) {
        if (filename == null) {
            return null;
        }
        final int prefix = getPrefixLength(filename);
        if (prefix < 0) {
            return null;
        }
        final int index = indexOfLastSeparator(filename);
        final int endIndex = index + separatorAdd;
        if (prefix >= filename.length() || index < 0 || prefix >= endIndex) {
            return "";
        }
        final String path = filename.substring(prefix, endIndex);
        failIfNullBytePresent(path);
        return path;
    }

    /**
     * Checks a filename to see if it matches the specified wildcard matcher,
     * always testing case-sensitive.
     * <p>
     * The wildcard matcher uses the characters '?' and '*' to represent a
     * single or multiple (zero or more) wildcard characters.
     * This is the same as often found on Dos/Unix command lines.
     * The check is case-sensitive always.
     * <pre>
     * wildcardMatch("c.txt", "*.txt")      --&gt; true
     * wildcardMatch("c.txt", "*.jpg")      --&gt; false
     * wildcardMatch("a/b/c.txt", "a/b/*")  --&gt; true
     * wildcardMatch("c.txt", "*.???")      --&gt; true
     * wildcardMatch("c.txt", "*.????")     --&gt; false
     * </pre>
     * N.B. the sequence "*?" does not work properly at present in match
     *
     * @param filename        the filename to match on
     * @param wildcardMatcher the wildcard string to match against
     * @return true if the filename matches the wildcard string
     */
    public static boolean wildcardMatch(final String filename, final String wildcardMatcher) {
        return FileWildcard.wildcardMatch(filename, wildcardMatcher, IOCase.SYSTEM);
    }

    /**
     * 文件转流
     *
     * @param path
     * @return
     */
    public static InputStream toInputStream(String path) throws IOException {
        if (null == path) {
            return null;
        }
        return toInputStream(new File(path));
    }

    /**
     * 文件转流
     *
     * @param file 文件
     * @return
     */
    public static InputStream toInputStream(File file) throws IOException {
        if (null == file) {
            return null;
        }
        URL url = file.toURI().toURL();
        return url.openStream();
    }

    /**
     * 写文件
     *
     * @param file     文件
     * @param data     数据
     * @param encoding 编码
     * @throws IOException
     */
    public static void write(final File file, final CharSequence data, final Charset encoding) throws IOException {
        write(file, data, encoding, false);
    }

    /**
     * 写文件
     *
     * @param file     文件
     * @param data     数据
     * @param encoding 编码
     * @throws IOException
     */
    public static void write(final File file, final CharSequence data, final String encoding) throws IOException {
        write(file, data, encoding, false);
    }

    /**
     * 写文件
     *
     * @param file     文件
     * @param data     数据
     * @param encoding 编码
     * @param append   追加
     * @throws IOException
     */
    public static void write(final File file, final CharSequence data, final String encoding, final boolean append)
            throws IOException {
        write(file, data, toCharset(encoding), append);
    }

    /**
     * 写文件
     *
     * @param file 文件
     * @throws IOException
     */
    public static FileOutputStream openOutputStream(final File file) throws IOException {
        return openOutputStream(file, false);
    }

    /**
     * 写文件
     *
     * @param file     文件
     * @param data     数据
     * @param encoding 编码
     * @param append   追加
     * @throws IOException
     */
    public static void write(final File file, final CharSequence data, final Charset encoding, final boolean append)
            throws IOException {
        final String str = data == null ? null : data.toString();
        writeStringToFile(file, str, encoding, append);
    }

    /**
     * 写文件
     *
     * @param file     文件
     * @param data     数据
     * @param encoding 编码
     * @param append   追加
     * @throws IOException
     */
    public static void writeStringToFile(final File file, final String data, final Charset encoding,
                                         final boolean append) throws IOException {
        try (OutputStream out = openOutputStream(file, append)) {
            IoHelper.write(data, out, encoding);
        }
    }

    /**
     * 打开文件
     *
     * @param file   文件
     * @param append 是否追加
     * @return
     * @throws IOException
     */
    public static FileOutputStream openOutputStream(final File file, final boolean append) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canWrite() == false) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            final File parent = file.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file, append);
    }

    /**
     * 新文件
     *
     * @param file
     * @param timeMillis
     * @return
     */
    public static boolean isFileNewer(final File file, final long timeMillis) {
        if (file == null) {
            throw new IllegalArgumentException("No specified file");
        }
        if (!file.exists()) {
            return false;
        }
        return file.lastModified() > timeMillis;
    }

    /**
     * 获取文件夹
     *
     * @param folder 文件夹
     * @return
     */
    public static String toFolder(final String folder) {
        return Strings.isNullOrEmpty(folder) ? SYMBOL_EMPTY : (folder.endsWith(SYMBOL_LEFT_SLASH) ? folder : folder + SYMBOL_LEFT_SLASH);
    }

    /**
     * 是否是window
     *
     * @return boolean
     */
    public static boolean isWindows() {
        return SYMBOL_RIGHT_SLASH_CHAR == File.separatorChar;
    }

    /**
     * 获取流
     *
     * @param file 文件
     * @return
     */
    public static BufferedInputStream getInputStream(File file) {
        try {
            return new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
