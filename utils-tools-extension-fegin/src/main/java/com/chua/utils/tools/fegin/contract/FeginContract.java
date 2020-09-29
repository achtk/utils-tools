package com.chua.utils.tools.fegin.contract;

import feign.Contract;
import feign.MethodMetadata;

import java.util.List;

/**
 * @author CH
 * @date 2020-09-29
 */
public class FeginContract implements Contract {
    @Override
    public List<MethodMetadata> parseAndValidatateMetadata(Class<?> targetType) {
        return null;
    }
}
