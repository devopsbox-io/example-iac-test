package io.devopsbox.infrastructure.test.s3

import io.devopsbox.infrastructure.test.common.terraform.TerraformIntegrationTest
import io.devopsbox.infrastructure.test.common.terraform.TerraformVariables
import org.apache.commons.codec.digest.DigestUtils
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.ListBucketsRequest
import spock.lang.Shared
import spock.lang.Unroll

class S3TerraformModuleTest extends TerraformIntegrationTest {

    @Shared
    S3Client s3Client

    def setupSpec() {
        s3Client = S3Client.builder()
                .region(Region.of(awsRegion()))
                .build()
    }

    @Unroll
    def "should create s3 bucket #testCase"() {
        given:
        def terraformVariables = new S3TerraformModuleVariables(
                awsRegion: awsRegion(),
                companyName: "acme",
                envName: environmentName(),
                appName: "orders",
                bucketPurpose: bucketPurpose,
        )

        when:
        deployTerraformModule("terraform/s3", terraformVariables)

        then:
        checkBucketExists(expectedBucketName)

        cleanup:
        destroyTerraformModule("terraform/s3", terraformVariables)

        where:
        testCase     | bucketPurpose                                                | expectedBucketName
        "short name" | "pictures"                                                   | "acme-" + environmentName() + "-orders-pictures"
        "long name"  | "pictures12345678901234567890123456789012345678901234567890" | DigestUtils.sha256Hex("acme-" + environmentName() + "-orders-pictures12345678901234567890123456789012345678901234567890").substring(0, 63)
    }

    class S3TerraformModuleVariables extends TerraformVariables {
        String awsRegion
        String companyName
        String envName
        String appName
        String bucketPurpose
    }

    void checkBucketExists(String name) {
        def listBucketsResponse = s3Client.listBuckets(ListBucketsRequest.builder()
                .build())

        assert listBucketsResponse.buckets().any { name.equals(it.name()) }
    }
}
