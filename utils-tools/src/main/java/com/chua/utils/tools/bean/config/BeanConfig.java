package com.chua.utils.tools.bean.config;

import com.chua.utils.tools.bean.interpreter.MapperInterpreter;
import com.chua.utils.tools.bean.interpreter.NameInterpreter;
import com.chua.utils.tools.bean.interpreter.NamedInterpreter;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * bean基础配置
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
@Data
public class BeanConfig {

    private Set<NameInterpreter> interpreters = new HashSet<NameInterpreter>() {
        {
            add(new NamedInterpreter());
        }
    };
    private Set<MapperInterpreter> mapperInterpreters = new HashSet<>();

    /**
     * 添加命名解释器
     *
     * @param nameInterpreter 命名解释器
     * @return this
     */
    public BeanConfig interpreter(NameInterpreter nameInterpreter) {
        interpreters.add(nameInterpreter);
        return this;
    }

    /**
     * 添加映射解释器
     *
     * @param mapperInterpreter 映射解释器
     * @return this
     */
    public BeanConfig mapper(MapperInterpreter mapperInterpreter) {
        mapperInterpreters.add(mapperInterpreter);
        return this;
    }
}
