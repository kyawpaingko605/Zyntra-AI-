#!/usr/bin/env sh

# ====== Gradle Wrapper Script for Unix ======

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Use the maximum available, or set custom
DEFAULT_JVM_OPTS="-Xmx64m -Xms64m"

warn () {
    echo "$*"
}

die () {
    echo
    echo "$*"
    echo
    exit 1
}

# Determine the Java Command
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/bin/java" ] ; then
        JAVACMD="$JAVA_HOME/bin/java"
    else
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME"
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH."
fi

# Locate the gradle-wrapper.jar
CLK_PATH="app/gradle/wrapper/gradle-wrapper.jar"
# For simple layout, look in root
if [ ! -f "$CLK_PATH" ]; then
    CLK_PATH="gradle/wrapper/gradle-wrapper.jar"
fi

# Run Gradle
exec "$JAVACMD" $DEFAULT_JVM_OPTS -classpath "$CLK_PATH" org.gradle.wrapper.GradleWrapperMain "$@"
