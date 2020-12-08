package com.chua.utils.tools.data.parser;

import com.chua.utils.tools.collects.HashOperateMap;
import com.chua.utils.tools.common.ArraysHelper;
import com.google.common.base.Strings;
import com.google.common.io.Resources;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * csv
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/8
 */
@Getter
@Setter
public class CsvFileDataParser implements DataParser {

    private Object source;
    private HashOperateMap operate;
    private String delimiter = ",";
    private URL url;
    private Map<String, String> header = new HashMap<>();
    private BufferedReader bufferedReader;
    private String readLine;

    @Override
    public void setSource(Object source) {
        this.source = source;
        this.url = Resources.getResource(source.toString());
        this.delimiter = null == operate ? delimiter : operate.getString("delimiter", delimiter);
        this.createHeader();
    }

    private void createHeader() {
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            String firstLine = bufferedReader.readLine();
            String[] lines =  ArraysHelper.splice(firstLine, delimiter, true);
            Arrays.stream(lines).forEach(column -> {
                if(Strings.isNullOrEmpty(column)) {
                    return;
                }
                String name = column.split(":")[0];
                String type = column.split(":")[1];
                header.put(name, type);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, String> getDataType() {
        return header;
    }

    @Override
    public Object[] getCurrent() {
        return ArraysHelper.splice(readLine, delimiter, true);
    }

    @Override
    public boolean hasNext() {
        String readLine = null;
        try {
            readLine = this.bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if(null == readLine) {
            return false;
        }
        this.readLine = readLine;
        return true;
    }

    @Override
    public void reset() {

    }
}
