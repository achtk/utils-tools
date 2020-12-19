package com.chua.utils.tools.example;

import com.chua.utils.tools.annotations.Binder;
import com.chua.utils.tools.bean.creator.ValueCreator;
import com.chua.utils.tools.bean.script.ValueScript;
import com.chua.utils.tools.collects.OperateHashMap;
import com.chua.utils.tools.example.interfaces.ITestInterface;
import com.chua.utils.tools.spi.factory.ExtensionFactory;

import java.util.Map;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/19
 */
public class ScriptFactoryExample {
    private static final Map<String, ValueCreator> VALUE_CREATORS = ExtensionFactory.getExtensionLoader(ValueCreator.class).toMap();

    public static void main(String[] args) {
        OperateHashMap operate = OperateHashMap.create();
        operate.put(ValueScript.VALUE, "E://test.script");

        ValueScript valueScript = new ValueScript();
        valueScript.setType(Binder.Type.SCRIPT);
        valueScript.setTargetClass(ITestInterface.class);
        valueScript.setOperate(operate);

        ValueCreator valueCreator = VALUE_CREATORS.get("script");
        ITestInterface testInterface = (ITestInterface) valueCreator.create(valueScript);
        System.out.println(testInterface.getName());
    }
}
