package com.chua.utils.netx.ftp.resolver;

import com.chua.utils.netx.resolver.NetResolver;
import com.chua.utils.netx.resolver.entity.Service;
import com.chua.utils.netx.resolver.fs.FileServer;
import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.properties.NetProperties;
import com.chua.utils.tools.util.SystemUtil;
import lombok.SneakyThrows;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.util.Properties;


/**
 * ftp
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
public class FtpResolver extends NetResolver<FTPClient> implements FileServer {

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
}
