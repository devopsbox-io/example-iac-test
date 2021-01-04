package io.devopsbox.infrastructure.test.common.terraform

import io.devopsbox.infrastructure.test.common.AwsSdkClientProvider
import org.apache.commons.io.FileUtils
import spock.lang.Shared
import spock.lang.Specification

abstract class TerraformIntegrationTest extends Specification {

    File tmpDir

    @Shared
    String environment
    @Shared
    Terraform terraform
    @Shared
    TerraformVariablesMarshaller terraformVariablesMarshaller
    @Shared
    String localstackContainerName
    @Shared
    AwsSdkClientProvider sdkClients

    def setupSpec() {
        terraform = new Terraform()
        terraformVariablesMarshaller = new TerraformVariablesMarshaller()
        environment = UUID.randomUUID().toString()

        def localstackEnabled = localstackEnabled()
        sdkClients = new AwsSdkClientProvider(awsRegion(), localstackEnabled)
        if (localstackEnabled) {
            startLocalstack()
        }
    }

    def setup() {
        tmpDir = File.createTempDir()
    }

    void cleanup() {
        tmpDir.deleteDir()
    }

    void cleanupSpec() {
        if (localstackEnabled()) {
            stopLocalstack()
        }
    }

    def environmentName() {
        return environment
    }

    def awsRegion() {
        return "eu-west-1"
    }

    def localstackEnabled() {
        return "true" == System.getProperty("localstack.enabled")
    }

    protected void deployTerraformModule(String moduleDirPath, TerraformVariables terraformVariables) {
        FileUtils.copyDirectory(new File(moduleDirPath), new File(moduleTmpDir()))
        runTerraformModule(moduleTmpDir(), terraformVariables, "apply")
    }

    protected void destroyTerraformModule(String moduleDirPath, TerraformVariables terraformVariables) {
        FileUtils.copyDirectory(new File(moduleDirPath), new File(moduleTmpDir()))
        runTerraformModule(moduleTmpDir(), terraformVariables, "destroy")
    }

    private void runTerraformModule(String moduleDirPath, TerraformVariables terraformVariables, String terraformCommand) {
        def moduleVariablesFile = File.createTempFile("variables-", ".tfvars.json", tmpDir)
        terraformVariablesMarshaller.marshall(moduleVariablesFile, terraformVariables)

        terraform.run(moduleDirPath,
                moduleVariablesFile.absolutePath,
                terraformCommand)
    }

    private moduleTmpDir() {
        return tmpDir.absolutePath + "/module"
    }

    private startLocalstack() {
        localstackContainerName = "localstack-" + UUID.randomUUID().toString()
        [
                "docker",
                "run",
                "-p", "4566:4566",
                "-e", "LOCALSTACK_SERVICES=s3",
                "--name", localstackContainerName,
                "localstack/localstack"
        ].execute()
    }

    private stopLocalstack() {
        [
                "docker",
                "rm",
                "-f",
                localstackContainerName,
        ].execute()
    }
}
