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

import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_COMMA;
import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_PROTOCOL_HTTP;

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
		if(url.indexOf(SYMBOL_COMMA) == -1) {
			if (input.url().indexOf(SYMBOL_PROTOCOL_HTTP) != 0) {
				input.insert(0, url());
			}
			return input.request();
		}
		ILoadBalancer loadBalancer = new BaseLoadBalancer();
		List<String> strings = Splitter.on(SYMBOL_COMMA).trimResults().omitEmptyStrings().splitToList(url);
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
		if (input.url().indexOf(SYMBOL_PROTOCOL_HTTP) != 0) {
			input.insert(0, string);
		}
		return input.request();
	}
}
