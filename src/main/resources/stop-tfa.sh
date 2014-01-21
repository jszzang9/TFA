#! /bin/sh
echo
echo "--------------------------------------"
echo " Stopping TFA"
echo "--------------------------------------"

PID=`ps -ef | grep tfa.jar | grep java | grep -v grep | awk '{print $2}'`

if [[ -z ${PID} ]]; then
    echo "no tfa exist."
else
    echo "tfa killed."
    kill -9 ${PID}
fi

echo "--------------------------------------"
echo
