package io.devopsbox.infrastructure.test.common

class Localstack {

    String localstackContainerName
    Boolean enabled

    Localstack() {
        enabled = "true" == System.getProperty("localstack.enabled")
    }

    def start() {
        localstackContainerName = "localstack-" + UUID.randomUUID().toString()
        [
                "docker",
                "run",
                "-p", "4566:4566",
                "-e", "LOCALSTACK_SERVICES=s3,sts,cloudformation",
                "--name", localstackContainerName,
                "localstack/localstack"
        ].execute()
    }

    def stop() {
        [
                "docker",
                "rm",
                "-f",
                localstackContainerName,
        ].execute()
    }
}
