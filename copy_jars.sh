#/bin/bash
if [ x"$THERMOSTAT_DEV_HOME" = x ] ; then
    echo "variable THERMOSTAT_DEV_HOME is not set, should point to the thermostat src directory"
    exit -1
fi

PLUGIN_DIR=$THERMOSTAT_DEV_HOME/distribution/target/plugins/kernel-cmdline/

if [ ! -e $PLUGIN_DIR ]; then
    mkdir -p $PLUGIN_DIR
fi
cp -v ./agent/target/thermostat-plug-in-agent-0.0.2-SNAPSHOT.jar $PLUGIN_DIR
cp -v ./client-cli/target/thermostat-plug-in-cli-0.0.2-SNAPSHOT.jar $PLUGIN_DIR
cp -v ./storage-common/target/thermostat-plug-in-storage-common-0.0.2-SNAPSHOT.jar $PLUGIN_DIR
cp -v ./thermostat-plugin.xml $PLUGIN_DIR

