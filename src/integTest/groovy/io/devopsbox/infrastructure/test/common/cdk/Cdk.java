package io.devopsbox.infrastructure.test.common.cdk;

import io.devopsbox.infrastructure.test.common.ProcessRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cdk {
    private static final Logger log = LoggerFactory.getLogger(Cdk.class);

    private final ProcessRunner processRunner;

    private final String awsRegion;
    private final boolean localstackEnabled;

    public Cdk(String awsRegion, boolean localstackEnabled) {
        this.awsRegion = awsRegion;
        this.localstackEnabled = localstackEnabled;
        this.processRunner = new ProcessRunner();
    }

    public void run(Map<String, String> environmentVariables, String cdkCommand) {
        Map<String, String> cdkEnvironmentVariables = new HashMap<>(environmentVariables);
        cdkEnvironmentVariables.put("AWS_REGION", awsRegion);

        List<String> commandList = new ArrayList<>();

        String cdkExec = "cdk";
        if (localstackEnabled) {
            cdkExec = "cdklocal";
        }

        commandList.add(cdkExec);
        commandList.add(cdkCommand);
        commandList.add("--verbose");
        commandList.add("--trace");
        commandList.add("--force");

        String cdkApp = "./gradlew startCdkTest";
        if (isDebugEnabled()) {
            cdkApp = cdkApp + " --debug-jvm";
        }
        commandList.add("--app");
        commandList.add(cdkApp);
        commandList.add("--require-approval");
        commandList.add("\"never\"");

        log.debug("Starting cdk process {}", commandList);

        int returnCode = processRunner.run(
                cdkEnvironmentVariables,
                commandList.toArray(new String[0])
        );
        if (returnCode != 0) {
            throw new RuntimeException("Cdk process failed!");
        }
    }

    private boolean isDebugEnabled() {
        String cdkDebugEnabled = System.getenv("CDK_DEBUG_ENABLED");
        return "true".equals(cdkDebugEnabled);
    }
}
