package com.xxl.job.autoconfigure;

import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * xxl-job executor auto-configuration.
 *
 * <p>Creates a {@link XxlJobSpringExecutor} bean from {@link XxlJobProperties} when:
 * <ul>
 *     <li>xxl-job-core is on the classpath;</li>
 *     <li>"xxl.job.executor.enabled" is not "false";</li>
 *     <li>no user-defined {@link XxlJobExecutor} bean exists.</li>
 * </ul>
 *
 * @author xxl-job
 */
@AutoConfiguration
@ConditionalOnClass(XxlJobSpringExecutor.class)
@EnableConfigurationProperties(XxlJobProperties.class)
@ConditionalOnProperty(prefix = "xxl.job.executor", name = "enabled", havingValue = "true", matchIfMissing = true)
public class XxlJobAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(XxlJobAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean(XxlJobExecutor.class)
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobProperties properties, Environment environment) {
        logger.info(">>>>>>>>>>> xxl-job executor auto-config init.");

        XxlJobProperties.Admin admin = properties.getAdmin();
        XxlJobProperties.Executor executor = properties.getExecutor();

        // resolve appname, fallback to "spring.application.name"
        String appname = executor.getAppname();
        if (!StringUtils.hasText(appname)) {
            appname = environment.getProperty("spring.application.name");
        }
        Assert.hasText(appname,
                "xxl-job executor appname must not be empty, " +
                "please configure \"xxl.job.executor.appname\" or \"spring.application.name\".");

        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(admin.getAddresses());
        xxlJobSpringExecutor.setAccessToken(admin.getAccessToken());
        xxlJobSpringExecutor.setTimeout(admin.getTimeout());
        xxlJobSpringExecutor.setEnabled(executor.isEnabled());
        xxlJobSpringExecutor.setAppname(appname);
        xxlJobSpringExecutor.setAddress(executor.getAddress());
        xxlJobSpringExecutor.setIp(executor.getIp());
        xxlJobSpringExecutor.setPort(executor.getPort());
        xxlJobSpringExecutor.setLogPath(executor.getLogpath());
        xxlJobSpringExecutor.setLogRetentionDays(executor.getLogRetentionDays());
        if (StringUtils.hasText(executor.getExcludedPackage())) {
            xxlJobSpringExecutor.setExcludedPackage(executor.getExcludedPackage());
        }
        return xxlJobSpringExecutor;
    }

}
