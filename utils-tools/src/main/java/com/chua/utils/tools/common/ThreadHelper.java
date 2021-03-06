package com.chua.utils.tools.common;

import java.lang.management.LockInfo;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.util.concurrent.*;

import static com.chua.utils.tools.constant.NumberConstant.THIRTY_TWO;
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
     * 单例线程池
     *
     * @param name 线程池名称
     * @return
     */
    public static ExecutorService newSingleThreadExecutor(String name) {
        return new ThreadPoolExecutor(
                SINGLETON,
                SINGLETON,
                KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                newThreadFactory(name)

        );
    }

    /**
     * 单例线程池
     *
     * @param threadFactory 线程池
     * @return
     */
    public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(
                SINGLETON,
                SINGLETON,
                KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory
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
     * @param name 线程池名称
     * @return
     */
    public static ExecutorService newCachedThreadPool(final String name) {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(), newThreadFactory(name));
    }

    /**
     * @param threadFactory 线程池
     * @return
     */
    public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(), threadFactory);
    }

    /**
     * @param thread 线程数
     * @return
     */
    public static ScheduledExecutorService newScheduledThreadPoolExecutor(final int thread) {
        return new ScheduledThreadPoolExecutor(thread, newThreadFactory(DEFAULT));
    }

    /**
     * @param thread 线程数
     * @param name   线程池名称
     * @return
     */
    public static ScheduledExecutorService newScheduledThreadPoolExecutor(final int thread, final String name) {
        return new ScheduledThreadPoolExecutor(thread, newThreadFactory(name));
    }

    /**
     * @param thread        线程数
     * @param threadFactory 线程池
     * @return
     */
    public static ScheduledExecutorService newScheduledThreadPoolExecutor(final int thread, final ThreadFactory threadFactory) {
        return new ScheduledThreadPoolExecutor(thread, threadFactory);
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
     * @param thread        线程数
     * @param threadFactory 线程池
     * @return
     */
    public static ExecutorService newFixedThreadExecutor(int thread, ThreadFactory threadFactory) {
        return Executors.newFixedThreadPool(thread, threadFactory);
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
        return r -> {
            Thread thread = new Thread(r);
            thread.setName(name);
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            if (thread.getPriority() != Thread.NORM_PRIORITY) {
                thread.setPriority(Thread.NORM_PRIORITY);
            }
            return thread;
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
        return new CompletableFuture<>();
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
        } catch (InterruptedException ignore) {
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
     * 获取线程信息
     *
     * @param threadInfo
     * @return
     */
    private static String getThreadDumpString(ThreadInfo threadInfo) {
        StringBuilder sb = new StringBuilder("\"");
        sb.append(threadInfo.getThreadName()).append("\"");
        sb.append(" Id=").append(threadInfo.getThreadId()).append(" ");
        sb.append(threadInfo.getThreadState());
        if (threadInfo.getLockName() != null) {
            sb.append(" on ");
            sb.append(threadInfo.getLockName());
        }
        if (threadInfo.getLockOwnerName() != null) {
            sb.append(" owned by \"");
            sb.append(threadInfo.getLockOwnerName());
            sb.append("\" Id=");
            sb.append(threadInfo.getLockOwnerId());
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
        for (; i < stackTrace.length && i < THIRTY_TWO; i++) {
            StackTraceElement ste = stackTrace[i];
            sb.append("\tat ");
            sb.append(ste.toString());
            sb.append('\n');
            if (i == 0 && threadInfo.getLockInfo() != null) {
                Thread.State ts = threadInfo.getThreadState();
                switch (ts) {
                    case BLOCKED:
                        sb.append("\t-  blocked on ");
                        break;
                    case WAITING:
                        sb.append("\t-  waiting on ");
                        break;
                    case TIMED_WAITING:
                        sb.append("\t-  timed waiting on ");
                        break;
                    default:
                }

                sb.append(threadInfo.getLockInfo());
                sb.append('\n');
            }

            for (MonitorInfo mi : lockedMonitors) {
                if (mi.getLockedStackDepth() == i) {
                    sb.append("\t-  locked ");
                    sb.append(mi);
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
            sb.append("\n\tNumber of locked synchronizers = ");
            sb.append(locks.length);
            sb.append('\n');
            for (LockInfo li : locks) {
                sb.append("\t- ");
                sb.append(li);
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
