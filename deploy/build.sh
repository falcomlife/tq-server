cp -rf ../../tq-web/dist/* ../storage/src/main/resources/static/
cd ../
mvn clean install -Dmaven.test.skip=true
cd -
cp -rf ../storage/target/storage-0.0.1-SNAPSHOT.jar ./microservice.jar
docker build -t swr.cn-north-4.myhuaweicloud.com/cotte-internal/tq:0.0.1 .
docker push swr.cn-north-4.myhuaweicloud.com/cotte-internal/tq:0.0.1