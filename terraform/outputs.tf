output "vpc_id" {
  value = aws_vpc.main.id
}

output "public_subnet_id" {
  value = aws_subnet.public.id
}

output "private_subnet_id" {
  value = aws_subnet.private.id
}

output "jenkins_master_public_ip" {
  value       = aws_instance.jenkins_master.public_ip
  description = "Use this IP to SSH (use your .pem private key)"
}

output "jenkins_master_private_ip" {
  value = aws_instance.jenkins_master.private_ip
}

output "jenkins_slave_private_ip" {
  value = aws_instance.jenkins_slave.private_ip
}
