
resource "aws_instance" "jenkins_master" {
  ami                         = data.aws_ami.ubuntu_2204.id
  instance_type               = var.instance_type_master
  subnet_id                   = aws_subnet.public.id
  key_name                    = var.key_name
  vpc_security_group_ids      = [aws_security_group.master_sg.id]
  associate_public_ip_address = true

  root_block_device {
    volume_size = 20
    volume_type = "gp3"
  }

  tags = {
    Name = "${var.project_name}-master"
    Role = "jenkins-master"
  }
}

resource "aws_instance" "jenkins_slave" {
  ami                    = data.aws_ami.ubuntu_2204.id
  instance_type          = var.instance_type_slave
  subnet_id              = aws_subnet.private.id
  key_name               = var.key_name
  vpc_security_group_ids = [aws_security_group.slave_sg.id]

  root_block_device {
    volume_size = 15
    volume_type = "gp3"
  }

  tags = {
    Name = "${var.project_name}-slave"
    Role = "jenkins-slave"
  }
}
