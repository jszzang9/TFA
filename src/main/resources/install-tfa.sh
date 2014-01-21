#! /bin/sh
echo
echo "--------------------------------------"
echo " Install TFA"
echo "--------------------------------------"

export JAVA_HOME=/usr/local/jdk1.6.0_45
export PATH=$PATH:/usr/bin:$JAVA_HOME/bin

cd /home/tfa
./stop-tfa.sh
rm -rf ./lib
rm -rf ./*.jar
rm -rf ./*.sh
unzip ./tfa.zip
chmod +x ./*.sh;
./start-tfa.sh

rm -rf ./install-tfa.sh
