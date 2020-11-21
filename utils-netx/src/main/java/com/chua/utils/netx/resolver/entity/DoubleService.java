package com.chua.utils.netx.resolver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 双服务
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
@Getter
@Setter
public class DoubleService<S1, S2> extends Service<S1>{

    private S2 service2;

    public DoubleService(S1 service1) {
        super(service1);
    }

    public DoubleService(S1 service1, S2 service2) {
        super(service1);
        this.service2 = service2;
    }
}
