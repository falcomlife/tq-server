cp -rf ../../tq-web/dist/* ../storage/src/main/resources/static/web/
cd ../
mvn clean install -Dmaven.test.skip=true
cd -
cp -rf ../storage/target/storage-0.0.1-SNAPSHOT.jar ./microservice.jar
version=`date "+%s"`
docker build -t swr.cn-north-4.myhuaweicloud.com/cotte-internal/tq:$version .
docker push swr.cn-north-4.myhuaweicloud.com/cotte-internal/tq:$version