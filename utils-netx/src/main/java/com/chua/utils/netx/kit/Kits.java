package com.chua.utils.netx.kit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * the kit collect
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/17
 */
public class Kits {

    private Map<String, Kit> operate = new ConcurrentHashMap<>();

    /**
     * 添加工具
     *
     * @param name 名称
     * @param kit  工具
     * @return 工具
     */
    public Kits addKit(final String name, final Kit kit) {
        operate.put(name, kit);
        return this;
    }

    /**
     * 工具集
     *
     * @return 工具集
     */
    public List<Kit> kits() {
        return new ArrayList<>(operate.values());
    }
}
