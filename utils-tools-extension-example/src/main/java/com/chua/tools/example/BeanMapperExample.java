package com.chua.tools.example;

import com.chua.tools.example.entity.TDemoInfoImpl;
import com.chua.utils.tools.bean.map.BeanMapper;
import com.chua.utils.tools.logger.LogUtils;
import com.github.jsonzou.jmockdata.JMockData;
import lombok.extern.slf4j.Slf4j;

/**
 * Spi测试
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/29
 */
@Slf4j
public class BeanMapperExample extends BaseExample{

    public static void main(String[] args) {
        LogUtils.info("{}", BeanMapper.of(JMockData.mock(TDemoInfoImpl.class), "name -> name2").toMap());
        LogUtils.info("{}", BeanMapper.of(JMockData.mock(TDemoInfoImpl.class)).toObject(TDemoInfoImpl.class));
    }

}
