#!/bin/sh

# Computes the absolute path of eXo
cd `dirname "$0"`

# Sets some variables
LOG_OPTS="-Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.SimpleLog"
SECURITY_OPTS="-Djava.security.auth.login.config=../conf/jaas.conf"
EXO_OPTS="-Dexo.product.developing=true"
EXO_CONFIG_OPTS="-Xshare:auto -Xms128m -Xmx512m -Dorg.exoplatform.container.configuration.debug"

JPDA_TRANSPORT=dt_socket
JPDA_ADDRESS=8000

REMOTE_DEBUG="-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n"

# For profiling
#LD_LIBRARY_PATH="/cygdrive/d/tools/YourKit/bin/win32/"
#PATH="$PATH:$LD_LIBRARY_PATH"
#export LD_LIBRARY_PATH
#YOURKIT_PROFILE_OPTION="-agentlib:yjpagent  -Djava.awt.headless=true"

JAVA_OPTS="$YOURKIT_PROFILE_OPTION $JAVA_OPTS $LOG_OPTS $SECURITY_OPTS $EXO_OPTS $EXO_CONFIG_OPTS"
export JAVA_OPTS
TOMCAT_BIN=`pwd`;

CHATSERVER_BIN="$TOMCAT_BIN/../../exo-chatserver/bin"
chmod -R +x $CHATSERVER_BIN
export PATH="$CHATSERVER_BIN:$PATH"

# Launches openfire
echo "========================================="
echo "Starting Openfire daemon...";
if [ `echo $OS | grep -i "window"` ]; then
	JAVA_HOME="`cygpath --windows $JAVA_HOME`"
  openfired &
else
  openfire start
fi

echo "========================================="
echo "Starting Tomcat server"
# Launches the server
cd "$TOMCAT_BIN";
exec "$PRGDIR"./catalina.sh "$@"