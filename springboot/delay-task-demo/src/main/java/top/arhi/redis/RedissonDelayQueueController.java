package top.arhi.redis;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 微信公众号：三友的java日记
 *
 * @author sanyou
 * @date 2023/2/12 18:14
 */
@RestController
public class RedissonDelayQueueController {

    @Resource
    private RedissonDelayQueue redissonDelayQueue;

    @GetMapping("/redisson/add")
    public void addTask(@RequestParam("task") String task) {
        redissonDelayQueue.offerTask(task, 5);
    }

}
