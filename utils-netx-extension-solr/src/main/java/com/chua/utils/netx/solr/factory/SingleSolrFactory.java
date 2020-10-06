package com.chua.utils.netx.solr.factory;

import com.chua.utils.tools.properties.NetxProperties;
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

	public SingleSolrFactory(NetxProperties netxProperties) {
		super(netxProperties);
	}

    @Override
	public void configure(NetxProperties netxProperties) {
		this.netxProperties = netxProperties;
	}

	/**
	 * 开始
	 */
	@Override
	public void start() {
		log.info(">>>>>>>>>>> SolrFactory Starting to connect");
		String[] hosts = netxProperties.getHost();
		try {
			this.httpSolrClient = new HttpSolrClient.Builder(hosts[0])
						.withConnectionTimeout(netxProperties.getConnectionTimeout())
						.withSocketTimeout(netxProperties.getReadTimeout()).build();

			setSolrClient(httpSolrClient);
			log.info(">>>>>>>>>>> SolrFactory connection complete.");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(">>>>>>>>>>> SolrFactory connection activation failed.");
		}
	}

}
