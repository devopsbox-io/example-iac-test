package io.devopsbox.infrastructure.test.common.aws;

import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

public class S3 {

    private final SdkClientProvider sdkClientProvider;

    public S3(SdkClientProvider sdkClientProvider) {
        this.sdkClientProvider = sdkClientProvider;
    }

    public boolean checkBucketExists(String name) {
        ListBucketsResponse listBucketsResponse = sdkClientProvider.getS3Client()
                .listBuckets(ListBucketsRequest.builder()
                        .build());

        return listBucketsResponse.buckets().stream().anyMatch(
                bucket -> name.equals(bucket.name())
        );
    }

}
