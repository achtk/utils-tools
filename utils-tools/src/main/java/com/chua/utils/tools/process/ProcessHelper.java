package com.chua.utils.tools.process;

import com.chua.utils.tools.common.IOHelper;
import com.chua.utils.tools.common.charset.CharsetHelper;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 进程工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
public class ProcessHelper {

    private static final Pattern COMMANDS_PATTERN = Pattern.compile("[\\,\\s+]{1}");
    /**
     * 创建进程<br>
     * 命令带参数时参数可作为其中一个参数，也可以将命令和参数组合为一个字符串传入
     *
     * @param cmd 命令
     * @return {@link Process}
     */
    public static Process createProcess(String cmd) {
        if (Strings.isNullOrEmpty(cmd)) {
            throw new NullPointerException("Command is empty !");
        }
        final List<String> commands = Splitter.on(COMMANDS_PATTERN).trimResults().omitEmptyStrings().splitToList(cmd);
        try {
            return new ProcessBuilder(commands).redirectErrorStream(true).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行命令<br>
     * 命令带参数时参数可作为其中一个参数，也可以将命令和参数组合为一个字符串传入
     *
     * @param command 命令
     * @return {@link Process}
     */
    public static String exec(String command) {
        return exec(command, CharsetHelper.systemCharset());
    }

    /**
     * 执行命令<br>
     * 命令带参数时参数可作为其中一个参数，也可以将命令和参数组合为一个字符串传入
     *
     * @param command 命令
     * @param charset 编码
     * @return {@link Process}
     */
    public static String exec(String command, Charset charset) {
        InputStream in = null;
        Process process = createProcess(command);
        try {
            in = process.getInputStream();
            return IOHelper.toString(in, charset);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOHelper.closeQuietly(in);
            process.destroy();
        }
        return null;
    }
}
