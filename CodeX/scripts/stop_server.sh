#!/bin/bash

set -e

service tomcat9 stop

rm -rf /var/lib/tomcat9/webapps/myapp*