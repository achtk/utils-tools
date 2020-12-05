package com.chua.utils.netx.data;

import com.chua.utils.netx.data.calcite.AbstractFileDataSchema;
import com.chua.utils.tools.collects.HashOperateMap;
import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.data.DataSchema;
import com.chua.utils.tools.data.table.DataTable;
import com.chua.utils.tools.data.table.FileDataTable;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import lombok.NoArgsConstructor;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;

import java.util.Map;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/4
 */
@NoArgsConstructor
public class CalciteFileDataSchema implements DataSchema<String>, SchemaFactory {

    private AbstractFileDataSchema fileDataSchema;
    private String data;
    private String schema = "system";
    private static final String DATA_FILE = "dataFile";

    public CalciteFileDataSchema(String data) {
        this.initial(data);
    }

    @Override
    public Schema create(SchemaPlus schemaPlus, String s, Map<String, Object> map) {
        if (null == fileDataSchema) {
            Object dataFile = map.get(DATA_FILE);
            if(null == dataFile) {
                throw new NullPointerException("文件不存在");
            }
            initial(dataFile.toString());
            fileDataSchema.setFile(dataFile.toString());
        }
        fileDataSchema.setMap(map);
        fileDataSchema.setS(s);
        fileDataSchema.setSchemaPlus(schemaPlus);
        return fileDataSchema;
    }

    @Override
    public void initial(String data) {
        this.data = data;
        String extension1 = FileHelper.getExtension(data);
        AbstractFileDataSchema fileDataSchema = ExtensionFactory.getExtensionLoader(AbstractFileDataSchema.class).getExtension(extension1);
        if (null == fileDataSchema) {
            throw new UnsupportedOperationException(extension1 + ":不存在");
        }
        this.data = data;
        this.fileDataSchema = fileDataSchema;
    }

    @Override
    public DataTable getTable() {
        FileDataTable fileDataTable = new FileDataTable();
        fileDataTable.setFile(data);
        fileDataTable.setTable(FileHelper.getName(data));
        return fileDataTable;
    }

    @Override
    public String schema() {
        return schema;
    }

    @Override
    public void schema(String schema) {
        this.schema = schema;
    }

    @Override
    public HashOperateMap operand() {
        HashOperateMap operateMap = HashOperateMap.create();
        operateMap.put(DATA_FILE, data);
        return operateMap;
    }
}
