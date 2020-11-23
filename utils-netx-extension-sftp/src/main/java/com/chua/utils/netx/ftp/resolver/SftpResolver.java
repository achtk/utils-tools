package com.chua.utils.netx.ftp.resolver;

import com.chua.utils.netx.resolver.NetResolver;
import com.chua.utils.netx.resolver.entity.Service;
import com.chua.utils.netx.resolver.fs.FileServer;
import com.chua.utils.netx.resolver.fs.RemoteFileServer;
import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.common.IoHelper;
import com.google.common.base.Splitter;
import com.jcraft.jsch.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.stream.Collectors;

import static com.chua.utils.tools.properties.NetProperties.*;

/**
 * ssh
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
@Slf4j
public class SftpResolver extends NetResolver<Session> implements FileServer, RemoteFileServer {

    private Session session;

    @SneakyThrows
    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        JSch jSch = new JSch();
        Session session = jSch.getSession(
                MapOperableHelper.getString(properties, CONFIG_FIELD_USERNAME),
                MapOperableHelper.getString(properties, CONFIG_FIELD_HOST),
                MapOperableHelper.getIntValue(properties, CONFIG_FIELD_PORT)
        );
        Properties config = new Properties();
        // 不验证 HostKey
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        if (MapOperableHelper.isSafeValid(CONFIG_FIELD_PASSWORD)) {
            session.setPassword(MapOperableHelper.getString(properties, CONFIG_FIELD_PASSWORD));
        }

        if (MapOperableHelper.isSafeValid(CONFIG_FIELD_SESSION_TIMEOUT)) {
            session.setTimeout(MapOperableHelper.getIntValue(properties, CONFIG_FIELD_SESSION_TIMEOUT));
        }
        session.connect();
        this.session = session;
    }

    @Override
    public Service<Session> get() {
        return new Service<>(session);
    }

    @Override
    public Set<String> search(String name) throws IOException {
        ChannelSftp channel = null;
        try {
            channel = (ChannelSftp) session.openChannel("sftp");
            Vector<ChannelSftp.LsEntry> vector = channel.ls(name);
            return vector.stream().map(entry -> entry.getFilename()).collect(Collectors.toSet());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.disconnect();
        }
        return null;
    }

    @Override
    public boolean mkdir(String name) throws IOException {
        ChannelSftp channel = null;
        try {
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.mkdir(name);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.disconnect();
        }
        return false;
    }

    @Override
    public boolean deleteFile(String name) throws IOException {
        ChannelSftp channel = null;
        try {
            try {
                channel = (ChannelSftp) session.openChannel("sftp");
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            try {
                channel.rm(name);
            } catch (SftpException e) {
                e.printStackTrace();
                return false;
            }
            try {
                channel.rmdir(name);
            } catch (SftpException e) {
                e.printStackTrace();
                return false;
            }
        } finally {
            channel.disconnect();
        }
        return true;
    }

    @Override
    public boolean exist(String name) {
        ChannelSftp channel = null;
        try {
            channel = (ChannelSftp) session.openChannel("sftp");
            Vector ls = channel.ls(name);
            return 0 != ls.size();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.disconnect();
        }
        return false;
    }

    @Override
    public void download(String name, File localPath) throws IOException {
        ChannelSftp channel = null;
        try {
            channel = (ChannelSftp) session.openChannel("sftp");
            try (InputStream inputStream = channel.get(name)) {
                FileHelper.write(localPath, inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.disconnect();
        }
    }

    @Override
    public void upload(String path, ByteBuffer buffer) throws IOException {
        ChannelSftp channel = null;
        try {
            channel = (ChannelSftp) session.openChannel("sftp");
            try (OutputStream outputStream = channel.put(path)) {
                IoHelper.write(buffer.array(), outputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.disconnect();
        }
    }

    @Override
    public void close() throws Exception {
        session.disconnect();
    }
}
