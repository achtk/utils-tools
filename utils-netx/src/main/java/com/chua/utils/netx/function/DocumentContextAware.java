package com.chua.utils.netx.function;

import com.chua.utils.netx.entity.DocumentData;
import com.chua.utils.netx.entity.DocumentMap;
import com.chua.utils.netx.entity.Search;

import java.util.List;

/**
 * 文件接口
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
public interface DocumentContextAware<DTO> extends AutoCloseable {
    /**
     * 添加文档
     *
     * @param documentMap 文档
     * @throws Exception Exception
     */
    void addDocument(DocumentMap documentMap) throws Exception;

    /**
     * 添加文档
     *
     * @param documentMaps 文档
     * @throws Exception Exception
     */
    void addDocuments(List<DocumentMap> documentMaps) throws Exception;

    /**
     * 删除文档
     *
     * @param documentMap 文档
     * @return 成功个数
     */
    Integer deleteDocument(DocumentMap documentMap);

    /**
     * 查询
     * <p>单字段查询：name:demo</p>
     * <p>OR查询：name:demo or id:1</p>
     * <p>AND查询：name:demo and id:1</p>
     * <p>多字符匹配查询：id:1*</p>
     * <p>单字符匹配查询：id:1?</p>
     * <p>近似查询：id:1~10</p>
     * <p>范围查询：id:[1 TO 3]</p>
     *
     * @param search 查询对象
     * @return DocumentData<DTO>
     * @throws Exception Exception
     */
    DocumentData<DTO> search(Search search) throws Exception;

    /**
     * 查询
     * <p>单字段查询：name:demo</p>
     * <p>OR查询：name:demo or id:1</p>
     * <p>AND查询：name:demo and id:1</p>
     * <p>多字符匹配查询：id:1*</p>
     * <p>单字符匹配查询：id:1?</p>
     * <p>近似查询：id:1~10</p>
     * <p>范围查询：id:[1 TO 3]</p>
     *
     * @param search   查询对象
     * @param dtoClass 类型
     * @return DocumentData<DTO>
     * @throws Exception Exception
     */
    <T> DocumentData<T> search(Search search, Class<T> dtoClass) throws Exception;

    /**
     * 删除所有文档
     *
     * @throws Exception Exception
     */
    void deleteAll() throws Exception;
}
