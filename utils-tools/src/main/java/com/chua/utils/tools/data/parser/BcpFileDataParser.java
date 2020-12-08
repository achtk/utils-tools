package com.chua.utils.tools.data.parser;

import lombok.Getter;
import lombok.Setter;

/**
 * bcp
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/8
 */
@Getter
@Setter
public class BcpFileDataParser extends CsvFileDataParser implements DataParser {

    {
        setDelimiter("[\t\\s+]");
    }
}
