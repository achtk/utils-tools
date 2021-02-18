package com.chua.tools.example;

import com.chua.tools.example.entity.TDemoInfo;
import com.chua.tools.example.entity.TDemoInfoImpl;
import com.chua.utils.tools.bean.modify.BeanModify;
import com.chua.utils.tools.bean.part.BeanPart;
import lombok.extern.slf4j.Slf4j;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
@Slf4j
public class BeanModifyExample extends BaseExample {

    public static void main(String[] args) throws Exception {
        TDemoInfo demoInfo = new TDemoInfoImpl();
        BeanModify<TDemoInfo> beanModify = BeanModify.of(demoInfo);
        beanModify.addField("data1", String.class);
        TDemoInfo toObject = beanModify.create().getToObject();
        BeanPart beanPart = BeanPart.of(toObject);
    }
}
