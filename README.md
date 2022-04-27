# SpringBoot
> 使用SpringBoot 集成Spring-data-jpa,Druid连接池,thymeleaf模板实现的一个简单项目，包含后台管理



## Docker镜像
### Pull image
```bash
docker pull skywa1ker/mall:latest
```
### Run
```bash
docker run -p 8081:8081 --name mall -v /data/mall/config:/data/mall/config -v /data/mall/log:/data/mall/log --restart=always -d skywa1ker/mall:latest
```
