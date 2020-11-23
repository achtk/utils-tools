package com.chua.utils.netx.ftp.resolver;

import com.chua.utils.netx.resolver.NetResolver;
import com.chua.utils.netx.resolver.entity.Service;
import com.chua.utils.netx.resolver.fs.FileServer;
import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.common.IoHelper;
import com.google.common.base.Splitter;
import com.jcraft.jsch.*;
import lombok.SneakyThrows;
import org.omg.IOP.IORHelper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static com.chua.utils.tools.properties.NetProperties.*;

/**
 * ssh
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
public class SshResolver extends NetResolver<Session> implements FileServer {

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
        ChannelExec channel = null;
        try {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(name);
            channel.setInputStream(null);
            String string = IoHelper.toString(new InputStreamReader(channel.getInputStream()));
            return new HashSet<>(Splitter.on(",").splitToList(string));
        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            channel.disconnect();
        }
        return null;
    }

    @Override
    public boolean mkdir(String name) throws IOException {
        try {
            search(name);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteFile(String name) throws IOException {
        try {
            search(name);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean exist(String name) {
        try {
            search(name);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
