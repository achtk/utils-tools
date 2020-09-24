package com.chua.utils.netx.elasticsearch.config;

import com.chua.utils.netx.elasticsearch.action.CallBack;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 索引配置文件
 * 
 * @author Administrator
 */
@Getter
@Setter
@Accessors(chain = true)
public class IndexConfig {
	/**
	 * 索引
	 */
	private String index;
	/**
	 * 索引类型
	 */
	private String type = "doc";
	/**
	 * 版本
	 */
	private long version;
	/**
	 * 覆盖
	 */
	private boolean override;
	/**
	 * 主建
	 */
	private String id;
	/**
	 * 异步
	 */
	private boolean isAsync;
	/**
	 * 分片数
	 */
	private int shards;
	/**
	 * 副本数
	 */
	private int replicas;
	/**
	 * 是否实时
	 */
	private boolean realtime = true;
	/**
	 * 检索之前执行刷新(是)
	 */
	private boolean refresh = false;
	/**
	 * 文档 json: { "xxx": "xxx" }
	 */
	private String doc;
	/**
	 * 计数
	 */
	private int count;
	/**
	 * 总数
	 */
	private int max;
	/**
	 * 异步回调
	 */
	private CallBack callBack;

}
