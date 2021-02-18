package com.chua.utils.tools.resource.parser.compress.file;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.JavassistHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.IoHelper;
import com.chua.utils.tools.compiler.Compiler;
import com.chua.utils.tools.compiler.JavassistCompiler;
import com.chua.utils.tools.resource.parser.ParserFile;
import com.chua.utils.tools.resource.parser.ParserJava;
import com.chua.utils.tools.resource.parser.compress.dir.ClassFileParserDir;
import com.taobao.arthas.compiler.DynamicCompiler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统文件
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/14
 */
public class ClassFileParserFile implements ParserFile, ParserJava {

    private final ClassFileParserDir root;
    private final java.io.File file;
    private String className;

    public ClassFileParserFile(final ClassFileParserDir root, java.io.File file) {
        this.root = root;
        this.file = file;
    }

    @Override
    public String getName() {
        try {
            FileInputStream inputStream = new FileInputStream(file);
            Class<?> aClass = JavassistHelper.toClass(inputStream);
            this.className = null == aClass ? null : aClass.getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return className;
    }

    @Override
    public String getRelativePath() {
        String filepath = file.getPath().replace("\\", "/");
        if (filepath.startsWith(root.getPath())) {
            int length = root.getPath().length() + 1;
            if (length < filepath.length()) {
                return filepath.substring(length);
            }
            return filepath;
        }

        return null;
    }

    @Override
    public Path path() {
        return Paths.get(file.getPath());
    }

    @Override
    public InputStream openInputStream() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return file.toString();
    }

    @Override
    public Class<?> toClass(ClassLoader classLoader) {
        DynamicCompiler compiler = compiler(classLoader);
        if (null == compiler) {
            return null;
        }

        Map<String, Class<?>> byteCodes = compiler.build();
        return FinderHelper.firstElement(byteCodes.values());
    }

    @Override
    public Map<String, ByteBuffer> toBuffer(ClassLoader classLoader) {
        DynamicCompiler compiler = compiler(classLoader);
        if (null == compiler) {
            return null;
        }

        Map<String, byte[]> byteCodes = compiler.buildByteCodes();
        Map<String, ByteBuffer> bufferMap = new HashMap<>(byteCodes.size());

        for (Map.Entry<String, byte[]> entry : byteCodes.entrySet()) {
            ByteBuffer buffer = ByteBuffer.wrap(entry.getValue());
            bufferMap.put(entry.getKey(), buffer);
        }
        return bufferMap;
    }

    /**
     * 编译
     *
     * @param classLoader
     * @return
     */
    private DynamicCompiler compiler(ClassLoader classLoader) {
        String sourceCode = null;

        try {
            ByteBuffer byteBuffer = IoHelper.copyInputStream(openInputStream());
            sourceCode = IoHelper.toString(byteBuffer.array());
        } catch (IOException e) {
            return null;
        }
        Compiler compiler = new JavassistCompiler();
        String className = compiler.getClassName(sourceCode);
        String pkg = compiler.getPkg(sourceCode);

        DynamicCompiler dynamicCompiler = new DynamicCompiler(ClassHelper.getDefaultClassLoader());
        dynamicCompiler.addSource(pkg + "." + className, sourceCode);
        return dynamicCompiler;
    }
}
