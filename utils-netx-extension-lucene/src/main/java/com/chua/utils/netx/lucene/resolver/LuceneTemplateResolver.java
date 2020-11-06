package com.chua.utils.netx.lucene.resolver;

import com.chua.utils.netx.centor.resolver.TemplateResolver;
import com.chua.utils.netx.lucene.factory.DirectoryFactory;
import com.chua.utils.netx.lucene.operator.*;
import com.chua.utils.tools.common.codec.encrypt.AbstractStandardEncrypt;
import com.chua.utils.tools.common.codec.encrypt.Encrypt;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import lombok.NonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.chua.utils.tools.common.codec.encrypt.AbstractStandardEncrypt.ENCRYPT_KEY;

/**
 * lucene模板
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/3
 */
public class LuceneTemplateResolver implements TemplateResolver {
    /**
     * desede
     */
    private static final String DESEDE = "des";
    /**
     * 存储位置
     */
    @NonNull
    private Path storePath = Paths.get(System.getProperty("user.home"), "lucene-data");
    /**
     * 加解密
     */
    @NonNull
    private Encrypt encrypt = ExtensionFactory.getExtensionLoader(Encrypt.class).getExtension(DESEDE);
    /**
     * 索引模板
     */
    private IndexOperatorTemplate indexOperatorTemplate;
    /**
     * 秘钥
     */
    private String secret = "lucene";
    /**
     * 目录类型
     */
    private DirectoryFactory.DirectoryType directoryType = DirectoryFactory.DirectoryType.NIO;
    /**
     * Directory工厂
     */
    private DirectoryFactory directoryFactory = new DirectoryFactory(this.directoryType);

    public LuceneTemplateResolver() {
        this.encrypt.append(ENCRYPT_KEY, this.secret);
        this.indexOperatorTemplate = new DefaultIndexOperatorTemplate(this.storePath, this.encrypt, directoryFactory);
    }

    /**
     * 构造
     *
     * @param storePath 存储位置
     * @param encrypt   加密算法
     */
    public LuceneTemplateResolver(@NonNull Path storePath, @NonNull Encrypt encrypt) {
        this.storePath = storePath;
        this.encrypt = encrypt;
        this.encrypt.append(ENCRYPT_KEY, this.secret);
        this.indexOperatorTemplate = new DefaultIndexOperatorTemplate(this.storePath, this.encrypt, directoryFactory);
    }

    /**
     * 构造
     *
     * @param storePath 存储位置
     * @param encrypt   加密算法
     * @param secret    秘钥
     */
    public LuceneTemplateResolver(@NonNull Path storePath, @NonNull Encrypt encrypt, String secret) {
        this.storePath = storePath;
        this.encrypt = encrypt;
        this.secret = secret;
        this.encrypt.append(ENCRYPT_KEY, this.secret);
        this.indexOperatorTemplate = new DefaultIndexOperatorTemplate(this.storePath, this.encrypt, directoryFactory);
    }

    /**
     * 构造
     *
     * @param directoryType 目录类型
     */
    public LuceneTemplateResolver(DirectoryFactory.DirectoryType directoryType) {
        this.encrypt.append(ENCRYPT_KEY, this.secret);
        this.directoryType = directoryType;
        this.directoryFactory = new DirectoryFactory(this.directoryType);
        this.indexOperatorTemplate = new DefaultIndexOperatorTemplate(this.storePath, this.encrypt, directoryFactory);
    }
    /**
     * 构造
     *
     * @param storePath     存储位置
     * @param directoryType 目录类型
     */
    public LuceneTemplateResolver(@NonNull Path storePath, DirectoryFactory.DirectoryType directoryType) {
        this.storePath = storePath;
        this.encrypt.append(ENCRYPT_KEY, this.secret);
        this.directoryType = directoryType;
        this.directoryFactory = new DirectoryFactory(this.directoryType);
        this.indexOperatorTemplate = new DefaultIndexOperatorTemplate(this.storePath, this.encrypt, directoryFactory);
    }
    /**
     * 构造
     *
     * @param storePath     存储位置
     * @param encrypt       加密算法
     * @param secret        秘钥
     * @param directoryType 目录类型
     */
    public LuceneTemplateResolver(@NonNull Path storePath, @NonNull Encrypt encrypt, String secret, DirectoryFactory.DirectoryType directoryType) {
        this.storePath = storePath;
        this.encrypt = encrypt;
        this.secret = secret;
        this.encrypt.append(ENCRYPT_KEY, this.secret);
        this.directoryType = directoryType;
        this.directoryFactory = new DirectoryFactory(this.directoryType);
        this.indexOperatorTemplate = new DefaultIndexOperatorTemplate(this.storePath, this.encrypt, directoryFactory);
    }

    /**
     * 构造
     *
     * @param storePath        存储位置
     * @param encrypt          加密算法
     * @param secret           秘钥
     * @param directoryFactory 目录类型
     */
    public LuceneTemplateResolver(@NonNull Path storePath, @NonNull Encrypt encrypt, String secret, DirectoryFactory directoryFactory) {
        this.storePath = storePath;
        this.encrypt = encrypt;
        this.secret = secret;
        this.encrypt.append(ENCRYPT_KEY, this.secret);
        this.directoryType = directoryFactory.getDirectoryType();
        this.directoryFactory = directoryFactory;
        this.indexOperatorTemplate = new DefaultIndexOperatorTemplate(storePath, encrypt, directoryFactory);
    }

    /**
     * 获取索引模板
     *
     * @return IndexOperatorTemplate
     */
    public IndexOperatorTemplate getIndexOperatorTemplate() {
        return indexOperatorTemplate;
    }

    /**
     * 获取文档模板
     *
     * @return IndexOperatorTemplate
     */
    public DocumentOperatorTemplate getDocumentOperatorTemplate(String index) throws IOException {
        return new DefaultDocumentOperatorTemplate(indexOperatorTemplate, index);
    }

    /**
     * 获取查询模板
     *
     * @return IndexOperatorTemplate
     */
    public SearchOperatorTemplate getSearchOperatorTemplate(String index) throws IOException {
        return new DefaultSearchOperatorTemplate(indexOperatorTemplate, index);
    }
}
