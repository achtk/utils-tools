package com.chua.utils.netx.resolver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 双服务
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
@Data
@AllArgsConstructor
public class Service<S1> {

    private S1 service1;
}
