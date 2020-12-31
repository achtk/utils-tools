package com.chua.utils.tools.example;

import com.chua.utils.tools.classes.ClassAccess;
import com.chua.utils.tools.example.entity.TDemoInfo;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/30
 */
public class FtrExample {

    public static void main(String[] args) throws Exception {
        TDemoInfo tDemoInfo = new TDemoInfo();

        ClassAccess<TDemoInfo> classAccess = ClassAccess.build(TDemoInfo.class);
//        ClassAccess<TDemoInfo> classAccess = ClassAccessFactory.get(TDemoInfo.class);
        int getName = classAccess.methodIndex("getName");
        Object name = classAccess.invoke(tDemoInfo, getName, "");
        System.out.println(name);

    }
}
