package top.arhi.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * 微信公众号：三友的java日记
 *
 * @author sanyou
 * @date 2023/2/9 15:12
 */

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(connectionFactory);
        return redisMessageListenerContainer;
    }

    @Bean
    public KeyExpirationEventMessageListener redisKeyExpirationListener(RedisMessageListenerContainer redisMessageListenerContainer) {
        return new KeyExpirationEventMessageListener(redisMessageListenerContainer);
    }

}

