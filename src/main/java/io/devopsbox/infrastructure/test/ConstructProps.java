package io.devopsbox.infrastructure.test;

import java.io.Serializable;

public class ConstructProps implements Serializable {
    private final String companyName;
    private final String envName;
    private final String appName;

    public ConstructProps(String companyName, String envName, String appName) {
        this.companyName = companyName;
        this.envName = envName;
        this.appName = appName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getEnvName() {
        return envName;
    }

    public String getAppName() {
        return appName;
    }
}
