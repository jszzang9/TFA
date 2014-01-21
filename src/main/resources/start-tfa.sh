#! /bin/sh

echo
echo "--------------------------------------"
echo " Starting TFA"
echo "--------------------------------------"

$* java -jar tfa.jar > /dev/null 2>&1 &

echo "tfa started."

echo "--------------------------------------"
echo

exit 0

