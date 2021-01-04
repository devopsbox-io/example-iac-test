package io.devopsbox.infrastructure.test.s3

import io.devopsbox.infrastructure.test.common.aws.S3
import io.devopsbox.infrastructure.test.common.terraform.TerraformIntegrationTest
import io.devopsbox.infrastructure.test.common.terraform.TerraformVariables
import org.apache.commons.codec.digest.DigestUtils
import spock.lang.Shared
import spock.lang.Unroll

class S3TerraformModuleTest extends TerraformIntegrationTest {

    @Shared
    S3 s3

    def setupSpec() {
        s3 = new S3(sdkClients)
    }

    @Unroll
    def "should create s3 bucket #testCase"() {
        given:
        def terraformVariables = new S3TerraformModuleVariables(
                useLocalstack: localstack.enabled,
                awsRegion: awsRegion(),
                companyName: "acme",
                envName: environmentName(),
                appName: "orders",
                bucketPurpose: bucketPurpose,
        )

        when:
        deployTerraformModule("terraform/s3", terraformVariables)

        then:
        s3.checkBucketExists(expectedBucketName)

        cleanup:
        destroyTerraformModule("terraform/s3", terraformVariables)

        where:
        testCase     | bucketPurpose                                                | expectedBucketName
        "short name" | "pictures"                                                   | "acme-" + environmentName() + "-orders-pictures"
        "long name"  | "pictures12345678901234567890123456789012345678901234567890" | DigestUtils.sha256Hex("acme-" + environmentName() + "-orders-pictures12345678901234567890123456789012345678901234567890").substring(0, 63)
    }

    class S3TerraformModuleVariables extends TerraformVariables {
        boolean useLocalstack
        String awsRegion
        String companyName
        String envName
        String appName
        String bucketPurpose
    }
}
