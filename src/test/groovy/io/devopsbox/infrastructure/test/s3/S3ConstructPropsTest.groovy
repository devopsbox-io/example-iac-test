package io.devopsbox.infrastructure.test.s3

import org.apache.commons.codec.digest.DigestUtils
import spock.lang.Specification
import spock.lang.Unroll

class S3ConstructPropsTest extends Specification {

    @Unroll
    def "should return s3 bucket #testCase"() {
        given:
        def props = new S3ConstructProps(
                "acme",
                "dev",
                "orders",
                bucketPurpose,
        )

        when:
        def bucketName = props.bucketName

        then:
        bucketName == expectedBucketName

        where:
        testCase     | bucketPurpose                                                | expectedBucketName
        "short name" | "pictures"                                                   | "acme-dev-orders-pictures"
        "long name"  | "pictures12345678901234567890123456789012345678901234567890" | DigestUtils.sha256Hex("acme-dev-orders-pictures12345678901234567890123456789012345678901234567890").substring(0, 63)
    }
}
