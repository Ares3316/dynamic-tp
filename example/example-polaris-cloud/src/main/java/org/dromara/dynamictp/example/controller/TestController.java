/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.dynamictp.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.dromara.dynamictp.common.properties.DtpProperties;
import org.dromara.dynamictp.core.DtpRegistry;
import org.dromara.dynamictp.core.support.task.runnable.NamedRunnable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author fabian4
 */
@Slf4j
@RestController
@SuppressWarnings("all")
public class TestController {

    @Resource
    private ThreadPoolExecutor dtpExecutor1;

    @Resource
    private DtpProperties dtpProperties;

    @GetMapping("/index")
    public String index() throws InterruptedException {
        Thread.sleep((int) (Math.random() * 100));
        return "index";
    }

    @GetMapping("/dtp-polaris-cloud-example/test")
    public String test() throws InterruptedException {
        task();
        return "Success";
    }

    public void task() throws InterruptedException {
        Executor dtpExecutor2 = DtpRegistry.getExecutor("dtpExecutor2");
        for (int i = 0; i < 100; i++) {
            Thread.sleep((int) (Math.random() * 100));
            dtpExecutor1.execute(() -> {
                log.info("i am a dtp1 task");
            });
            dtpExecutor2.execute(NamedRunnable.of(() -> {
                try {
                    Thread.sleep((int) (Math.random() * 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("i am a dtp2 task");
            }, "task-" + i));
        }
    }
}
