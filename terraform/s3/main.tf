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

locals {
  requested_bucket_name = "${var.company_name}-${var.env_name}-${var.app_name}-${var.bucket_purpose}"
  bucket_name = length(local.requested_bucket_name) > 63 ? substr(sha256(local.requested_bucket_name), 0, 63) : local.requested_bucket_name
}

resource "aws_s3_bucket" "bucket" {
  bucket = local.bucket_name
}
