package com.chua.utils.tools.prop.placeholder.resolver;

import com.chua.utils.tools.collects.collections.CollectionHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.prop.mapper.FileDataMapper;
import com.chua.utils.tools.prop.mapper.FileMapper;
import com.chua.utils.tools.prop.parser.FileParser;
import com.chua.utils.tools.prop.placeholder.mapper.PlaceholderMapper;
import com.chua.utils.tools.prop.resolver.IFileResolver;
import com.google.common.collect.HashMultimap;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.*;

/**
 * 占位工厂
 *
 * @author CH
 */
public class PropertiesPlaceholderFactory {

    private static final List<AbstractPropertiesPlaceholderResolver> DEFAULT_RESOLVERS = new ArrayList<AbstractPropertiesPlaceholderResolver>() {
        {
            add(new DefaultPropertiesPlaceholderResolver());
            add(new SystemPropertiesPlaceholderResolver());
        }
    };
    private FileResolverFactory fileResolverFactory;
    private List<AbstractPropertiesPlaceholderResolver> resolvers;
    private List<HashMultimap> hashMultimapList;
    /**
     * 查找路径
     */
    private Set<String> paths;
    /**
     * 文件解析器
     */
    private Set<IFileResolver> fileResolvers;

    private PropertiesPlaceholderFactory() {
    }

    public PropertiesPlaceholderFactory(
            List<AbstractPropertiesPlaceholderResolver> resolvers,
            Set<String> paths,
            Set<IFileResolver> fileResolvers,
            List<HashMultimap> hashMultimapList) {
        this.resolvers = CollectionHelper.getOrDefault(resolvers, DEFAULT_RESOLVERS);
        this.hashMultimapList = hashMultimapList;
        this.paths = paths;
        this.fileResolvers = fileResolvers;
        this.fileResolverFactory = new FileResolverFactory(this.paths, this.fileResolvers);
        this.fileResolverFactory.parser(hashMultimapList);
    }


    /**
     * @return
     */
    public static PropertiesPlaceholderFactory.Builder newBuilder() {
        return new PropertiesPlaceholderFactory.Builder();
    }

    /**
     * 处理占位符
     *
     * @param value
     * @return
     */
    public Object placeholder(Object value) {
        if (!BooleanHelper.hasLength(resolvers) || !BooleanHelper.hasLength(hashMultimapList)) {
            return value;
        }
        if (!(value instanceof String) || null == value) {
            return value;
        }
        String newValue = value.toString();
        //第一次映射
        for (AbstractPropertiesPlaceholderResolver resolver : resolvers) {
            if (resolver.isMatcher(newValue)) {
                PlaceholderMapper placeholderMapper = resolver.analyze(newValue);
                return analysis(placeholderMapper);
            }
        }
        return value;
    }

    /**
     * 处理占位符
     *
     * @return
     */
    public List<HashMultimap<String, Object>> placeholders() {
        List<HashMultimap<String, Object>> result = new ArrayList<>(hashMultimapList.size());
        for (HashMultimap<String, Object> hashMultimap : hashMultimapList) {
            renderHashMultimap(hashMultimap, result);
        }
        return result;
    }

    /**
     * 渲染HashMultimap数据
     *
     * @param hashMultimap
     * @param result
     */
    private void renderHashMultimap(HashMultimap<String, Object> hashMultimap, List<HashMultimap<String, Object>> result) {
        HashMultimap<String, Object> item = HashMultimap.create();
        for (Map.Entry<String, Object> entry : hashMultimap.entries()) {
            Object value = entry.getValue();
            String key = entry.getKey();
            if (null == value) {
                item.put(key, null);
            }
            if (value instanceof Collection) {
                Collection collection = (Collection) value;
                if (collection.size() == 0) {
                    renderItem(item, key, FinderHelper.firstElement(collection));
                    continue;
                }
                item.put(key, collection);
                continue;
            }
            renderItem(item, key, value);
        }
        result.add(item);
    }

    /**
     * 渲染数据
     *
     * @param item  元素集合
     * @param key   索引
     * @param value 值
     */
    private void renderItem(HashMultimap<String, Object> item, String key, Object value) {
        if (value instanceof String) {
            item.put(key, placeholder(value.toString()));
        } else {
            item.put(key, value);
        }
    }

    /**
     * 解析结果数据
     *
     * @param placeholderMapper
     * @return
     */
    private synchronized Object analysis(PlaceholderMapper placeholderMapper) {
        if (!placeholderMapper.isCheckPlaceholder()) {
            return placeholderMapper.getValue();
        }
        String value = placeholderMapper.getValue();
        boolean isPlaceholdersAgain = false;
        for (AbstractPropertiesPlaceholderResolver resolver : resolvers) {
            if (resolver.isMatcher(value)) {
                isPlaceholdersAgain = true;
                //再次处理占位符
                PlaceholderMapper mapper = resolver.analyze(placeholderMapper.getValue());
                return analysis(mapper);
            }
        }

        if (!isPlaceholdersAgain) {
            return analysisDataMapper(placeholderMapper);
        }
        return placeholderMapper.getDefaultValue();
    }

    /**
     * 返回最终数据
     *
     * @param placeholderMapper 数据映射
     * @return
     */
    private Object analysisDataMapper(PlaceholderMapper placeholderMapper) {
        String value = placeholderMapper.getValue();
        List<Object> valueForValue = new ArrayList<>();
        for (HashMultimap hashMultimap : hashMultimapList) {
            if (hashMultimap.containsKey(value)) {
                valueForValue.addAll(hashMultimap.get(value));
            }
        }
        //当数据索引未找到返回默认值
        if (valueForValue.size() == 0) {
            return placeholderMapper.getDefaultValue();
        }
        //多条记录
        if (valueForValue.size() > 1) {
            return valueForValue;
        }
        //新的数据
        Object newValue = valueForValue.get(0);
        //判断是否是非字符, 非字符串不存在占位符
        if (!(newValue instanceof String) || newValue == null) {
            return newValue;
        }
        //字符串再次检验占位符
        return placeholder(newValue.toString());
    }

    /**
     * 建造
     */
    @Setter
    @Accessors(chain = true)
    public static class Builder {

        private List<AbstractPropertiesPlaceholderResolver> resolvers = new ArrayList<>();
        private final List<HashMultimap> hashMultimapList = new ArrayList<>();
        //查找路径
        private Set<String> paths = new HashSet<>();
        //文件解析器
        private Set<IFileResolver> fileResolvers = new HashSet<>();

        /**
         * 添加解释器
         *
         * @param resolver
         * @return
         */
        public Builder addResolver(AbstractPropertiesPlaceholderResolver resolver) {
            resolvers.add(resolver);
            return this;
        }

        /**
         * 添加文件路径
         *
         * @param path 文件路径
         * @return
         */
        public Builder addPath(final String path) {
            if (null != path) {
                paths.add(path);
            }
            return this;
        }

        /**
         * 添加文件解析器
         *
         * @param fileResolver 文件解析器
         * @return
         */
        public Builder addFileResolver(IFileResolver fileResolver) {
            if (null != fileResolver) {
                fileResolvers.add(fileResolver);
            }
            return this;
        }

        /**
         * 数据映射
         *
         * @param hashMultimapList
         * @return
         */
        public Builder dataMapper(List<HashMultimap> hashMultimapList) {
            if (BooleanHelper.hasLength(hashMultimapList)) {
                //合并数据
                this.hashMultimapList.addAll(hashMultimapList);
            }
            return this;
        }

        /**
         * 数据映射
         *
         * @param hashMultimap
         * @return
         */
        public Builder dataMapper(HashMultimap... hashMultimap) {
            if (BooleanHelper.hasLength(hashMultimap)) {
                for (HashMultimap multimap : hashMultimap) {
                    hashMultimapList.add(multimap);
                }
            }
            return this;
        }

        /**
         * 数据映射
         *
         * @param cfg
         * @return
         */
        public Builder dataMapper(Map<String, Object> cfg) {
            if (!BooleanHelper.hasLength(cfg)) {
                return this;
            }
            HashMultimap hashMultimap = HashMultimap.create();
            for (Map.Entry<String, Object> entry : cfg.entrySet()) {
                hashMultimap.put(entry.getKey(), entry.getValue());
            }
            return dataMapper(hashMultimap);
        }

        /**
         * 构建
         *
         * @return
         */
        public PropertiesPlaceholderFactory build() {
            return new PropertiesPlaceholderFactory(resolvers, paths, fileResolvers, hashMultimapList);
        }
    }

    /**
     * 文件解析
     */
    private class FileResolverFactory {
        //查找路径
        private Set<String> paths;
        //文件解析器
        private Set<IFileResolver> fileResolvers;
        //文件转化
        private FileParser fileParser;

        public FileResolverFactory(Set<String> paths, Set<IFileResolver> fileResolvers) {
            this.paths = paths;
            this.fileResolvers = fileResolvers;
            this.fileParser = new FileParser(fileResolvers);
        }

        /**
         * 文件分析
         *
         * @return HashMultimap
         */
        public void parser(List<HashMultimap> hashMultimapList) {
            if (!BooleanHelper.hasLength(this.paths) || !BooleanHelper.hasLength(this.fileResolvers)) {
                return;
            }

            if (null == hashMultimapList) {
                hashMultimapList = new ArrayList<>();
            }

            for (String path : paths) {
                FileDataMapper fileDataMapper = this.fileAnalysis(path);
                if (null == fileDataMapper) {
                    continue;
                }
                FileMapper fileMapper = FinderHelper.firstElement(fileDataMapper.getFileMapperList());
                if (null == fileMapper) {
                    continue;
                }
                hashMultimapList.add(fileMapper.getHashMultimap());
            }
        }

        /**
         * 解析文件
         *
         * @param path 文件路径
         * @return FileDataMapper
         */
        private FileDataMapper fileAnalysis(final String path) {
            return fileParser.stream(path, null);
        }
    }
}
