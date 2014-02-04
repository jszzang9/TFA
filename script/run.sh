pid=`ps  -ef|grep TFALauncher|grep java|awk '{print $2}'`;if [ $pid ];then kill -9 $pid;fi
java -cp tfa.jar com.expull.tfa.TFALauncher >> log &