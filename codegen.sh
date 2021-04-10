java -jar ../wutsi-codegen/target/wutsi-codegen-0.0.27.jar server \
    -in https://wutsi-openapi.s3.amazonaws.com/site_api.yaml \
    -out . \
    -name site \
    -package com.wutsi.site \
    -jdk 11 \
    -github_user wutsi \
    -github_project site-server \
    -heroku wutsi-site \
    -service_cache \
    -service_logger \
    -service_database \
    -service_mqueue

if [ $? -eq 0 ]
then
    echo Code Cleanup...
    mvn antrun:run@ktlint-format
    mvn antrun:run@ktlint-format

else
    echo "FAILED"
    exit -1
fi
