terraform {
  required_version = ">= 1.0"

  backend "s3" {
    bucket  = "abeer-terraform-state"
    key     = "jenkins/terraform.tfstate"
    region  = "eu-central-1"
    encrypt = true
  }
}
