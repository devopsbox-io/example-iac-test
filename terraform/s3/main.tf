terraform {
  required_providers {
    aws = {
      source = "hashicorp/aws"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

locals {
  requested_bucket_name = "${var.company_name}-${var.env_name}-${var.app_name}-${var.bucket_purpose}"
  bucket_name = length(local.requested_bucket_name) > 63 ? substr(sha256(local.requested_bucket_name), 0, 63) : local.requested_bucket_name
}

resource "aws_s3_bucket" "bucket" {
  bucket = local.bucket_name
}
