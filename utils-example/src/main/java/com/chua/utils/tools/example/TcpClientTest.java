package com.chua.utils.tools.example;

import com.chua.utils.netx.centor.ClientConfigCenter;
import com.chua.utils.netx.vertx.tcp.VertxNetClientConfigurationCenter;
import com.chua.utils.tools.handler.DataReaderHandler;
import com.chua.utils.tools.handler.DataWriteHandler;
import com.chua.utils.tools.properties.NetxProperties;
import io.vertx.core.buffer.Buffer;
import org.assertj.core.util.Lists;
import org.testng.annotations.Test;

/**
 * @author CH
 * @date 2020-10-07
 */
public class TcpClientTest {

	public static void main(String[] args) throws Throwable {
		NetxProperties netxProperties = new NetxProperties();
		netxProperties.setPort(43577);
		netxProperties.setHost("218.75.14.162");
		ClientConfigCenter clientConfigCenter = new VertxNetClientConfigurationCenter(netxProperties);
		clientConfigCenter.connect(Lists.newArrayList(new DemoDataWriteHandler(), new DemoDataReaderHandler()));
	}

	private static class DemoDataWriteHandler implements DataWriteHandler {
		@Override
		public Object getData() {
			return Buffer.buffer(new byte[0]);
		}

		@Override
		public void handler(Object o) {

		}
	}

	private static class DemoDataReaderHandler implements DataReaderHandler {
		@Override
		public void handler(Object o) {
			System.out.println();
		}
	}
}
