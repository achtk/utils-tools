package com.chua.tools.example;

import com.chua.tools.example.entity.TDemoInfoImpl;
import com.chua.tools.example.utils.MockUtils;
import com.chua.utils.tools.order.Ordered;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Spi测试
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/29
 */
@Slf4j
public class OrderedExample extends BaseExample{

    public static void main(String[] args) {
        List<TDemoInfoImpl> list = MockUtils.createForList(TDemoInfoImpl.class);
        Ordered.asc(list, TDemoInfoImpl::getId);
        System.out.println(list);
    }

}
