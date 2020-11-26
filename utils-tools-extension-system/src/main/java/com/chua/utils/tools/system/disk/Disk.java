package com.chua.utils.tools.system.disk;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 磁盘
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/26
 */
@Data
public class Disk {

    /**
     * The disk name
     *
     * @return the name
     */
    String name;

    /**
     * The disk model
     *
     * @return the model
     */
    String model;

    /**
     * The disk serial number, if available.
     *
     * @return the serial number
     */
    String serial;

    /**
     * The size of the disk
     *
     * @return the disk size, in bytes
     */
    long size;
    /**
     * The size of the disk
     *
     * @return the disk size, in bytes
     */
    String sizeSize;

    /**
     * The number of reads from the disk
     *
     * @return the reads
     */
    long reads;
    /**
     * The number of reads from the disk
     *
     * @return the reads
     */
    String readsSize;

    /**
     * The number of bytes read from the disk
     *
     * @return the bytes read
     */
    long readBytes;
    /**
     * The number of bytes read from the disk
     *
     * @return the bytes read
     */
    String readBytesSize;
    /**
     * The number of writes to the disk
     *
     * @return the writes
     */
    long writes;
    /**
     * The number of writes to the disk
     *
     * @return the fWrites
     */
    String writesSize;
    /**
     * The number of bytes written to the disk
     *
     * @return the bytes written
     */
    long writeBytes;
    /**
     * The number of bytes written to the disk
     *
     * @return the bytes written
     */
    String writeBytesSize;

    /**
     * The length of the disk queue (#I/O's in progress). Includes I/O requests that
     * have been issued to the device driver but have not yet completed. Not
     * supported on macOS.
     *
     * @return the current disk queue length
     */
    long currentQueueLength;

    /**
     * The time spent reading or writing, in milliseconds.
     *
     * @return the transfer time
     */
    long transferTime;

    /**
     * The partitions on this disk.
     *
     * @return an {@code UnmodifiableList} of the partitions on this drive.
     */
    List<Map<String, Object>> partitions;

    /**
     * The time this disk's statistics were updated.
     *
     * @return the timeStamp, in milliseconds since the epoch.
     */
    long timeStamp;

    /**
     * Make a best effort to update all the statistics about the drive without
     * needing to recreate the drive list. This method provides for more frequent
     * periodic updates of individual drive statistics but may be less efficient to
     * use if updating all drives. It will not detect if a removable drive has been
     * removed and replaced by a different drive in between method calls.
     *
     * @return True if the update was (probably) successful, false if the disk was
     * not found
     */
    boolean updateAttributes;
}
