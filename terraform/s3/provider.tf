terraform {
  required_providers {
    aws = {
      source = "hashicorp/aws"
    }
  }
}

provider "aws" {
  region = var.aws_region

  access_key = var.use_localstack ? "fake_access_key" : null
  s3_force_path_style = var.use_localstack
  secret_key = var.use_localstack ? "fake_secret_key" : null
  skip_credentials_validation = var.use_localstack
  skip_metadata_api_check = var.use_localstack
  skip_requesting_account_id = var.use_localstack

  dynamic "endpoints" {
    for_each = var.use_localstack ? [1] : []
    content {
      s3 = "http://localhost:4566"
    }
  }
}
