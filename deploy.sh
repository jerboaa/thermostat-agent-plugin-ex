#/bin/bash
if [ x"$THERMOSTAT_PLUGIN_HOME" = x ] ; then
    echo "variable THERMOSTAT_PLUGIN_HOME is not set. It should point to Thermostat's plugin directory."
    exit 1
fi


TARGET_DIR="kernel-cmdline"

if [ -e $THERMOSTAT_PLUGIN_HOME/$TARGET_DIR ]; then
    rm -rf $THERMOSTAT_PLUGIN_HOME/$TARGET_DIR
fi

DISTRO_ZIP=$(pwd)/distribution/target/thermostat-kernel-cmdline-distribution*.zip
pushd $THERMOSTAT_PLUGIN_HOME
unzip $DISTRO_ZIP
popd
# Copy the common jar to the webapp
cp $THERMOSTAT_PLUGIN_HOME/$TARGET_DIR/thermostat-kernel-cmdline-storage-common-*.jar $THERMOSTAT_PLUGIN_HOME/../webapp/WEB-INF/lib
