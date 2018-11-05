# Spring cloud ConfigServer UI

版本信息：
 Spring 
 
Spring Cloud Finchley.RELEASE

Spring boot 2.0.3.RELEASE

spring-cloud-config 2.0.0.RELEASE

已经包含Srping cloud config组件，可以直接使用

开发目的：

1.能直观的管理配置文件
2.可以在线编辑配置文件信息
3.产品经理要求的
4.我就是产品经理
5.就希望能做个有用的东西

功能描述：

1.支持Git作为配置文件的仓库(暂不支持SVN)
2.支持perproties和yaml文件格式
3.支持配置文件修改、删除、新增属性，并时实上传到配置服务器中
4.支持ConfigServer在Eureka集群模式下运行，选取其中一台作为配置管理服务器
5.因为修改后的文件要实时上传到Git,如果是外网https://github.com或类似情景会有卡顿...
6.其它根据需要再开发...

待开发：

1.增加分布锁
2.增加直接加key-value,不提交
3.增加直接批量提交
4.拆分，相关功能剥离出config