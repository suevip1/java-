package top.arhi.test;

import top.arhi.model.pojo.Body;
import top.arhi.util.JacksonUtil;

import java.util.List;

public class Demo36 {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String json = """
                            [{"name":"zhangsan","age":18},{"name":"lisi","age":23}]
                            """;
                    List<Body> ts = JacksonUtil.parseJsonArrToList(json, Body.class);
                    for (Body t : ts) {
                        System.out.println(Thread.currentThread().getName() + ":" + t);
                    }
                }
            });
            thread.start();
        }
    }
}
