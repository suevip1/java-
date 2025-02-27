package top.arhi.hashwheel;

import cn.hutool.cron.timingwheel.SystemTimer;
import cn.hutool.cron.timingwheel.TimerTask;
import lombok.extern.slf4j.Slf4j;

/**
 * 微信公众号：三友的java日记
 *
 * @author sanyou
 * @date 2023/2/26 20:37
 */
@Slf4j
public class SystemTimerDemo {

    public static void main(String[] args) {
        SystemTimer systemTimer = new SystemTimer();
        systemTimer.start();

        log.info("提交延迟任务");
        systemTimer.addTask(new TimerTask(() -> log.info("执行延迟任务"), 5000));
    }

}
