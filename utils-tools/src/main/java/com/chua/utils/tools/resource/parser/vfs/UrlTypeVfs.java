package com.chua.utils.tools.resource.parser.vfs;

import com.chua.utils.tools.constant.StringConstant;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.resource.log.Log;
import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.compress.dir.ZipParserDir;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Predicate;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * url类型 vfs
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class UrlTypeVfs {

    private final static String[] REPLACE_EXTENSION = new String[]{
            ".ear/",
            ".jar/",
            ".war/",
            ".sar/",
            ".har/",
            ".par/"};
    private static final String VFS_ZIP = StringConstant.URL_PROTOCOL_VFSZIP;
    private static final String VFS_FILE = StringConstant.URL_PROTOCOL_VFSFILE;
    private final Pattern p = Pattern.compile("\\.[ejprw]ar/");
    Predicate<File> realFile = file -> file.exists() && file.isFile();

    public boolean matches(URL url) {
        return VFS_ZIP.equals(url.getProtocol()) || VFS_FILE.equals(url.getProtocol());
    }

    public ParserDir createDir(final URL url, final Matcher matcher) {
        try {
            URL adaptedUrl = adaptUrl(url);
            return new ZipParserDir(new JarFile(adaptedUrl.getFile()), matcher);
        } catch (Exception e) {
            try {
                return new ZipParserDir(new JarFile(url.getFile()), matcher);
            } catch (IOException e1) {
                Log.log.warn("Could not get URL", e);
                Log.log.warn("Could not get URL", e1);
            }
        }
        return null;
    }

    public URL adaptUrl(URL url) throws MalformedURLException {
        if (VFS_ZIP.equals(url.getProtocol())) {
            return replaceZipSeparators(url.getPath(), realFile);
        } else if (VFS_FILE.equals(url.getProtocol())) {
            return new URL(url.toString().replace(VFS_FILE, "file"));
        } else {
            return url;
        }
    }

    URL replaceZipSeparators(String path, Predicate<File> acceptFile)
            throws MalformedURLException {
        int pos = 0;
        while (pos != -1) {
            pos = findFirstMatchOfDeployableExtention(path, pos);

            if (pos > 0) {
                File file = new File(path.substring(0, pos - 1));
                if (acceptFile.test(file)) {
                    return replaceZipSeparatorStartingFrom(path, pos);
                }
            }
        }

        throw new IllegalStateException("Unable to identify the real zip file in path '" + path + "'.");
    }

    /**
     * @param path
     * @param pos
     * @return
     */
    int findFirstMatchOfDeployableExtention(String path, int pos) {
        java.util.regex.Matcher m = p.matcher(path);
        if (m.find(pos)) {
            return m.end();
        } else {
            return -1;
        }
    }

    URL replaceZipSeparatorStartingFrom(String path, int pos)
            throws MalformedURLException {
        String zipFile = path.substring(0, pos - 1);
        String zipPath = path.substring(pos);

        int numSubs = 1;
        for (String ext : REPLACE_EXTENSION) {
            while (zipPath.contains(ext)) {
                zipPath = zipPath.replace(ext, ext.substring(0, 4) + "!");
                numSubs++;
            }
        }

        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < numSubs; i++) {
            prefix.append("zip:");
        }

        if (zipPath.trim().length() == 0) {
            return new URL(prefix + "/" + zipFile);
        } else {
            return new URL(prefix + "/" + zipFile + "!" + zipPath);
        }
    }
}
