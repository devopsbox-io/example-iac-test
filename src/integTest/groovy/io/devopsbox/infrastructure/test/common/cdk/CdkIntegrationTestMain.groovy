package io.devopsbox.infrastructure.test.common.cdk

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import software.amazon.awscdk.core.App

class CdkIntegrationTestMain {
    private static final Logger log = LoggerFactory.getLogger(CdkIntegrationTestMain.class);

    public static final String ENV_VAR_CDK_STACK_ID = "ENV_VAR_CDK_STACK_ID"
    public static final String ENV_VAR_CDK_CONSTRUCT_CLASS_NAME = "CDK_CONSTRUCT_CLASS_NAME"
    public static final String ENV_VAR_CDK_CONSTRUCT_PROPS_FILE = "CDK_CONSTRUCT_PROPS_FILE"

    static void main(String[] args) {
        def stackId = System.getenv(ENV_VAR_CDK_STACK_ID)
        def constructClass = System.getenv(ENV_VAR_CDK_CONSTRUCT_CLASS_NAME)
        def propsPath = System.getenv(ENV_VAR_CDK_CONSTRUCT_PROPS_FILE)

        log.debug("Starting test cdk application with construct class: {} and props path: {}", constructClass, propsPath)
        log.debug("Environment: {}", System.getenv())

        App app = new App()

        new CdkIntegrationTestStack(app, stackId, constructClass, propsPath)

        app.synth();
    }
}
