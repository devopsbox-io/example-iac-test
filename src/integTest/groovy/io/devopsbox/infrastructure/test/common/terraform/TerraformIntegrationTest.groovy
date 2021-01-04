package io.devopsbox.infrastructure.test.common.terraform

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

    def setupSpec() {
        terraform = new Terraform()
        terraformVariablesMarshaller = new TerraformVariablesMarshaller()
        environment = UUID.randomUUID().toString()
    }

    def setup() {
        tmpDir = File.createTempDir()
    }

    void cleanup() {
        tmpDir.deleteDir()
    }

    def environmentName() {
        return environment
    }

    def awsRegion() {
        return "eu-west-1"
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
}
