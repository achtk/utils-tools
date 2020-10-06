package com.chua.utils.tools.fegin.target;

import com.google.common.base.Splitter;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import feign.Request;
import feign.RequestTemplate;
import feign.Target;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * target
 * @author CH
 * @date 2020-10-06
 */
public class ContractTarget extends Target.HardCodedTarget {
	public ContractTarget(Class type, String url) {
		super(type, url);
	}

	@Override
	public Request apply(RequestTemplate input) {
		String url = url();
		if(url.indexOf(",") == -1) {
			if (input.url().indexOf("http") != 0) {
				input.insert(0, url());
			}
			return input.request();
		}
		ILoadBalancer loadBalancer = new BaseLoadBalancer();
		List<String> strings = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(url);
		List<Server> servers = new ArrayList<>(strings.size());
		for (String string : strings) {
			try {
				URL url1 = new URL(string);
				Server server = new Server(url1.getProtocol(), url1.getHost(), url1.getPort());
				servers.add(server);
			} catch (MalformedURLException e) {
				continue;
			}
		}
		loadBalancer.addServers(servers);
		Server server = loadBalancer.chooseServer(null);
		String string = server.getScheme() + "://" + server.getHost() + ":" + server.getPort();
		if (input.url().indexOf("http") != 0) {
			input.insert(0, string);
		}
		return input.request();
	}
}
