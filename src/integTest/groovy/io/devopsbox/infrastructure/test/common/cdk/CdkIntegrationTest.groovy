package io.devopsbox.infrastructure.test.common.cdk

import io.devopsbox.infrastructure.test.ConstructProps
import io.devopsbox.infrastructure.test.common.Localstack
import io.devopsbox.infrastructure.test.common.aws.SdkClientProvider
import software.amazon.awscdk.core.Construct
import spock.lang.Shared
import spock.lang.Specification

abstract class CdkIntegrationTest extends Specification {

    @Shared
    String environment
    @Shared
    File tmpDir
    @Shared
    Cdk cdk
    @Shared
    PropsFileSerializer propsFileSerializer
    @Shared
    SdkClientProvider sdkClients
    @Shared
    Localstack localstack

    def setupSpec() {
        tmpDir = File.createTempDir()
        localstack = new Localstack()
        cdk = new Cdk(awsRegion(), localstack.enabled)
        propsFileSerializer = new PropsFileSerializer()
        environment = UUID.randomUUID().toString()

        sdkClients = new SdkClientProvider(awsRegion(), localstack.enabled)
        if (localstack.enabled) {
            localstack.start()
        }
    }

    void cleanupSpec() {
        tmpDir.deleteDir()
        if (localstack.enabled) {
            localstack.stop()
        }
    }

    def environmentName() {
        return environment
    }

    def awsRegion() {
        return "eu-west-1"
    }

    protected <T extends Construct> void deployCdkConstruct(String cdkStackId, Class<T> constructClass, ConstructProps constructProps) {
        runCdkConstruct(cdkStackId, constructClass, constructProps, "deploy")
    }

    protected <T extends Construct> void destroyCdkConstruct(String cdkStackId, Class<T> constructClass, ConstructProps constructProps) {
        runCdkConstruct(cdkStackId, constructClass, constructProps, "destroy")
    }

    private <T extends Construct> void runCdkConstruct(String cdkStackId, Class<T> constructClass, ConstructProps constructProps, String cdkCommand) {
        def constructPropsFile = File.createTempFile("props-", ".ser", tmpDir)
        propsFileSerializer.serialize(constructPropsFile, constructProps)

        cdk.run([
                (CdkIntegrationTestMain.ENV_VAR_CDK_STACK_ID)            : cdkStackId,
                (CdkIntegrationTestMain.ENV_VAR_CDK_CONSTRUCT_CLASS_NAME): constructClass.name,
                (CdkIntegrationTestMain.ENV_VAR_CDK_CONSTRUCT_PROPS_FILE): constructPropsFile.absolutePath,
        ], cdkCommand)
    }
}
