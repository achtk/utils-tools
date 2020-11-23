package com.chua.utils.netx.lucene.resolver;

import com.chua.utils.netx.lucene.entity.DataDocument;
import com.chua.utils.netx.lucene.entity.HitData;
import com.chua.utils.netx.lucene.operator.DocumentOperatorTemplate;
import com.chua.utils.netx.lucene.operator.IndexOperatorTemplate;
import com.chua.utils.netx.lucene.operator.SearchOperatorTemplate;
import com.chua.utils.netx.resolver.NetResolver;
import com.chua.utils.netx.resolver.collector.NetCollector;
import com.chua.utils.netx.resolver.document.NetDocument;
import com.chua.utils.netx.resolver.entity.NetCollectorConf;
import com.chua.utils.netx.resolver.entity.Service;
import com.chua.utils.tools.bean.copy.BeanCopy;
import com.chua.utils.tools.classes.ClassHelper;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.chua.utils.tools.constant.NumberConstant.DEFAULT_SIZE;

/**
 * lucene
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
public class LuceneResolver extends NetResolver<LuceneTemplateResolver> implements NetCollector, NetDocument<String> {

    private LuceneTemplateResolver luceneTemplateResolver;

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        this.luceneTemplateResolver = new LuceneTemplateResolver();
    }

    @Override
    public Service<LuceneTemplateResolver> get() {
        return new Service(luceneTemplateResolver);
    }

    @Override
    public boolean createCollection(NetCollectorConf netCollectorConf) {
        IndexOperatorTemplate indexOperatorTemplate = luceneTemplateResolver.getIndexOperatorTemplate();
        try {
            indexOperatorTemplate.create(netCollectorConf.getCollectionName(), netCollectorConf.getFragmentation());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteCollection(String collectionName) {
        IndexOperatorTemplate indexOperatorTemplate = luceneTemplateResolver.getIndexOperatorTemplate();
        try {
            indexOperatorTemplate.delete(collectionName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Set<String> listCollection() {
        IndexOperatorTemplate indexOperatorTemplate = luceneTemplateResolver.getIndexOperatorTemplate();
        return indexOperatorTemplate.getCollections();
    }

    @Override
    public boolean existCollection(String collectionName) {
        IndexOperatorTemplate indexOperatorTemplate = luceneTemplateResolver.getIndexOperatorTemplate();
        return indexOperatorTemplate.exist(collectionName);
    }

    @Override
    public Object execute(Object action, Class aClass) {
        throw new IllegalStateException("Unavailable");
    }


    @Override
    public Object addDocument(Object objectToSave, String collectionName) {
        DocumentOperatorTemplate operatorTemplate = null;
        try {
            operatorTemplate = luceneTemplateResolver.getDocumentOperatorTemplate(collectionName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (objectToSave instanceof DataDocument) {
            try {
                operatorTemplate.addDocument((DataDocument) objectToSave);
                return objectToSave;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (objectToSave instanceof Map) {
            DataDocument dataDocument = new DataDocument();
            dataDocument.setData((Map<String, Object>) objectToSave);
            try {
                operatorTemplate.addDocument(dataDocument);
                return objectToSave;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            DataDocument dataDocument = new DataDocument();

            Map<String, Object> data = new HashMap<>(DEFAULT_SIZE);
            ClassHelper.doWithLocalFields(ClassHelper.getClass(objectToSave), field -> {
                try {
                    field.setAccessible(true);
                    Object o = field.get(objectToSave);
                    data.put(field.getName(), o);
                } catch (Exception ignore) {
                }
            });
            dataDocument.setData(data);
            try {
                operatorTemplate.addDocument(dataDocument);
                return objectToSave;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Collection addDocument(Collection objectToSave, String collectionName) {
        if (null == objectToSave) {
            return null;
        }
        List<Object> result = new ArrayList<>(objectToSave.size());
        objectToSave.stream().forEach(item -> {
            addDocument(item, collectionName);
        });
        return result;
    }

    @Override
    @SneakyThrows
    public long removeDocument(Object objectToSave, String collectionName) {
        DocumentOperatorTemplate documentOperatorTemplate = luceneTemplateResolver.getDocumentOperatorTemplate(collectionName);
        if (objectToSave instanceof String) {
            documentOperatorTemplate.deleteDocument((String) objectToSave);
            return 1;
        } else if (objectToSave instanceof DataDocument) {
            documentOperatorTemplate.deleteDocument(((DataDocument) objectToSave).getDataId());
            return 1;
        }
        return 0;
    }

    @Override
    public <T> List<T> query(String s, Class<T> entityClass, String collectionName) {
        SearchOperatorTemplate searchOperatorTemplate = null;
        try {
            searchOperatorTemplate = luceneTemplateResolver.getSearchOperatorTemplate(collectionName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        HitData data = searchOperatorTemplate.quickSearch(s);
        List<Map<String, Object>> dataData = data.getData();
        if (null == entityClass || Map.class.isAssignableFrom(entityClass)) {
            return (List<T>) dataData;
        }
        return dataData.stream().map(item -> {
            T object = ClassHelper.forObject(entityClass);
            if (null == object) {
                return null;
            }
            BeanCopy<T> beanCopy = BeanCopy.of(object);
            beanCopy.with(item);

            return beanCopy.create();
        }).filter(item -> null != item).collect(Collectors.toList());
    }

    @Override
    public long removeDocumentByAction(String s, String collectionName) {
        return 0;
    }
}
