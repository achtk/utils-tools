package com.chua.utils.netx.db4o.context;

import com.chua.utils.netx.db4o.factory.Db4oFactory;
import com.chua.utils.netx.factory.INetxFactory;
import com.chua.utils.tools.properties.NetxProperties;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import lombok.Getter;

import java.util.List;
import java.util.function.Predicate;

/**
 * db4o-context
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/29
 */
public class Db4oContext implements AutoCloseable {

    @Getter
    private final ObjectContainer objectContainer;
    private final NetxProperties netxProperties;
    private final INetxFactory<ObjectContainer> netxFactory;

    public Db4oContext(NetxProperties netxProperties) {
        this.netxProperties = netxProperties;
        this.netxFactory = new Db4oFactory(netxProperties);
        this.objectContainer = this.netxFactory.client();
        this.netxFactory.start();

    }
    /**
     * 保存数据
     *
     * @param obj 数据
     */
    public void set(Object obj) {
        try {
            this.objectContainer.store(obj);
        } catch (Exception e) {
            e.printStackTrace();
            objectContainer.rollback();
        }
    }

    /**
     * 删除数据
     *
     * @param obj 数据
     */
    public void delete(Object obj) {
        try {
            ObjectSet objectSet = objectContainer.queryByExample(obj);
            Object next = objectSet.next();
            objectContainer.delete(next);
        } catch (Exception e) {
            e.printStackTrace();
            objectContainer.rollback();
        }
    }

    /**
     * 查询数据
     *
     * @param obj 数据
     * @return List
     */
    public List<Object> query(Object obj) throws Exception {
        return objectContainer.queryByExample(obj);
    }

    /**
     * 查询数据
     *
     * @param predicate 数据
     * @return List
     */
    public <T> List<T> query(Predicate<T> predicate) throws Exception {
        return objectContainer.query(new com.db4o.query.Predicate<T>() {
            @Override
            public boolean match(T t) {
                return predicate.test(t);
            }
        });
    }

    @Override
    public void close() throws Exception {
        this.netxFactory.close();
    }
}
