package com.chua.utils.netx.solr.factory;

import com.chua.unified.properties.NetxProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

/**
 * solr工具类
 * 
 * @author CH
 *
 */
@Getter
@Setter
@Slf4j
public class SingleSolrFactory extends SolrFactory {

	private HttpSolrClient httpSolrClient;
	private NetxProperties netxProperties;

	@Override
	public void configure(NetxProperties netxProperties) {
		this.netxProperties = netxProperties;
	}

	/**
	 * 开始
	 */
	@Override
	public void start() {
		String[] hosts = netxProperties.getHost();
		this.httpSolrClient = new HttpSolrClient.Builder(hosts[0])
					.withConnectionTimeout(netxProperties.getConnectionTimeout())
					.withSocketTimeout(netxProperties.getReadTimeout()).build();

		setSolrClient(httpSolrClient);
	}

}
