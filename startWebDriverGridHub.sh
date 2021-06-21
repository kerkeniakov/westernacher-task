#!/bin/sh

echo "WebDriver Grid Hub on 4445"
echo ""
 
echo "*********************************************"
echo "*"
echo "* WebDriver grid Hub instance."
echo "*"
echo "*  http://localhost:4445/grid/console"
echo "*"
echo "*********************************************"
echo ""

 
jarfile=selenium-server-standalone-3.141.59.jar
 
if [ -z "${JAVA_HOME+xxx}" ]; then
  echo JAVA_HOME is not set at all;
  exit 1  
fi

export PATH="$JAVA_HOME/bin:$PATH"

nohup $JAVA_HOME/bin/java -jar $jarfile -role hub -hubConfig ./hub.json </dev/null &>/dev/null &

echo 'Done'
exit
