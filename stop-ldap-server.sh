#!/bin/sh

if podman ps | sed 1d | awk -F' ' '{print $1}' >/dev/null 2>&1; then
  PROC_ID=$(podman ps | sed 1d | awk -F' ' '{print $1}')
  podman kill "$PROC_ID"
fi
