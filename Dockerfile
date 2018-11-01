FROM  jre8_152:0001 
MAINTAINER  redking 
USER root 
RUN ["mkdir","/xm-ms"] 
ADD xm-ms-configserver-1.0.0.jar /xm-ms 
RUN ["chmod","777","/xm-ms/xm-ms-configserver-1.0.0.jar"] 
ENV arg1 "" 
ENV LANG C.UTF-8 
EXPOSE 8765 
ENTRYPOINT java -server -Xms512m -Xmx512m -Xss512k -XX:MaxMetaspaceSize=128m -XX:+UseG1GC -XX:+UseStringDeduplication -XX:+PrintGCDetails -jar /xm-ms/xm-ms-configserver-1.0.0.jar $arg1 
#docker build -t xm-ms-configserver:1.0.0 . 
#docker build -t xm-ms-configserver-1.0.0.jar:1.0.0 . 
#docker run -e arg1="--server.port=8765" -p 8765:8765 -v /docker-dev/xm-ms-configserver/:/xm-ms --net=host -itd xm-ms-configserver:1.0.0 
#docker run -e arg1="--server.port=8765" -p 8765:8765 -v /docker-dev/xm-ms-configserver-1.0.0.jar/:/xm-ms --net=host -itd xm-ms-configserver-1.0.0.jar:1.0.0 
#docker start container-id 
