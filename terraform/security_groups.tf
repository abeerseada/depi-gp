# SG for Jenkins master (public)
resource "aws_security_group" "master_sg" {
  name        = "${var.project_name}-master-sg"
  description = "Allow SSH and web (if needed) to Jenkins master"
  vpc_id      = aws_vpc.main.id

  ingress {
    description = "SSH from allowed CIDR"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [var.allowed_ssh_cidr]
  }

  ingress {
    description = "Jenkins UI "
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
    ingress {
    description = "Prometheus UI"
    from_port   = 9090
    to_port     = 9090
    protocol    = "tcp"
    cidr_blocks = [var.allowed_ssh_cidr]
  }
    ingress {
    description = "Alerts"
    from_port   = 9093
    to_port     = 9093
    protocol    = "tcp"
    cidr_blocks = [var.allowed_ssh_cidr]
  }
  ingress {
    description = "Grafana UI"
    from_port   = 3000
    to_port     = 3000
    protocol    = "tcp"
    cidr_blocks = [var.allowed_ssh_cidr]
  }


  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = { Name = "${var.project_name}-master-sg" }
}

# SG for Jenkins slave (private)
resource "aws_security_group" "slave_sg" {
  name        = "${var.project_name}-slave-sg"
  description = "Allow SSH from master (for ansible) and outbound access"
  vpc_id      = aws_vpc.main.id

  ingress {
    description     = "SSH from master"
    from_port       = 22
    to_port         = 22
    protocol        = "tcp"
    security_groups = [aws_security_group.master_sg.id]
  }
    ingress {
    description     = "Node exporter from master"
    from_port       = 9100
    to_port         = 9100
    protocol        = "tcp"
    security_groups = [aws_security_group.master_sg.id]
  }


  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = { Name = "${var.project_name}-slave-sg" }
}
