package org.noahsark.rpc.socket.remote;

import org.noahsark.rpc.common.thread.ServiceThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 通用任务处理线程
 *
 * @author zhangxt
 * @date 2022/10/18 09:55
 **/
public class CommonServiceThread extends ServiceThread {

    private static Logger log = LoggerFactory.getLogger(CommonServiceThread.class);

    /**
     * 超时时间
     */
    private static final int TIMEOUT_MS = 60 * 1000;

    // 任务队列
    private BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>(100);

    public boolean offer(Runnable task) {
        return tasks.offer(task);
    }

    @Override
    public void run() {
        log.info("common thread start in {}", LocalDateTime.now());

        Runnable task;

        while (!this.isStopped()) {
            try {

                task = tasks.poll(TIMEOUT_MS, TimeUnit.MILLISECONDS);

                if (task != null) {
                    task.run();
                }

            } catch (Exception ex) {
                log.warn("catch an exception!", ex);
            }
        }

        log.info(" {} stop in {}", this.getServiceName(), LocalDateTime.now());
    }

    @Override
    public String getServiceName() {
        return "common-service-thread";
    }
}
