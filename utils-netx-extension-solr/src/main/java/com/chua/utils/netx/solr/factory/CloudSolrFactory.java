package com.chua.utils.netx.solr.factory;

import com.chua.unified.properties.NetxProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

	public CloudSolrFactory(NetxProperties netxProperties) {
		super(netxProperties);
	}

	@Override
	public void start() {
		log.info(">>>>>>>>>>> SolrFactory Starting to connect");

		String[] hosts = netxProperties.getHost();

		try {
			this.cloudSolrClient =
					new CloudSolrClient.Builder(Arrays.asList(hosts), Optional.of("/solr"))
							.withConnectionTimeout(netxProperties.getConnectionTimeout())
							.withSocketTimeout(netxProperties.getReadTimeout()).build();

			this.cloudSolrClient.setDefaultCollection(SolrFactory.DEFAULT_COLLECTION);

			setSolrClient(cloudSolrClient);
			log.info(">>>>>>>>>>> SolrFactory connection complete.");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(">>>>>>>>>>> SolrFactory connection activation failed.");
		}

	}


}
