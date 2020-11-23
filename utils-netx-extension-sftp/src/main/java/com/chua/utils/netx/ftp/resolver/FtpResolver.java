package com.chua.utils.netx.ftp.resolver;

import com.chua.utils.netx.resolver.NetResolver;
import com.chua.utils.netx.resolver.entity.Service;
import com.chua.utils.netx.resolver.fs.FileServer;
import com.chua.utils.netx.resolver.fs.RemoteFileServer;
import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.properties.NetProperties;
import com.chua.utils.tools.util.SystemUtil;
import lombok.SneakyThrows;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * ftp
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
public class FtpResolver extends NetResolver<FTPClient> implements FileServer, RemoteFileServer {

    private FTPClient ftpClient;

    @SneakyThrows
    @Override
    public void setProperties(Properties properties) {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(
                MapOperableHelper.getString(properties, NetProperties.CONFIG_FIELD_HOST),
                MapOperableHelper.getIntValue(properties, NetProperties.CONFIG_FIELD_PORT));
        if (SystemUtil.isWindow()) {
            ftpClient.setControlEncoding("GBK");
        } else {
            ftpClient.setControlEncoding("UTF-8");
        }
        if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            if (ftpClient.login(
                    MapOperableHelper.getString(properties, NetProperties.CONFIG_FIELD_USERNAME),
                    MapOperableHelper.getString(properties, NetProperties.CONFIG_FIELD_PASSWORD)
            )) {
                this.ftpClient = ftpClient;
            }
        }
        super.setProperties(properties);
    }

    @Override
    public Service<FTPClient> get() {
        return new Service<>(ftpClient);
    }

    @Override
    public Set<String> search(String name) throws IOException {
        FTPFile[] ftpFiles = ftpClient.listFiles(name);
        return Arrays.stream(ftpFiles).map(ftpFile -> ftpFile.getName()).collect(Collectors.toSet());
    }

    @Override
    public boolean mkdir(String name) throws IOException {
        return ftpClient.makeDirectory(name);
    }

    @Override
    public boolean deleteFile(String name) throws IOException {
        return ftpClient.deleteFile(name);
    }

    @Override
    public boolean exist(String name) {
        try {
            FTPFile[] ftpFiles = ftpClient.listFiles(name);
            if (ftpFiles.length < 1) {
                return ftpClient.listDirectories(name).length != 0;
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void download(String name, File localPath) throws IOException {
        //设置被动模式
        ftpClient.enterLocalPassiveMode();
        //设置以二进制方式传输
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        FTPFile[] ftpFiles = ftpClient.listFiles(name);
        if (null == ftpFiles || ftpFiles.length == 0) {
            throw new IOException("file does not exist");
        }
        Arrays.stream(ftpFiles).parallel().forEach(ftpFile -> {
            String fileName = ftpFile.getName();
            File temp = new File(localPath.getAbsolutePath(), fileName);
            byte[] bytes = new byte[2048];
            int line = 0;
            try (InputStream inputStream = ftpClient.retrieveFileStream(fileName);
                 FileOutputStream fileOutputStream = new FileOutputStream(temp)) {
                while ((line = inputStream.read(bytes)) != 0) {
                    fileOutputStream.write(bytes, 0, line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void upload(String path, ByteBuffer buffer) throws IOException {
        //设置PassiveMode传输
        ftpClient.enterLocalPassiveMode();
        //设置以二进制流的方式传输
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        //根目录
        String parentPath = FileHelper.getParentPath(path);
        try {
            ftpClient.makeDirectory(parentPath);
        } catch (IOException e) {
        }
        OutputStream outputStream = ftpClient.appendFileStream(path);
        outputStream.write(buffer.array());
    }

    @Override
    public void close() throws Exception {
        ftpClient.disconnect();
    }
}
