package io.devopsbox.infrastructure.test.s3

import io.devopsbox.infrastructure.test.common.aws.S3
import io.devopsbox.infrastructure.test.common.cdk.CdkIntegrationTest
import spock.lang.Shared

class S3CdkConstructTest extends CdkIntegrationTest {

    @Shared
    S3 s3

    def setupSpec() {
        s3 = new S3(sdkClients)
    }

    def "should create s3 bucket"() {
        given:
        def stackId = "S3BucketConstructTest" + environmentName()
        def constructProps = new S3ConstructProps(
                "acme",
                environmentName(),
                "orders",
                "pictures"
        )

        when:
        deployCdkConstruct(stackId, S3Construct, constructProps)

        then:
        s3.checkBucketExists("acme-" + environmentName() + "-orders-pictures")

        cleanup:
        destroyCdkConstruct(stackId, S3Construct, constructProps)
    }
}
