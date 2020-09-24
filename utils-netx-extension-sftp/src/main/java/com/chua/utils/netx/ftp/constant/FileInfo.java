package com.chua.utils.netx.ftp.constant;

import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

/**
 * @author CHTK
 */
@Getter
@Setter
public class FileInfo {
    private String name;
    private String group;
    private String link;
    private long size;
    private String type;
    private Calendar timestamp;
    private boolean directory;
    private boolean file;

}
