# Prerequisites

- GO (tested with 1.15.3)
- Terraform (tested with 0.14.3)
- AWS CDK (tested with 1.71.0)
- Java JDK (tested with openjdk 11.0.9)
- NodeJS (required by AWS CDK, tested with v12.18.3)
- Docker (tested with 18.09.5)
- cdklocal (https://github.com/localstack/aws-cdk-local, tested with 1.65.2)

Should work with different versions of some prerequisites.

# What's inside?

- `terraform/s3` sample Terraform S3 module
- `test` Terraform module tests written in Terratest
- `src/integTest/groovy/io/devopsbox/infrastructure/test/s3/S3TerraformModuleTest.groovy` Terraform module tests written in Spock
- `src/main/java/io/devopsbox/infrastructure/test/s3` sample AWS CDK S3 Construct
- `src/integTest/groovy/io/devopsbox/infrastructure/test/s3/S3CdkConstructTest.groovy` AWS CDK Construct test written in Spock
- `src/test/groovy/io/devopsbox/infrastructure/test/s3/S3ConstructPropsTest.groovy` Unit tests of S3 naming convention, which is used by AWS CDK Construct

# Running

## Terratest

```shell script
cd test
go test
```

## Terraform Spock tests

```shell script
./gradlew integTest --tests *S3TerraformModuleTest
```

## Terraform Spock tests with Localstack

```shell script
./gradlew integTest --tests *S3TerraformModuleTest -Dlocalstack.enabled=true
```

## AWS CDK Spock tests

```shell script
./gradlew integTest --tests *S3CdkConstructTest
```

## AWS CDK Spock tests with Localstack

```shell script
./gradlew integTest --tests *S3CdkConstructTest -Dlocalstack.enabled=true
```

## S3 naming convention tests used by AWS CDK

```shell script
./gradlew test
```
