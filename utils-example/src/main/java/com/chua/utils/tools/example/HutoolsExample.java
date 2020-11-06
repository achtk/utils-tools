package com.chua.utils.tools.example;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.*;
import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.collects.HashOperateMap;
import com.chua.utils.tools.common.ArraysHelper;
import com.chua.utils.tools.common.ObjectHelper;
import com.chua.utils.tools.example.entity.TDemoInfo;
import com.chua.utils.tools.process.ProcessHelper;
import com.chua.utils.tools.text.IdHelper;

import java.util.Map;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
public class HutoolsExample {

    public static void main(String[] args) {
        System.out.println(IdUtil.createSnowflake(3, 1).nextId());
        System.out.println(IdHelper.createSnowflake(3, 1).nextId());

        System.out.println(RuntimeUtil.execForStr("ping 127.0.0.1"));
        System.out.println(ProcessHelper.exec("ping 127.0.0.1"));


    }
}
