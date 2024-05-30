#!/bin/bash -xe
apt-get update
apt-get install -y ruby wget
cd /home/ubuntu
wget https://aws-codedeploy-us-west-2.s3.amazonaws.com/latest/install
chmod +x ./install
./install auto
service codedeploy-agent start