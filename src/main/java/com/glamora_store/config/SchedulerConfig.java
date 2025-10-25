package com.glamora_store.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling // Khi có annotation này, Spring sẽ tìm tất cả các method có @Scheduled trong
                  // toàn bộ project, và tự động chạy định kỳ theo thời gian bạn chỉ định.
public class SchedulerConfig {
}
