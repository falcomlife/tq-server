# This is dockerfile for micro service cluster
# Version: 1.0
# Author: sora

FROM swr.cn-north-4.myhuaweicloud.com/cotte-internal/centos7-tools-jdk8-git:0.0.3
ARG CONTAINERNAME
WORKDIR /opt
COPY microservice.jar /opt
RUN mkdir -p /opt/storage/images
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
ENV LANG=en_US.UTF-8 \
    LC_ALL=en_US.UTF-8 \
    LC_CTYPE=en_US.UTF-8
