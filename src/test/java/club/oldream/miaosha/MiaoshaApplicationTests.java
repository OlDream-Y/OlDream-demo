package club.oldream.miaosha;

import club.oldream.miaosha.rabbitmq.MQReceiver;
import club.oldream.miaosha.rabbitmq.MQSender;
import club.oldream.miaosha.rabbitmq.MiaoshaMessage;
import club.oldream.miaosha.redis.RedisConfig;
import club.oldream.miaosha.util.MD5Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

@SpringBootTest
class MiaoshaApplicationTests {


    @Test
    void contextLoads() {

    }

}
