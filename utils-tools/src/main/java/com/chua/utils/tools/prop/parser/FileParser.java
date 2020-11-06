package com.chua.utils.tools.prop.parser;

import com.chua.utils.tools.collects.collections.CollectionHelper;
import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.function.AbstractConverter;
import com.chua.utils.tools.prop.decorator.FileDecorator;
import com.chua.utils.tools.prop.finder.FileFinder;
import com.chua.utils.tools.prop.mapper.FileDataMapper;
import com.chua.utils.tools.prop.mapper.FileMapper;
import com.chua.utils.tools.prop.resolver.IFileResolver;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * 文件解析
 * @author CH
 */
@NoArgsConstructor
@AllArgsConstructor
public class FileParser {
    /**
     * 文件查找器
     */
    private FileFinder fileFinder;

    /**
     * 文件修饰
     */
    private FileDecorator fileDecorator = new FileDecorator();
    /**
     * 文件解析器
     */
    private Set<IFileResolver> fileResolvers = new HashSet<>();

    /**
     *
     * @param fileResolvers
     */
    public FileParser(Set<IFileResolver> fileResolvers) {
        CollectionHelper.add(this.fileResolvers, fileResolvers);
    }

    public FileParser(FileFinder fileFinder, FileDecorator fileDecorator) {
        this.fileFinder = fileFinder;
        this.fileDecorator = fileDecorator;
    }

    /**
     *
     * @return
     */
    public static FileParser.Builder builder() {
        return new FileParser.Builder();
    }

    /**
     * 解析文件
     * @param path 文件
     * @param abstractConverter 转化器
     * @return
     */
    public FileDataMapper stream(final String path, final AbstractConverter abstractConverter) {
        if(null == path) {
            return null;
        }
        InputStream inputStream = this.stream(path);
        if(null == inputStream) {
            return null;
        }
        return parser(FileHelper.getName(path), inputStream, abstractConverter);
    }

    /**
     * 文件解析
     * @param path 文件路径
     * @return
     */
    public InputStream stream(String path) {
        if(null == path) {
            return null;
        }
        try {
            URL url = new URL(path);
            return url.openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析文件
     * @param name 文件名
     * @param stream 流
     * @param abstractConverter 转化器
     * @return
     */
    public FileDataMapper parser(final String name, final InputStream stream, final AbstractConverter abstractConverter) {
        if(null == stream || null == name) {
            return null;
        }
        //获取文件类型
        String extension = FileHelper.getExtension(name);
        IFileResolver fileResolver = ExtensionFactory.getExtensionLoader(IFileResolver.class).getExtension(extension);
        fileResolver = isValid(extension, fileResolver);
        if(null == fileResolver) {
            throw new IllegalArgumentException("不支持该文件" + extension);
        }
        fileResolver.stream(stream);
        FileMapper fileMapper = fileResolver.analysis(abstractConverter);
        fileDecorator.dissolves(fileMapper);

        return fileDecorator.toMapper();
    }

    /**
     * 是否适配文件
     * @param extension 文件后缀
     * @param fileResolver 文件解析器
     * @return
     */
    private IFileResolver isValid(String extension, IFileResolver fileResolver) {
        if(null != fileResolver && fileResolver.isValid(extension)) {
            return fileResolver;
        }

        for (IFileResolver resolver : fileResolvers) {
            if(null == resolver || !resolver.isValid(extension)) {
                continue;
            }
            return resolver;
        }
        return null;
    }


    /**
     * 构造
     */
    @Setter
    @Accessors(chain = true)
    public static class Builder {

        /**
         * 文件查找器
         */
        private FileFinder fileFinder;

        /**
         * 文件修饰
         */
        private FileDecorator fileDecorator;

        /**
         *
         * @return
         */
        public FileParser build() {
            return new FileParser(fileFinder, fileDecorator);
        }
    }
}
