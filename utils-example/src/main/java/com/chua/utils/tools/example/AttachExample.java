package com.chua.utils.tools.example;

import com.sun.tools.attach.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/11
 */
public class AttachExample {

    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        VirtualMachine virtualMachine = VirtualMachine.attach("2936");
        List<VirtualMachineDescriptor> virtualMachineDescriptors = VirtualMachine.list();
        File temp = new File("D:\\work\\utils-tools-parent\\utils-agent\\target\\utils-agent-1.0-SNAPSHOT.jar");
        System.out.println("文件存在: " + temp.exists());
        System.out.println("文件存在: " + temp.getParent());
        virtualMachine.loadAgent(temp.getAbsolutePath());
        System.out.println();
        while (true) {

        }
    }
}
