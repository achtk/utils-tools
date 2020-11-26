package com.chua.utils.tools.resource.entity;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.adaptor.JavassistAdaptor;
import com.chua.utils.tools.classes.adaptor.MetadataAdapter;
import com.chua.utils.tools.classes.entity.ClassDescription;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.StringHelper;
import com.google.common.base.Strings;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.omg.CORBA.INITIALIZE;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.jar.JarEntry;

import static com.chua.utils.tools.constant.StringConstant.JAR_URL_SEPARATOR;
import static com.chua.utils.tools.constant.SuffixConstant.SUFFIX_CLASS;
import static com.chua.utils.tools.constant.SymbolConstant.*;

/**
 * 资源对象
 *
 * @author CH
 */
@Getter
@NoArgsConstructor
@Setter
@Accessors(chain = true)
public class Resource implements Serializable {
    /**
     * 资源URL
     */
    private URL url;

    public Resource(URL url) {
        setUrl(url);
    }

    /**
     * 类信息
     */
    private ClassDescription classDescription = ClassDescription.INSTANCE;

    /**
     * @param url
     * @return
     */
    public static Resource create(URL url) {
        return new Resource(url);
    }

    public Resource setUrl(URL url) {
        this.url = url;
        return this;
    }

    public ClassDescription getClassDescription() {
        if(url.toExternalForm().indexOf(SYMBOL_DOLLAR) != -1) {
            return null;
        }
        if(null == classDescription || Strings.isNullOrEmpty(classDescription.getName())) {
            this.classDescription = createDescription();
        }
        return classDescription;
    }

    /**
     * @return
     */
    private ClassDescription createDescription() {
        try (InputStream inputStream = url.openStream()) {
            MetadataAdapter metadataAdapter = new JavassistAdaptor(inputStream);
            return metadataAdapter.description();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return null == url ? "" : url.toExternalForm();
    }
}
