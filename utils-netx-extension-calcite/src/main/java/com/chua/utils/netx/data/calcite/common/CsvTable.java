package com.chua.utils.netx.data.calcite.common;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.calcite.DataContext;
import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.linq4j.AbstractEnumerable;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.ScannableTable;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.util.Pair;
import org.apache.calcite.util.Source;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * csv表
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/5
 */
@AllArgsConstructor
public final class CsvTable extends AbstractTable implements ScannableTable {

    private Source source;

    @Override
    public RelDataType getRowType(RelDataTypeFactory relDataTypeFactory) {
        JavaTypeFactory typeFactory = (JavaTypeFactory) relDataTypeFactory;

        List<String> names = Lists.newLinkedList();
        List<RelDataType> types = Lists.newLinkedList();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(source.file()));
            String line = reader.readLine();
            List<String> lines = Lists.newArrayList(line.split(","));
            lines.forEach(column -> {
                String name = column.split(":")[0];
                String type = column.split(":")[1];
                names.add(name);
                types.add(typeFactory.createSqlType(SqlTypeName.get(type)));
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return typeFactory.createStructType(Pair.zip(names, types));
    }

    @Override
    public Enumerable<Object[]> scan(DataContext dataContext) {
        return new AbstractEnumerable<Object[]>() {
            @Override
            public Enumerator<Object[]> enumerator() {
                return new Enumerator<Object[]>() {
                    private Object[] current;

                    private BufferedReader br;

                    {
                        try {
                            this.br = new BufferedReader(source.reader());
                            this.br.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public Object[] current() {
                        return current;
                    }

                    @Override
                    public boolean moveNext() {
                        try {
                            String line = br.readLine();
                            if (line == null) {
                                return false;
                            }

                            // 如果是多列，这里要多个值
                            current = line.split(",");
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                        return true;
                    }

                    /**
                     * 出现异常走这里
                     * */
                    @Override
                    public void reset() {
                    }

                    /**
                     * InputStream流在这里关闭
                     * */
                    @Override
                    public void close() {

                    }
                };
            }
        };
    }
}
