package com.chua.utils.tools.example;

import com.chua.utils.netx.centor.ConfigurationCenter;
import com.chua.utils.netx.entity.CenterConfig;
import com.chua.utils.netx.zookeeper.center.ZookeeperConfigurationCenter;
import com.chua.utils.tools.properties.NetxProperties;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * @author CH
 * @date 2020-10-08
 */
public class ConfigurationCenterTest {

	@Test
	public void test() throws Exception {
		NetxProperties netxProperties = NetxProperties.newProperty("127.0.0.1:2181");

		ConfigurationCenter configurationCenter = new ZookeeperConfigurationCenter();
		configurationCenter.initial(netxProperties);
		configurationCenter.start();

		CenterConfig centerConfig = new CenterConfig();
		centerConfig.setName("demo");
		centerConfig.put("demo_key_1", "233");

		CenterConfig centerConfig1 = new CenterConfig();
		centerConfig1.setName("demo1");
		centerConfig1.put("demo_key_2", "233");
		centerConfig1.put("demo_key_1", "444");
		centerConfig1.setOrder(1);

		configurationCenter.register(centerConfig);
		configurationCenter.register(centerConfig1);
		Object properties = configurationCenter.getIfOnly("demo_key_3");
		System.out.println();
	}
}
