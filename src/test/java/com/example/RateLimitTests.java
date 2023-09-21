package com.example;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.example.domain.TestResp;
import com.example.domain.vo.TestRespVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@SpringBootTest
@Slf4j
class RateLimitTests {

    @Test
    public void test() throws InterruptedException {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", 3);
        //链式构建请求
        String result = HttpRequest.get("http://127.0.0.1:8088/test/limit")
                .header(Header.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOjEsInJuU3RyIjoiYlF0RnJnT1lYTlVoSG42M2xuSnc3Y0NucERlVlFNTloifQ.Kzf0IpRPa6rY9Ynd0xH5Ng78Ewbp5qt-qvO35cCTy3g")//头信息，多个头信息多次调用此方法即可
                .form(paramMap)//表单内容
                .timeout(20000)//超时，毫秒
                .execute().body();
        TypeReference<TestResp<TestRespVO>> typeReference = new TypeReference<TestResp<TestRespVO>>() {
        };
        log.info("返回：{}", JSONUtil.toBean(result, typeReference, false).getMsg());

        Thread.sleep(1000);

        // test
        testRateLimit(10);
    }

    @SneakyThrows
    public static void testRateLimit(int clientSize) {
        CountDownLatch downLatch = new CountDownLatch(clientSize);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(clientSize);
        IntStream.range(0, clientSize).forEach(i ->
                fixedThreadPool.submit(() -> {
                    //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
                    HashMap<String, Object> paramMap = new HashMap<>();
                    paramMap.put("id", 3);
                    //链式构建请求
                    String result = HttpRequest.get("http://127.0.0.1:8088/test/limit")
                            .header(Header.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOjEsInJuU3RyIjoiYlF0RnJnT1lYTlVoSG42M2xuSnc3Y0NucERlVlFNTloifQ.Kzf0IpRPa6rY9Ynd0xH5Ng78Ewbp5qt-qvO35cCTy3g")//头信息，多个头信息多次调用此方法即可
                            .form(paramMap)//表单内容
                            .timeout(20000)//超时，毫秒
                            .execute().body();
                    TypeReference<TestResp<TestRespVO>> typeReference = new TypeReference<TestResp<TestRespVO>>() {
                    };
                    log.info("返回：{}", JSONUtil.toBean(result, typeReference, false).getMsg());
                    downLatch.countDown();
                })
        );
        downLatch.await();
        fixedThreadPool.shutdown();
    }


}
