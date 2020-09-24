package com.chua.utils.tools.common;

import java.io.OutputStream;
import java.lang.management.*;
import java.util.concurrent.*;

import static com.chua.utils.tools.constant.StringConstant.DEFAULT;

/**
 * 线程工具
 *
 * @author CH
 */
public class ThreadHelper {

    /**
     *
     */
    private static final int SINGLETON = 1;
    /**
     *
     */
    private static final long KEEP_ALIVE_TIME = 0L;

    private static final int PROCESSOR = processor();

    /**
     * @return
     */
    public static ExecutorService newSingleThreadExecutor() {
        return newSingleThreadExecutor(DEFAULT);
    }

    /**
     * @return
     */
    public static ExecutorService newSingleThreadExecutor(String name) {
        return new ThreadPoolExecutor(
                SINGLETON,
                SINGLETON,
                KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                newThreadFactory(name)

        );
    }

    /**
     * @return
     */
    public static ExecutorService newForkJoinPool() {
        return ForkJoinPool.commonPool();
    }

    /**
     * @return
     */
    public static ExecutorService newFixedThreadExecutor(final int max) {
        return newFixedThreadExecutor(max, DEFAULT);
    }

    /**
     * @return
     */
    public static ExecutorService newProcessorThreadExecutor() {
        return newFixedThreadExecutor(PROCESSOR);
    }

    /**
     * @return
     */
    public static ExecutorService newProcessorThreadExecutor(final int core) {
        return newFixedThreadExecutor(Math.min(core, PROCESSOR));
    }

    /**
     * @return
     */
    public static ExecutorService newProcessorThreadExecutor(final String name) {
        return newFixedThreadExecutor(PROCESSOR, name);
    }

    /**
     * @return
     */
    public static ExecutorService newMinThreadExecutor(final int value1, final int value2) {
        return newFixedThreadExecutor(Math.min(value1, value2));
    }

    /**
     * @return
     */
    public static ExecutorService newMinThreadExecutor(final int value1, final int value2, final String name) {
        return newFixedThreadExecutor(Math.min(value1, value2), name);
    }

    /**
     * @return
     */
    public static ExecutorService newMaxThreadExecutor(final int value1, final int value2) {
        return newFixedThreadExecutor(Math.max(value1, value2));
    }

    /**
     * @return
     */
    public static ExecutorService newMaxThreadExecutor(final int value1, final int value2, final String name) {
        return newFixedThreadExecutor(Math.max(value1, value2), name);
    }

    /**
     * @return
     */
    public static ExecutorService newCachedThreadPool() {
        return newCachedThreadPool(DEFAULT);
    }

    /**
     * @return
     */
    public static ExecutorService newCachedThreadPool(final String name) {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), newThreadFactory(name));
    }

    /**
     * @return
     */
    public static ScheduledExecutorService newScheduledThreadPoolExecutor(final int max) {
        return new ScheduledThreadPoolExecutor(max, newThreadFactory(DEFAULT));
    }

    /**
     * @return
     */
    public static ScheduledExecutorService newScheduledThreadPoolExecutor(final int max, final String name) {
        return new ScheduledThreadPoolExecutor(max, newThreadFactory(name));
    }

    /**
     * @return
     */
    public static ScheduledExecutorService newScheduledThreadPoolExecutor(final String name) {
        return new ScheduledThreadPoolExecutor(1, newThreadFactory(name));
    }

    /**
     * @return
     */
    public static Thread newThread(final Runnable runnable) {
        return new Thread(runnable);
    }

    /**
     * @return
     */
    public static void newAndRunThread(final Runnable runnable) {
        ExecutorService executorService = newSingleThreadExecutor();
        executorService.execute(runnable);
    }

    /**
     * @param thread 线程数
     * @param name   名称
     * @return
     */
    public static ExecutorService newFixedThreadExecutor(int thread, String name) {
        return Executors.newFixedThreadPool(thread, newThreadFactory(name));
    }

    /**
     * @param time
     */
    public static void gracefully(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param name
     * @return
     */
    public static ThreadFactory newThreadFactory(final String name) {
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName(name);
                if (thread.isDaemon()) {
                    thread.setDaemon(false);
                }
                if (thread.getPriority() != Thread.NORM_PRIORITY) {
                    thread.setPriority(Thread.NORM_PRIORITY);
                }
                return thread;
            }
        };
    }

    /**
     * @return
     */
    public static int processor() {
        return Runtime.getRuntime().availableProcessors() * 2 - 1;
    }

    /**
     * 获取CompletedFuture
     *
     * @return
     */
    public static <T> CompletableFuture<T> newCompletableFuture() {
        return new CompletableFuture<T>();
    }


    /**
     * 暂停
     *
     * @param time 时间
     */
    public static void sleep(long time) throws InterruptedException {
        Thread.sleep(time);
    }

    /**
     * 暂停
     *
     * @param time     时间
     * @param timeUnit 类型
     */
    public static void sleep(long time, TimeUnit timeUnit) throws InterruptedException {
        long unit = timeUnit.toMillis(time);
        Thread.sleep(unit);
    }

    /**
     * 暂停
     *
     * @param time     时间
     * @param timeUnit 类型
     */
    public static void sleepQuietly(long time, TimeUnit timeUnit) {
        try {
            long unit = timeUnit.toMillis(time);
            Thread.sleep(unit);
        } catch (InterruptedException e) {
        }
    }

    /**
     * 暂停
     *
     * @param time 时间
     */
    public static void sleepSecondsQuietly(long time) {
        sleepQuietly(time, TimeUnit.SECONDS);
    }

    /**
     * 暂停
     *
     * @param time 时间
     */
    public static void sleepMillisecondsQuietly(long time) {
        sleepQuietly(time, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取jstack
     *
     * @param stream 输出流
     * @throws Exception
     */
    public static void jstack(OutputStream stream) throws Exception {
        ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
        for (ThreadInfo threadInfo : threadMxBean.dumpAllThreads(true, true)) {
            stream.write(getThreadDumpString(threadInfo).getBytes());
        }
    }

    /**
     * 获取线程信息
     *
     * @param threadInfo
     * @return
     */
    private static String getThreadDumpString(ThreadInfo threadInfo) {
        StringBuilder sb = new StringBuilder("\"" + threadInfo.getThreadName() + "\"" +
                " Id=" + threadInfo.getThreadId() + " " +
                threadInfo.getThreadState());
        if (threadInfo.getLockName() != null) {
            sb.append(" on " + threadInfo.getLockName());
        }
        if (threadInfo.getLockOwnerName() != null) {
            sb.append(" owned by \"" + threadInfo.getLockOwnerName() +
                    "\" Id=" + threadInfo.getLockOwnerId());
        }
        if (threadInfo.isSuspended()) {
            sb.append(" (suspended)");
        }
        if (threadInfo.isInNative()) {
            sb.append(" (in native)");
        }
        sb.append('\n');
        int i = 0;

        StackTraceElement[] stackTrace = threadInfo.getStackTrace();
        MonitorInfo[] lockedMonitors = threadInfo.getLockedMonitors();
        for (; i < stackTrace.length && i < 32; i++) {
            StackTraceElement ste = stackTrace[i];
            sb.append("\tat " + ste.toString());
            sb.append('\n');
            if (i == 0 && threadInfo.getLockInfo() != null) {
                Thread.State ts = threadInfo.getThreadState();
                switch (ts) {
                    case BLOCKED:
                        sb.append("\t-  blocked on " + threadInfo.getLockInfo());
                        sb.append('\n');
                        break;
                    case WAITING:
                        sb.append("\t-  waiting on " + threadInfo.getLockInfo());
                        sb.append('\n');
                        break;
                    case TIMED_WAITING:
                        sb.append("\t-  waiting on " + threadInfo.getLockInfo());
                        sb.append('\n');
                        break;
                    default:
                }
            }

            for (MonitorInfo mi : lockedMonitors) {
                if (mi.getLockedStackDepth() == i) {
                    sb.append("\t-  locked " + mi);
                    sb.append('\n');
                }
            }
        }
        if (i < stackTrace.length) {
            sb.append("\t...");
            sb.append('\n');
        }

        LockInfo[] locks = threadInfo.getLockedSynchronizers();
        if (locks.length > 0) {
            sb.append("\n\tNumber of locked synchronizers = " + locks.length);
            sb.append('\n');
            for (LockInfo li : locks) {
                sb.append("\t- " + li);
                sb.append('\n');
            }
        }
        sb.append('\n');
        return sb.toString();
    }

    /**
     * 关闭线程池
     *
     * @param executorService
     */
    public static void closeQuietly(final ExecutorService executorService) {
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    /**
     * 关闭线程
     *
     * @param thread
     */
    public static void closeQuietly(final Thread thread) {
        if (thread != null) {
            thread.interrupt();
        }
    }

    /**
     * @return
     */
    public static CountDownLatch newProcessorCountDownLatch() {
        return new CountDownLatch(processor());
    }

    /**
     * @param i
     * @return
     */
    public static CountDownLatch newFixedCountDownLatch(int i) {
        return new CountDownLatch(i);
    }
}
