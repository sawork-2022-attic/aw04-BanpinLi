# WebPOS

由于在本应用中大部分操作都集中在内存里，同时并没有涉及大量的计算操作，所以在进行压力测试的时候，会人为的添加一些耗时操作来模拟请求需要计算的情况，以此来看到更加明显的效果。

1、垂直扩展

首先创建一个docker的image，并使用docker命令``` docker run -d --name aw04_0.5c_1 --cpus=0.5 -p 8081:8080 aw04-horizontal```，为其分配0.5个cpu，并且将内部端口号映射到本机上的8081端口，使得应用在容器中进行运行。

使用压力测试，会发现直接在本机上的平均响应时间会小于运行在容器中的0.5cpu的响应时间。

2、水平扩展

按照前面分配docker运行image的命令，再使用三个命令运行三个应用，分配的cpu数量为0.5个，占用的端口号为8082、8083、8084。然后使用haproxy来完成负载均衡，具体的配置如下图：

```
defaults
	mode tcp
frontend aw04
	bind *:8080
	default_backend servers
backend servers
	balance roundrobin
	server server1 localhost:8081
	server server2 localhost:8082
	server server3 localhost:8083
	server server4 localhost:8084
```

在命令行中执行命令```./haproxy -f ./haproxy.cfg -d```然后我们在浏览器中直接访问```http://localhost:8080```，此时会将请求分发到不同的服务器上实现负载均衡。

最终的使用gatling来进行压力测试会发现，平均响应时间会比前面的单个服务器响应时间会短很多。

3、缓存失效和session共享

当我们使用多个服务器来做负载均衡的时候，假设有a和b服务器，访问a的时候，会检查是否有缓存，但是这个请求可能在上次访问的是b，所以缓存就放在了b服务器上面，这个时候就会造成一个cache missing的情况；同时访问a服务器的时候会携带有一个session用来保存这个会话的涉及到的数据信息，具体来说就是保存当前浏览器中涉及到的Cart数据，此时session放在了a服务器中，那么下一次访问可能会被负载均衡器访问到b服务器，那么session的数据就找不到，相当于丢失了cart中的数据，也就是session共享出了问题。

解决这个我们可以使用redis来作为系统的缓存，只需要在pom.xml导入相关依赖，同时在配置文件中配置redis作为系统的缓存，那么就可以避免上面提到的两个问题；除此之外，为了能够提高性能，所以不能够使用单个的redis来作为缓存，在这里就使用到了redis cluster来作为缓存，此时只需要在配置文件中简单配置集群的nodes即可，由于我的环境是windows配置redis不太方便，于是在linux中搭建了一个redis集群，又因为redis集群本身就可以实现数据的共享，所以也可以解决前面提到的问题。
