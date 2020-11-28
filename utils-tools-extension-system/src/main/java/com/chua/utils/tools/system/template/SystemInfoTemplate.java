package com.chua.utils.tools.system.template;

import com.chua.utils.tools.system.disk.*;
import net.sf.cglib.beans.BeanMap;
import oshi.SystemInfo;
import oshi.hardware.*;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 系统信息模板
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/26
 */
public class SystemInfoTemplate {

    private static final int K = 1024;
    private static final float F_K = 1024.0f;
    private static final int DEFAULT_SIZE = 1 << 4;
    private SystemInfo systemInfo = new SystemInfo();
    private DecimalFormat format = new DecimalFormat("###.0");
    /**
     * 显卡信息
     *
     * @return 显卡信息
     */
    public List<Map<String, Object>> createGraphicsCardInfo() {
        List<GraphicsCard> data = systemInfo.getHardware().getGraphicsCards();
        return null == data ? Collections.emptyList() : data.stream().map(new Function<GraphicsCard, Map<String, Object>>() {
            @Override
            public Map<String, Object> apply(GraphicsCard data) {
                Map<String, Object> param = new HashMap<>(DEFAULT_SIZE);
                param.putAll(BeanMap.create(data));
                param.put("VRamSize", getNetFileSizeDescription(Long.parseLong(param.getOrDefault("VRam", "0").toString())));
                return param;
            }
        }).collect(Collectors.toList());
    }
    /**
     * 声卡信息
     *
     * @return 声卡信息
     */
    public List<Map<String, Object>> createSoundCardInfo() {
        List<SoundCard> data = systemInfo.getHardware().getSoundCards();
        return null == data ? Collections.emptyList() : data.stream().map(new Function<SoundCard, Map<String, Object>>() {
            @Override
            public Map<String, Object> apply(SoundCard data) {
                Map<String, Object> param = new HashMap<>(DEFAULT_SIZE);
                param.putAll(BeanMap.create(data));
                return param;
            }
        }).collect(Collectors.toList());
    }
    /**
     * 驱动信息
     *
     * @return 驱动信息
     */
    public List<Map<String, Object>> createUsbDeviceInfo() {
        List<UsbDevice> data = systemInfo.getHardware().getUsbDevices(true);
        return null == data ? Collections.emptyList() : data.stream().map(new Function<UsbDevice, Map<String, Object>>() {
            @Override
            public Map<String, Object> apply(UsbDevice data) {
                Map<String, Object> param = new HashMap<>(DEFAULT_SIZE);
                param.putAll(BeanMap.create(data));
                return param;
            }
        }).collect(Collectors.toList());
    }
    /**
     * 显示器信息
     *
     * @return 显示器信息
     */
    public List<Map<String, Object>> createDisplayInfo() {
        List<Display> data = systemInfo.getHardware().getDisplays();
        return null == data ? Collections.emptyList() : data.stream().map(new Function<Display, Map<String, Object>>() {
            @Override
            public Map<String, Object> apply(Display display) {
                Map<String, Object> param = new HashMap<>(DEFAULT_SIZE);
                param.putAll(BeanMap.create(display));
                return param;
            }
        }).collect(Collectors.toList());
    }
    /**
     * 网卡信息
     *
     * @return 网卡信息
     */
    public List<Map<String, Object>> createNetworkInfo() {
        List<NetworkIF> networkifs = systemInfo.getHardware().getNetworkIFs();
        return null == networkifs ? Collections.emptyList() : networkifs.stream().map(new Function<NetworkIF, Map<String, Object>>() {
            @Override
            public Map<String, Object> apply(NetworkIF networkif) {
                Map<String, Object> param = new HashMap<>(DEFAULT_SIZE);
                param.putAll(BeanMap.create(networkif));
                return param;
            }
        }).collect(Collectors.toList());
    }

    /**
     * 内存信息
     *
     * @return 内存信息
     */
    public CpuInfo createCpuInfo() {
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        CpuInfo cpuInfo = new CpuInfo();
        cpuInfo.putAll(BeanMap.create(processor));
        return cpuInfo;
    }

    /**
     * 内存信息
     *
     * @return 内存信息
     */
    public MemoryInfo createMemoryInfo() {
        GlobalMemory globalMemory = systemInfo.getHardware().getMemory();
        MemoryInfo memoryInfo = new MemoryInfo();
        memoryInfo.setTotal(globalMemory.getTotal());
        memoryInfo.setTotalSize(getNetFileSizeDescription(globalMemory.getTotal()));
        memoryInfo.setAvailable(globalMemory.getAvailable());
        memoryInfo.setAvailableSize(getNetFileSizeDescription(globalMemory.getAvailable()));
        memoryInfo.setVirtualMemory(BeanMap.create(globalMemory.getVirtualMemory()));
        memoryInfo.setPhysicalMemorys(null != globalMemory.getPhysicalMemory() ? globalMemory.getPhysicalMemory().stream().map(new Function<PhysicalMemory, Map<String, Object>>() {
            @Override
            public Map<String, Object> apply(PhysicalMemory physicalMemory) {
                Map<String, Object> result = new HashMap<>(DEFAULT_SIZE);
                result.putAll(BeanMap.create(physicalMemory));
                return result;
            }
        }).collect(Collectors.toList()) : null);

        return memoryInfo;
    }


    /**
     * 创建磁盘信息
     *
     * @return 磁盘信息
     */
    public DiskInfo createDiskInfo() {
        DiskInfo diskInfo = new DiskInfo();
        List<HWDiskStore> diskStores = systemInfo.getHardware().getDiskStores();
        for (HWDiskStore diskStore : diskStores) {
            Disk disk = new Disk();
            disk.setName(diskStore.getName());
            disk.setModel(diskStore.getModel());
            disk.setCurrentQueueLength(diskStore.getCurrentQueueLength());
            disk.setReads(diskStore.getReads());
            disk.setReadsSize(getNetFileSizeDescription(diskStore.getReads()));
            disk.setReadBytes(diskStore.getReadBytes());
            disk.setReadBytesSize(getNetFileSizeDescription(diskStore.getReads()));
            disk.setWrites(diskStore.getWrites());
            disk.setWritesSize(getNetFileSizeDescription(diskStore.getReads()));
            disk.setWriteBytes(diskStore.getWriteBytes());
            disk.setWriteBytesSize(getNetFileSizeDescription(diskStore.getWriteBytes()));
            disk.setSize(diskStore.getSize());
            disk.setSizeSize(getNetFileSizeDescription(diskStore.getSize()));
            disk.setSerial(diskStore.getSerial());
            disk.setPartitions(null != diskStore.getPartitions() ? diskStore.getPartitions().stream().map(new Function<HWPartition, Map<String, Object>>() {
                @Override
                public Map<String, Object> apply(HWPartition hwPartition) {
                    Map<String, Object> result = new HashMap<>(DEFAULT_SIZE);
                    result.putAll(BeanMap.create(hwPartition));
                    result.put("sizeSize", getNetFileSizeDescription(Long.parseLong(result.getOrDefault("size", "0").toString())));
                    return result;
                }
            }).collect(Collectors.toList()) : null);
            disk.setTimeStamp(diskStore.getTimeStamp());
            disk.setTransferTime(diskStore.getTransferTime());

            diskInfo.add(disk);
        }
        return diskInfo;
    }

    /**
     * @param size
     * @return
     */
    public String getNetFileSizeDescription(long size) {
        StringBuffer bytes = new StringBuffer();
        if (size >= K * K * K) {
            double i = (size / (F_K * F_K * F_K));
            bytes.append(format.format(i)).append("GB");
        } else if (size >= K * K) {
            double i = (size / (F_K * F_K));
            bytes.append(format.format(i)).append("MB");
        } else if (size >= K) {
            double i = (size / (F_K));
            bytes.append(format.format(i)).append("KB");
        } else if (size < K) {
            if (size <= 0) {
                bytes.append("0B");
            } else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }
}
