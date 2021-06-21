#!/bin/bash

echo "WebDriver Grid Hub on 4445"
echo ""
 
echo "*********************************************"
echo "*"
echo "* WebDriver Grid Node"
echo "* It requires that a WebDriver JSON Hub is already running, usually on port 5555."
echo "* You can run more than one of these if each has its own JSON config file."
echo "*"
echo "*********************************************"
echo ""


 
jarfile=selenium-server-standalone-3.141.59.jar
chromedriverbin=../src/main/resources/  

if [ -z "${JAVA_HOME+xxx}" ]; then
  echo JAVA_HOME is not set at all;
  exit 1  
fi


export PATH="$JAVA_HOME/bin:$PATH"


nohup $JAVA_HOME/bin/java -jar $jarfile -role node -nodeConfig ./node.json -Dwebdriver.chrome.driver=$chromedriverbin </dev/null &>/dev/null &

echo 'Done'
exit