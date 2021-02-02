package com.chua.tools.example;

import com.chua.tools.example.entity.TDemoInfoImpl;
import com.chua.utils.netx.datasource.mem.MemSearch;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import com.chua.utils.tools.text.IdHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/19
 */
public class MemSearchExample {

    public static void main(String[] args) throws Exception {
        //  DatabaseManagerSwing.main(args);
        //MemSearch memSearch = new HsqlSearch();
        MemSearch memSearch = ExtensionFactory.getExtensionLoader(MemSearch.class)
                .getExtension("hsqldb");
        System.out.println("获取memSearch： " + memSearch.getClass().getName());
        memSearch.create(TDemoInfoImpl.class);

        List<TDemoInfoImpl> demoInfos = new ArrayList<>();
        IntStream.range(0, 10).forEach(index -> {
            TDemoInfoImpl demoInfo = new TDemoInfoImpl();
            demoInfo.setId(index);
            demoInfo.setUuid(IdHelper.createUuid());
            demoInfo.setDemoTitle("Demo" + index);

            demoInfos.add(demoInfo);
        });
        memSearch.addData(demoInfos);
        List<?> query = memSearch.query("");
        for (Object o : query) {
            System.out.println(o);
        }

        //memSearch.remove();
    }
}
