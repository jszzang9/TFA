#! /bin/sh

PID_TFA=`ps -ef | grep tfa.jar | grep java | grep -v grep | awk '{print $2}'`


if [[ ${PID_TFA} ]]; then
    kill -9 ${PID_TFA}
fi
