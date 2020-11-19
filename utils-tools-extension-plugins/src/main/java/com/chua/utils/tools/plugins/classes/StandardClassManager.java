package com.chua.utils.tools.plugins.classes;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.IoHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.constant.StringConstant;
import com.chua.utils.tools.plugins.classloader.PluginClassLoader;
import com.chua.utils.tools.plugins.entity.ClassInfo;
import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.ParserJava;
import com.chua.utils.tools.resource.parser.vfs.ParserVfs;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.chua.utils.tools.constant.StringConstant.*;
import static com.chua.utils.tools.constant.SuffixConstant.*;
import static com.chua.utils.tools.constant.SymbolConstant.*;

/**
 * 类管理器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
@Slf4j
public class StandardClassManager implements ClassManager {

    private final Multimap<String, ClassInfo> pathClass = HashMultimap.create();

    private static final String CONFIG_NAME = "META-INF/plugin.idx";

    @Override
    public PluginClassLoader createClassLoader() {
        return new PluginClassLoader(new URL[0]);
    }

    @Override
    public void register(Path path) {
        //判断配置项是否存在插件配置文件, 存在只处理改文件
        if (existConfig(path)) {
            parserCfg(path);
            return;
        }
        parserPath(path);
    }

    /**
     * 解析整个文件
     *
     * @param path 文件
     */
    private void parserPath(Path path) {
        try {
            ParserDir parserDir = ParserVfs.fromUrl(path.toUri().toURL(), parserFile -> {
                String name = parserFile.getRelativePath();
                if (!name.endsWith(CLASS_FILE_EXTENSION)) {
                    if (name.endsWith(SUFFIX_JAVA)) {
                        parserJava(parserFile);
                    }
                    return;
                }
                ClassInfo classInfo = new ClassInfo();
                classInfo.setClassLoader(createClassLoader());
                classInfo.setByteBuffer(parserFile.toByteBuffer());
                classInfo.setName(parserFile.getName());
                classInfo.setPath(parserFile.path());
                pathClass.put(FileHelper.deleteSuffix(path.getFileName().toString(), CLASS_FILE_EXTENSION), classInfo);
            });
            Iterable<ParserFile> parserDirFiles = parserDir.getFiles();
            for (ParserFile parserDirFile : parserDirFiles) {
                parserDirFile.getRelativePath();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析cfg信息
     *
     * @param path 目录
     */
    private void parserCfg(Path path) {
        try (ZipFile zipFile = new ZipFile(path.toFile())) {
            ZipEntry entry = zipFile.getEntry(CONFIG_NAME);
            if (null == entry) {
                log.warn("配置[{}]文件无法解析", CONFIG_NAME);
                return;
            }
            Properties properties = new Properties();
            try (InputStreamReader inputStream = new InputStreamReader(zipFile.getInputStream(entry))) {
                properties.load(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Set<Object> value = new HashSet<>();
            value.addAll(properties.keySet());
            value.addAll(properties.values());

            for (Object key : value) {
                String name = key.toString().replace(SYMBOL_DOT, SYMBOL_LEFT_SLASH);
                ClassInfo classInfo = new ClassInfo();
                classInfo.setClassLoader(createClassLoader());
                classInfo.setPath(Paths.get(name + SUFFIX_CLASS));
                classInfo.setName(key.toString());
                ZipEntry classEntry = zipFile.getEntry(classInfo.getPath().toString().replace(SYMBOL_RIGHT_SLASH, SYMBOL_LEFT_SLASH));
                if (null == classEntry) {
                    continue;
                }
                classInfo.setByteBuffer(ByteBuffer.wrap(IoHelper.toByteArray(zipFile.getInputStream(classEntry))));

                pathClass.put(path.getFileName().toString(), classInfo);
            }

        } catch (IOException e) {
        }

    }

    /**
     * 是否存在配置文件
     *
     * @param path 路径
     * @return 存在配置文件返回true
     */
    private boolean existConfig(Path path) {
        String fileName = path.getFileName().toString();
        if (fileName.endsWith(SUFFIX_JAR_SUFFIX)) {
            try (ZipFile zipFile = new ZipFile(path.toFile())) {
                ZipEntry entry = zipFile.getEntry(CONFIG_NAME);
                return null != entry;
            } catch (IOException e) {
            }
        }
        return false;
    }


    /**
     * 解析java
     *
     * @param parserFile 文件
     */
    private void parserJava(ParserFile parserFile) {
        Map<String, ByteBuffer> byteBuffer = null;
        if (parserFile instanceof ParserJava) {
            ParserJava parserJava = (ParserJava) parserFile;
            byteBuffer = parserJava.toBuffer();
        }

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassLoader(createClassLoader());
        classInfo.setName(FinderHelper.firstElement(byteBuffer.keySet()));
        classInfo.setPath(parserFile.path());
        classInfo.setByteBuffer(FinderHelper.firstElement(byteBuffer.values()));
        pathClass.put(parserFile.path().getFileName().toString(), classInfo);
    }

    @Override
    public void unregister(Path path) {
        String name = path.getFileName().toString();
        if (pathClass.containsKey(name)) {
            pathClass.removeAll(name);
        }
    }

    @Override
    public Collection<Class<?>> findSubType(String className) {
        Collection<ClassInfo> classInfos = pathClass.values();
        ExecutorService executorService = ThreadHelper.newProcessorThreadExecutor();
        List<Future<Collection<Class<?>>>> futureList = new ArrayList<>();

        for (ClassInfo classInfo : classInfos) {
            futureList.add(executorService.submit(new ClassFindTask(classInfo)));
        }

        List<Class<?>> objects = new ArrayList<>();
        for (Future<Collection<Class<?>>> objectFuture : futureList) {
            Collection<Class<?>> value = null;
            try {
                value = objectFuture.get();
            } catch (Exception e) {
                continue;
            }
            if (!value.isEmpty()) {
                objects.addAll(value);
            }
        }
        return objects;
    }

    @AllArgsConstructor
    private class ClassFindTask implements Callable<Collection<Class<?>>> {

        private ClassInfo classInfo;

        @Override
        public Collection<Class<?>> call() throws Exception {
            String name = classInfo.getName();
            ClassLoader classLoader = classInfo.getClassLoader();
            if (classLoader instanceof PluginClassLoader) {
                ((PluginClassLoader) classLoader).setClassInfo(classInfo);
            }

            Class<?> aClass = ClassHelper.forName(name, classLoader);
            if (null == aClass) {
                return Collections.emptyList();
            }
            return Collections.singletonList(aClass);
        }
    }
}
