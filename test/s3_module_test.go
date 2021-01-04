package test

import (
	"crypto/sha256"
	"encoding/hex"
	"github.com/gruntwork-io/terratest/modules/aws"
	"github.com/gruntwork-io/terratest/modules/files"
	"github.com/gruntwork-io/terratest/modules/random"
	"github.com/gruntwork-io/terratest/modules/terraform"
	"os"
	"strings"
	"testing"
)

func TestS3BucketCreated(t *testing.T) {
	t.Parallel()

	envName := strings.ToLower(random.UniqueId())
	awsRegion := "eu-west-1"

	tests := map[string]struct {
		terraformVariables map[string]interface{}
		expectedBucketName string
	}{
		"short name": {
			terraformVariables: map[string]interface{}{
				"aws_region":     awsRegion,
				"company_name":   "acme",
				"env_name":       envName,
				"app_name":       "orders",
				"bucket_purpose": "pictures",
			},
			expectedBucketName: "acme-" + envName + "-orders-pictures",
		},
		"long name": {
			terraformVariables: map[string]interface{}{
				"aws_region":     awsRegion,
				"company_name":   "acme",
				"env_name":       envName,
				"app_name":       "orders",
				"bucket_purpose": "pictures12345678901234567890123456789012345678901234567890",
			},
			expectedBucketName: sha256String("acme-" + envName + "-orders-pictures12345678901234567890123456789012345678901234567890")[:63],
		},
	}

	for name, testCase := range tests {
		// capture range variables
		name := name
		testCase := testCase
		t.Run(name, func(t *testing.T) {
			t.Parallel()

			terraformModuleDir, err := files.CopyTerraformFolderToTemp("../terraform/s3", "terratest-")
			if err != nil {
				t.Fatalf("Error while creating temp dir %v", err)
			}
			defer os.RemoveAll(terraformModuleDir)

			terraformOptions := terraform.WithDefaultRetryableErrors(t, &terraform.Options{
				TerraformDir: terraformModuleDir,
				Vars:         testCase.terraformVariables,
			})

			defer terraform.Destroy(t, terraformOptions)

			terraform.InitAndApply(t, terraformOptions)

			aws.AssertS3BucketExists(t, awsRegion, testCase.expectedBucketName)
		})
	}
}

func sha256String(str string) string {
	sha256Bytes := sha256.Sum256([]byte(str))
	return hex.EncodeToString(sha256Bytes[:])
}
