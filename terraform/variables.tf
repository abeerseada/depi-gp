variable "region" {
  description = "AWS region"
  type        = string
  default     = "eu-central-1"
}

variable "vpc_cidr" {
  description = "VPC CIDR"
  default     = "10.0.0.0/16"
}

variable "public_subnet_cidr" {
  description = "Public subnet CIDR"
  default     = "10.0.1.0/24"
}

variable "private_subnet_cidr" {
  description = "Private subnet CIDR"
  default     = "10.0.10.0/24"
}

variable "availability_zone_public" {
  description = "AZ for public subnet"
  default     = "eu-central-1a"
}

variable "availability_zone_private" {
  description = "AZ for private subnet"
  default     = "eu-central-1b"
}

variable "instance_type_master" {
  description = "Instance type for Jenkins master"
  default     = "t3.medium"
}

variable "instance_type_slave" {
  description = "Instance type for Jenkins slave"
  default     = "t3.small"
}

variable "key_name" {
  description = "Existing EC2 key pair name"
  type        = string
}

variable "allowed_ssh_cidr" {
  description = "CIDR allowed to SSH to Jenkins master"
  default     = "156.197.103.221/32"
}

variable "project_name" {
  description = "Tag prefix"
  default     = "abeer-jenkins"
}
