package com.xxl.job.autoconfigure;

import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * test for {@link XxlJobAutoConfiguration}
 *
 * @author xxl-job
 */
class XxlJobAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(XxlJobAutoConfiguration.class))
            .withPropertyValues(
                    "xxl.job.executor.appname=xxl-job-executor-test",
                    "xxl.job.executor.logpath=/tmp/xxl-job-test/jobhandler");

    @Test
    void executorBeanCreatedWithProperties() {
        contextRunner
                .withPropertyValues(
                        "xxl.job.admin.addresses=http://127.0.0.1:9090/xxl-job-admin",
                        "xxl.job.admin.accessToken=default_token",
                        "xxl.job.admin.timeout=5",
                        "xxl.job.executor.port=0",
                        "xxl.job.executor.logretentiondays=7")
                .run(context -> {
                    assertThat(context).hasSingleBean(XxlJobSpringExecutor.class);
                    XxlJobSpringExecutor executor = context.getBean(XxlJobSpringExecutor.class);
                    assertThat(ReflectionTestUtils.getField(executor, "adminAddresses"))
                            .isEqualTo("http://127.0.0.1:9090/xxl-job-admin");
                    assertThat(ReflectionTestUtils.getField(executor, "accessToken")).isEqualTo("default_token");
                    assertThat(ReflectionTestUtils.getField(executor, "timeout")).isEqualTo(5);
                    assertThat(ReflectionTestUtils.getField(executor, "appname")).isEqualTo("xxl-job-executor-test");
                    assertThat(ReflectionTestUtils.getField(executor, "logRetentionDays")).isEqualTo(7);
                });
    }

    @Test
    void executorBeanCreatedByDefault() {
        contextRunner.run(context -> assertThat(context).hasSingleBean(XxlJobSpringExecutor.class));
    }

    @Test
    void autoConfigurationBacksOffWhenDisabled() {
        contextRunner
                .withPropertyValues("xxl.job.executor.enabled=false")
                .run(context -> assertThat(context).doesNotHaveBean(XxlJobExecutor.class));
    }

    @Test
    void autoConfigurationBacksOffWhenUserDefinesExecutor() {
        contextRunner
                .withUserConfiguration(CustomExecutorConfiguration.class)
                .run(context -> {
                    assertThat(context).hasSingleBean(XxlJobExecutor.class);
                    assertThat(context.getBean(XxlJobExecutor.class))
                            .isSameAs(context.getBean("customXxlJobExecutor"));
                });
    }

    @Test
    void appnameFallbackToSpringApplicationName() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(XxlJobAutoConfiguration.class))
                .withPropertyValues(
                        "spring.application.name=xxl-job-executor-fallback",
                        "xxl.job.executor.logpath=/tmp/xxl-job-test/jobhandler")
                .run(context -> {
                    assertThat(context).hasSingleBean(XxlJobSpringExecutor.class);
                    assertThat(ReflectionTestUtils.getField(
                            context.getBean(XxlJobSpringExecutor.class), "appname"))
                            .isEqualTo("xxl-job-executor-fallback");
                });
    }

    @Test
    void contextFailsWhenAppnameMissing() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(XxlJobAutoConfiguration.class))
                .run(context -> {
                    assertThat(context).hasFailed();
                    assertThat(context.getStartupFailure()).hasMessageContaining("xxl.job.executor.appname");
                });
    }

    @Configuration(proxyBeanMethods = false)
    static class CustomExecutorConfiguration {

        @Bean
        XxlJobExecutor customXxlJobExecutor() {
            return new XxlJobExecutor();
        }
    }

}
