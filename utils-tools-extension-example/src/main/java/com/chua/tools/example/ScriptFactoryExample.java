package com.chua.tools.example;

import com.chua.tools.example.interfaces.TestInterface;
import com.chua.utils.tools.annotations.BinderScript;
import com.chua.utils.tools.bean.creator.ValueCreator;
import com.chua.utils.tools.bean.script.ValueScript;
import com.chua.utils.tools.collects.OperateHashMap;
import com.chua.utils.tools.spi.factory.ExtensionFactory;

import java.util.Map;

/**
 * 脚本工具
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/19
 */
public class ScriptFactoryExample extends BaseExample {
    /**
     * 获取所有脚本解释器
     */
    private static final Map<String, ValueCreator> VALUE_CREATORS = ExtensionFactory.getExtensionLoader(ValueCreator.class).toMap();

    public static void main(String[] args) {
        //用脚本创建接口实现
        OperateHashMap operate = OperateHashMap.create();
        operate.put(ValueScript.VALUE, "E://test.script");

        ValueScript valueScript = new ValueScript();
        valueScript.setType(BinderScript.Type.SCRIPT);
        valueScript.setTargetClass(TestInterface.class);
        valueScript.setOperate(operate);

        ValueCreator valueCreator = VALUE_CREATORS.get("script");
        TestInterface testInterface = (TestInterface) valueCreator.create(valueScript);
        log.info("测试接口: getName() -> {}", testInterface.getName());
    }
}
