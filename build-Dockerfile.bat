@echo off
::等待控制台输入
set/p jar=please input your Jar name, xm-ms-xxxx.jar for example :

echo FROM  jre8:171 > Dockerfile
echo MAINTAINER  redking >> Dockerfile
echo USER root >> Dockerfile
echo RUN ["mkdir","/ms"] >> Dockerfile
echo ADD %jar% /ms >> Dockerfile
echo RUN ["chmod","777","/ms/%jar%"] >> Dockerfile
echo ENV arg1 "" >> Dockerfile
echo ENV LANG C.UTF-8 >> Dockerfile

set/p port=please input your Jar port, 8080 for example :

echo EXPOSE %port% >> Dockerfile
::set L=%jar%.length
echo ENTRYPOINT java -server -Xms512m -Xmx512m -Xss512k -XX:MaxMetaspaceSize=128m -XX:+UseG1GC -XX:+UseStringDeduplication -XX:+PrintGCDetails -jar /ms/%jar% $arg1 >> Dockerfile
echo #docker build -t %jar:-1.0.0.jar=%:1.0.0 . >> Dockerfile
echo #docker build -t %jar:-0.0.1.jar=%:1.0.0 . >> Dockerfile
echo #docker run -e arg1="--server.port=%port%" -p %port%:%port% -v /docker-dev/%jar:-1.0.0.jar=%/:/ms --net=host -itd %jar:-1.0.0.jar=%:1.0.0 >> Dockerfile
echo #docker run -e arg1="--server.port=%port%" -p %port%:%port% -v /docker-dev/%jar:-0.0.1.jar=%/:/ms --net=host -itd %jar:-0.0.1.jar=%:1.0.0 >> Dockerfile
echo #docker start container-id >> Dockerfile
echo ....... build Dockerfile succeed !!!!

pause