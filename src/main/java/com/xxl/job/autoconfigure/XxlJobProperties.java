package com.xxl.job.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * xxl-job configuration properties, prefix "xxl.job".
 *
 * <p>Supports relaxed binding, both camelCase (e.g. "xxl.job.admin.accessToken")
 * and kebab-case (e.g. "xxl.job.admin.access-token") are valid.
 *
 * @author xxl-job
 */
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {

    /**
     * xxl-job admin config
     */
    private final Admin admin = new Admin();

    /**
     * xxl-job executor config
     */
    private final Executor executor = new Executor();

    public Admin getAdmin() {
        return admin;
    }

    public Executor getExecutor() {
        return executor;
    }

    /**
     * xxl-job admin config
     */
    public static class Admin {

        /**
         * xxl-job admin address list, such as "http://address" or "http://address01,http://address02".
         * If blank, the executor will start without registry to admin.
         */
        private String addresses;

        /**
         * xxl-job access token, must match the token configured in admin.
         */
        private String accessToken;

        /**
         * xxl-job admin request timeout, by second. Valid range is [1, 10], default 3s.
         */
        private int timeout = 3;

        public String getAddresses() {
            return addresses;
        }

        public void setAddresses(String addresses) {
            this.addresses = addresses;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }
    }

    /**
     * xxl-job executor config
     */
    public static class Executor {

        /**
         * Whether xxl-job executor is enabled, default true.
         */
        private boolean enabled = true;

        /**
         * xxl-job executor appname, used for registry and grouping in admin.
         * If blank, fallback to "spring.application.name".
         */
        private String appname;

        /**
         * xxl-job executor registry-address: default use address to registry,
         * otherwise use ip:port if address is blank.
         */
        private String address;

        /**
         * xxl-job executor ip: default auto-obtain the local ip if blank.
         */
        private String ip;

        /**
         * xxl-job executor port: default auto-select an available port (from 9999) if not positive.
         */
        private int port;

        /**
         * xxl-job executor log-path.
         */
        private String logpath = "/data/applogs/xxl-job/jobhandler";

        /**
         * xxl-job executor log-retention-days, log files older than this will be cleaned.
         */
        private int logRetentionDays = 30;

        /**
         * xxl-job executor excluded package, will skip job scan for matched beans.
         * Such as "org.package01" or "org.package01,org.package02".
         */
        private String excludedPackage;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getAppname() {
            return appname;
        }

        public void setAppname(String appname) {
            this.appname = appname;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getLogpath() {
            return logpath;
        }

        public void setLogpath(String logpath) {
            this.logpath = logpath;
        }

        public int getLogRetentionDays() {
            return logRetentionDays;
        }

        public void setLogRetentionDays(int logRetentionDays) {
            this.logRetentionDays = logRetentionDays;
        }

        public String getExcludedPackage() {
            return excludedPackage;
        }

        public void setExcludedPackage(String excludedPackage) {
            this.excludedPackage = excludedPackage;
        }
    }

}
