#!/bin/bash

set -e

mount_dir=$1
ecs_file_system=$2

if [ -z "$ecs_file_system" ]; then
    exit 0
fi

if mount | grep -q $ecs_file_system; then
    echo "already mounted ... exiting"
    exit 0
fi

ec2_az=$(curl -s http://169.254.169.254/latest/meta-data/placement/availability-zone)
ec2_region=${ec2_az:0:${#ec2_az}-1}
efs_host="${ecs_file_system}.efs.${ec2_region}.amazonaws.com"

if which yum >/dev/null 2>&1; then
    yum install -y nfs-utils bind-utils
fi

if which apt-get >/dev/null 2>&1; then
    apt-get install -y nfs-common dnsutils
fi

mkdir -p $mount_dir

if ! grep -q $ecs_file_system /etc/fstab; then
  echo "${efs_host}:/ $mount_dir nfs4 defaults" >>/etc/fstab
fi

retry=0
until nslookup $efs_host >/dev/null 2>&1; do
    if [ "$retry" -ge "60" ]; then
        echo "gave up waiting for EFS"
        exit 1
    fi
    echo "waiting for EFS ..."
    retry=$((retry+1))
    sleep 3
done

retry=0
until mount -a; do
    if [ "$retry" -ge "10" ]; then
        echo "failed to mount EFS ... exiting"
        exit 1
    fi
    echo "failed to mount EFS ... retrying"
    retry=$((retry+1))
    sleep 3
done

chmod 777 $mount_dir
touch '/tmp/ecs_restart_docker'