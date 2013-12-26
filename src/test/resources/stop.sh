#! /bin/sh

PID_DCP=`ps -ef | grep marlboro.dcp.jar | grep java | grep -v grep | awk '{print $2}'`


if [[ ${PID_DCP} ]]; then
    kill -9 ${PID_DCP}
fi
