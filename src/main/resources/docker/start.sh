#!/bin/bash

if [ "${VERBOSE}" = "true" ]; then
    echo "[II] Starting service using HOST=${HOST}"
    echo "[II] Starting service using PORT=${PORT}"
    echo "[II] Starting service using TYPE=${TYPE}"
    echo "[II] Starting service using TIMEOUT=${TIMEOUT}"
    echo "[II] Starting service using URI=${URI}"
    echo "[II] Starting service using LOGDETAILS=${LOGDETAILS}"
fi

PARAMS="--host ${HOST} --type ${TYPE}"
if [ ! -z "${PORT}" ]; then
    PARAMS="${PARAMS} --port ${PORT}"
fi
if [ ! -z "${TIMEOUT}" ]; then
    PARAMS="${PARAMS} --timeout ${TIMEOUT}"
fi
if [ ! -z "${URI}" ]; then
    PARAMS="${PARAMS} --uri ${URI}"
fi
if [ "${LOGDETAILS}" = "true" ]; then
    PARAMS="${PARAMS} --logDetails"
fi

if [ "${VERBOSE}" = "true" ]; then
    echo "using params: ${PARAMS}"
fi

java -jar /tmp/${project.artifactId}.jar ${PARAMS}
