#/bin/bash

PLUGIN_DIR=$THERMOSTAT_DEV_HOME/distribution/target/image/plugins

TARGET_DIR="kernel-cmdline"

if [ -e $PLUGIN_DIR/$TARGET_DIR ]; then
    rm -rf $PLUGIN_DIR/$TARGET_DIR
fi

DISTRO_ZIP=$(pwd)/distribution/target/thermostat-kernel-cmdline-distribution*.zip
pushd $PLUGIN_DIR
unzip $DISTRO_ZIP
popd
