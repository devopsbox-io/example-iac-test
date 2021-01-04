package io.devopsbox.infrastructure.test.common.aws;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.client.builder.AwsClientBuilder;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.net.URI;

public class SdkClientProvider {
    public static final String LOCALSTACK_ENDPOINT = "http://localhost:4566";
    private final String awsRegion;
    private final boolean localstackEnabled;

    private S3Client s3Client;

    public SdkClientProvider(String awsRegion, boolean localstackEnabled) {
        this.awsRegion = awsRegion;
        this.localstackEnabled = localstackEnabled;
        this.s3Client = createS3Client();
    }

    private S3Client createS3Client() {
        S3ClientBuilder clientBuilder = S3Client.builder()
                .region(Region.of(awsRegion));
        configureLocalstackIfEnabled(clientBuilder);

        return clientBuilder.build();
    }

    public S3Client getS3Client() {
        return s3Client;
    }

    private void configureLocalstackIfEnabled(AwsClientBuilder clientBuilder) {
        if (localstackEnabled) {
            clientBuilder.endpointOverride(URI.create(LOCALSTACK_ENDPOINT));
            clientBuilder.credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create("fake_access_key", "fake_secret_key")
            ));
        }
    }
}
