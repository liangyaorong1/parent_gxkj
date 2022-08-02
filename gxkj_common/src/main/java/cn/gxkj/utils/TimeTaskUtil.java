package cn.gxkj.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Component
public class TimeTaskUtil {
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private Map<String, ScheduledFuture<?>> mapFuture = new HashMap<>();

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    /**
     * 启动定时任务
     *
     * @param flag      定时任务标识
     * @param runnable  具体业务实现
     * @param startTime 任务运行时间
     */
    public void startCron(String flag, Runnable runnable, Date startTime) {
        if (mapFuture.containsKey(flag)) {
            mapFuture.get(flag).cancel(true);
        }
        Date now = new Date();
        if (startTime.after(now)) {
            mapFuture.put(flag, threadPoolTaskScheduler.schedule(runnable, startTime));
            System.out.println("添加了定时任务："+flag);
        }
    }

    /**
     * 关闭定时任务
     *
     * @param flag 定时任务标识
     */
    public void stopCron(String flag) {
        if (mapFuture.containsKey(flag)) {
            mapFuture.get(flag).cancel(true);
            mapFuture.remove(flag);
        }
    }
}
