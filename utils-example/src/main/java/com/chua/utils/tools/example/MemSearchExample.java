package com.chua.utils.tools.example;

import com.chua.utils.netx.datasource.hsqldb.mem.HsqlSearch;
import com.chua.utils.netx.datasource.mem.MemSearch;
import com.chua.utils.netx.datasource.sqlite.mem.SqliteSearch;
import com.chua.utils.tools.example.entity.TDemoInfo;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import com.chua.utils.tools.text.IdHelper;
import org.hsqldb.util.DatabaseManagerSwing;
import org.springframework.boot.SpringApplication;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        memSearch.create(TDemoInfo.class);

        List<TDemoInfo> demoInfos = new ArrayList<>();
        IntStream.range(0, 10).forEach(index -> {
            TDemoInfo demoInfo = new TDemoInfo();
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
