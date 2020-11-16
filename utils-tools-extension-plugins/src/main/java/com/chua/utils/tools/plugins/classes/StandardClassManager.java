package com.chua.utils.tools.plugins.classes;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.plugins.classloader.PluginClassLoader;
import com.chua.utils.tools.plugins.entity.ClassInfo;
import com.chua.utils.tools.resource.parser.ParserDir;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.ParserJava;
import com.chua.utils.tools.resource.parser.vfs.ParserVfs;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.AllArgsConstructor;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.chua.utils.tools.constant.StringConstant.CLASS_FILE_EXTENSION;
import static com.chua.utils.tools.constant.SuffixConstant.SUFFIX_JAVA;

/**
 * 类管理器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class StandardClassManager implements ClassManager {

    private final Multimap<String, ClassInfo> pathClass = HashMultimap.create();

    @Override
    public PluginClassLoader createClassLoader() {
        return new PluginClassLoader(new URL[0]);
    }

    @Override
    public void register(Path path) {
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
    public Collection<Object> findSubType(String className) {
        Collection<ClassInfo> classInfos = pathClass.values();
        ExecutorService executorService = ThreadHelper.newProcessorThreadExecutor();
        List<Future<Collection<Object>>> futureList = new ArrayList<>();

        for (ClassInfo classInfo : classInfos) {
            futureList.add(executorService.submit(new ClassFindTask(classInfo)));
        }

        List<Object> objects = new ArrayList<>();
        for (Future<Collection<Object>> objectFuture : futureList) {
            Collection<Object> value = null;
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
    private class ClassFindTask implements Callable<Collection<Object>> {

        private ClassInfo classInfo;

        @Override
        public Collection<Object> call() throws Exception {
            String name = classInfo.getName();
            ClassLoader classLoader = classInfo.getClassLoader();
            if(classLoader instanceof PluginClassLoader) {
                ((PluginClassLoader)classLoader).setClassInfo(classInfo);
            }

            Object object = ClassHelper.forObject(name, classLoader);
            if (null == object) {
                return Collections.emptyList();
            }
            return Collections.singletonList(object);
        }
    }
}
