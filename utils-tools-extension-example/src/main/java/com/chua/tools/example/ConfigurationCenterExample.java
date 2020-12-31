package com.chua.tools.example;

import com.chua.utils.tools.properties.NetProperties;

/**
 * @author CH
 * @date 2020-10-08
 */
public class ConfigurationCenterExample {

	public void test() throws Exception {
		NetProperties netProperties = NetProperties.newProperty("127.0.0.1:2181");

//		ConfigurationCenter configurationCenter = new ZookeeperConfigurationCenter();
//		configurationCenter.initial(netxProperties);
//		configurationCenter.start();
//
//		CenterConfig centerConfig = new CenterConfig();
//		centerConfig.setName("demo");
//		centerConfig.put("demo_key_1", "233");
//
//		CenterConfig centerConfig1 = new CenterConfig();
//		centerConfig1.setName("demo1");
//		centerConfig1.put("demo_key_2", "233");
//		centerConfig1.put("demo_key_1", "444");
//		centerConfig1.setOrder(1);
//
//		configurationCenter.register(centerConfig);
//		configurationCenter.register(centerConfig1);
//		Object properties = configurationCenter.getIfOnly("demo_key_3");
//		System.out.println();
	}
}
