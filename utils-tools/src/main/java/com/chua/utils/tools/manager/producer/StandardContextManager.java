package com.chua.utils.tools.manager.producer;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.collects.collections.CollectionHelper;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.manager.*;
import com.chua.utils.tools.manager.parser.ClassDescriptionParser;
import com.chua.utils.tools.manager.parser.StandardClassDescriptionParser;
import com.chua.utils.tools.spi.processor.ExtensionProcessor;
import com.google.common.collect.HashMultimap;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 对象管理器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/27
 */
@Data
public class StandardContextManager implements ContextManager {

    private EventBusContextManager eventBusContextManager = new StandardEventBusContextManager();
    private ObjectContextManager contextManager = new StandardScannerObjectContextManager();
    private StrategyContextManager strategyContextManager = new StandardStrategyContextManager();
    private ProfileAdaptorManager profileAdaptorManager = new StandardProfileAdaptorManager(null);

    private static final CopyOnWriteArraySet<?> COPY_ON_WRITE_ARRAY_SET = new CopyOnWriteArraySet<>();
    private static final HashMultimap<Class<?>, Object> MULTIMAP = HashMultimap.create();

    @Override
    public EventBusContextManager createEventBusContextManager() {
        return eventBusContextManager;
    }

    @Override
    public ObjectContextManager createObjectContextManager(boolean scanner) {
        return contextManager;
    }

    @Override
    public StrategyContextManager createStrategyContextManager() {
        return strategyContextManager;
    }

    @Override
    public ProfileAdaptorManager createProfileAdaptorManager(ExtensionProcessor extensionProcessor) {
        return null == extensionProcessor ? profileAdaptorManager : new StandardProfileAdaptorManager(extensionProcessor);
    }

    @Override
    public <Manager> List<Manager> createContextManager(Class<Manager> managerClass) {
        Set<Object> objects = MULTIMAP.get(managerClass);
        List<Manager> result = new ArrayList<>();
        CollectionHelper.doWithMatcher(objects, item -> {
            if (item.getClass().isAssignableFrom(managerClass)) {
                result.add((Manager) item);
            }
        });
        return result;
    }

    @Override
    public <Manager> Manager add(Manager manager) {
        if (null == manager) {
            return null;
        }
        ClassHelper.doWithInterface(manager.getClass(), item -> MULTIMAP.put(item, manager));
        return manager;
    }

    @Override
    public <T> ClassDescriptionParser<T> createClassDescriptionParser(Class<T> tClass) {
        return new StandardClassDescriptionParser<>(tClass);
    }
}
