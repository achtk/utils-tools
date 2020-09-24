package com.chua.utils.netx.solr.factory;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.impl.CloudSolrClient;

import java.util.Arrays;
import java.util.Optional;

/**
 * solr工具类
 * 
 * @author CH
 *
 */
@Getter
@Setter
@Slf4j
public class CloudSolrFactory extends SolrFactory {

	private CloudSolrClient cloudSolrClient;

	@Override
	public void start() {
		String[] hosts = netxProperties.getHost();

		this.cloudSolrClient =
				new CloudSolrClient.Builder(Arrays.asList(hosts), Optional.of("/solr"))
						.withConnectionTimeout(netxProperties.getConnectionTimeout())
						.withSocketTimeout(netxProperties.getReadTimeout()).build();

		this.cloudSolrClient.setDefaultCollection(SolrFactory.DEFAULT_COLLECTION);

		setSolrClient(cloudSolrClient);

	}


}
