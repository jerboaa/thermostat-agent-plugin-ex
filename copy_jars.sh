#/bin/bash

THERMOSTAT_VERSION=0.15
PLUGIN_DIR=../thermostat-${THERMOSTAT_VERSION}/distribution/target/image/plugins/kernel-cmdline/

if [ ! -e $PLUGIN_DIR ]; then
    mkdir -p $PLUGIN_DIR
fi
cp -v ./agent/target/thermostat-plug-in-agent-0.0.2-SNAPSHOT.jar $PLUGIN_DIR
cp -v ./client-cli/target/thermostat-plug-in-cli-0.0.2-SNAPSHOT.jar $PLUGIN_DIR
cp -v ./storage-common/target/thermostat-plug-in-storage-common-0.0.2-SNAPSHOT.jar $PLUGIN_DIR
cp -v ./thermostat-plugin.xml $PLUGIN_DIR

