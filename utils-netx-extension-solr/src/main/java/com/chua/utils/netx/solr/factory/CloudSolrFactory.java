package com.chua.utils.netx.solr.factory;

import com.chua.utils.tools.properties.NetProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class CloudSolrFactory extends SolrFactory {

	private CloudSolrClient cloudSolrClient;

	public CloudSolrFactory(NetProperties netProperties) {
		super(netProperties);
	}

	@Override
	public void start() {
		log.info(">>>>>>>>>>> SolrFactory Starting to connect");

		String[] hosts = netProperties.getHost();

		try {
			this.cloudSolrClient =
					new CloudSolrClient.Builder(Arrays.asList(hosts), Optional.of("/solr"))
							.withConnectionTimeout(netProperties.getConnectionTimeout())
							.withSocketTimeout(netProperties.getReadTimeout()).build();

			this.cloudSolrClient.setDefaultCollection(SolrFactory.DEFAULT_COLLECTION);

			setSolrClient(cloudSolrClient);
			log.info(">>>>>>>>>>> SolrFactory connection complete.");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(">>>>>>>>>>> SolrFactory connection activation failed.");
		}

	}


}
