package com.chua.utils.tools.collects;

import com.chua.utils.tools.function.BiAppendable;
import com.chua.utils.tools.function.MapOperable;
import com.chua.utils.tools.function.Operable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 扩展性Map
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public class HashOperateMap extends HashMap<String, Object> implements MapOperable<String>, BiAppendable<String, Object> {

    @Override
    public BiAppendable append(String v1, Object v2) throws IOException {
        this.put(v1, v2);
        return this;
    }

    @Override
    public Map<String, Object> getMap() {
        return this;
    }

}
