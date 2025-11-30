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

  # Optional HTTP for Jenkins UI (port 8080). Uncomment if you want web open.
  ingress {
    description = "Jenkins UI (optional)"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
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

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = { Name = "${var.project_name}-slave-sg" }
}
