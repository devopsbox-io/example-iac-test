package io.devopsbox.infrastructure.test.s3;

import io.devopsbox.infrastructure.test.ConstructProps;
import org.apache.commons.codec.digest.DigestUtils;

public class S3ConstructProps extends ConstructProps {
    public static final int BUCKET_NAME_MAX_LENGTH = 63;

    private final String bucketPurpose;

    public S3ConstructProps(String companyName, String envName, String appName, String bucketPurpose) {
        super(companyName, envName, appName);
        this.bucketPurpose = bucketPurpose;
    }

    public String getBucketPurpose() {
        return bucketPurpose;
    }

    public String getBucketName() {
        String bucketName = getCompanyName() + "-" + getEnvName() + "-" + getAppName() + "-" + getBucketPurpose();
        if (bucketName.length() > BUCKET_NAME_MAX_LENGTH) {
            bucketName = DigestUtils.sha256Hex(bucketName).substring(0, BUCKET_NAME_MAX_LENGTH);
        }
        return bucketName;
    }
}
