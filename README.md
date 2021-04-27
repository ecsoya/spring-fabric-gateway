[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.ecsoya/spring-fabric-gateway/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/io.github.ecsoya/spring-fabric-gateway)



# Spring Fabric Gateway

Provided Spring MVC services and spring boot starters based on [fabric-gateway-java](https://github.com/hyperledger/fabric-gateway-java), more info can be found from [https://ecsoya.github.io/fabric/](https://ecsoya.github.io/fabric/)(中文).

### Dependencies

1. Build your fabric network with Hyperledger Fabric v2.3.0.

2. Optional, install the default common [Chaincode](https://github.com/ecsoya/spring-fabric-gateway/raw/master/spring-fabric-gateway/src/chaincode/common/chaincode.go).

### Springboot Starters

1. `fabric-gateway-spring-boot-starter`: A spring MVC boot starter.
2. `fabric-explorer-spring-boot-starter`: A simple fabric explorer starter.

### How to use

##### Spring Boot Configure:

Firstly, add the following dependency to your project's `pom.xml` file:

```
<dependency>
	<groupId>io.github.ecsoya</groupId>
	<artifactId>fabric-gateway-spring-boot-starter</artifactId>
	<version>2.0.0-SNAPSHOT</version>
</dependency>
```

or


```
<dependency>
	<groupId>io.github.ecsoya</groupId>
	<artifactId>fabric-explorer-spring-boot-starter</artifactId>
	<version>2.0.0-SNAPSHOT</version>
</dependency>
```

Secondly, configure fabric network in `application.yml`

```
# Fabric Network Configure      
spring:         
   fabric:
      chaincode: 
         identify: your chaincode id
         name: Common chaincode
         version: 1.0
      channel: your channel name
      organizations:
      - org1
      - org2
      name: your fabric network name
      gateway:
         wallet:
            identify: admin
      network:
         file: network/connection.yml //your fabric network config file.
         name: fabric network name
# Fabric explorer
      explorer: 
         title: Fabric Explorer
         logo: img/logo.png
         copyright: Ecsoya (jin.liu@soyatec.com)
         hyperledger-explorer-url: http://www.hyperleder.org

```

Finally, enjoy to use the following spring components:

1. `FabricContext`: wrapper class for fabric gateway.
2. `IFabricInfoService`: query fabric infos such as blocks, transaction and so on.
3. `IFabricService`: CRUD FabricObject if you install the default common [Chaincode](https://github.com/ecsoya/spring-fabric-gateway/raw/master/spring-fabric-gateway/src/chaincode/common/chaincode.go).

```
@Autowired
private FabricContext fabricContext;
```

### Fabric Network Config File

The `Fabric Network Config File` is the core configuration file of fabric network, both `FabricContext` and `Gateway` are built with this kind of file. It holds details of network and channel configurations typically loaded from an external config file. 

Please look at the `org.hyperledger.fabric.sdk.NetworkConfig` to get more details.

You can also look at my project [Fabric Network Builder](https://github.com/ecsoya/fabric-network-builder) to learn how to create a fabric network config file.

### Default common chaincode

The default common chaincode provided useful functions such as `create`, `get`, `update`, `query`. It used a CompositeKey with `id` and `type` to identify different objects, it also support pagination query.

The chaincode source can be found from [here](https://github.com/ecsoya/spring-fabric-gateway/raw/master/spring-fabric-gateway/src/chaincode/common/chaincode.go).

### Fabric Explorer Starter

This will help you to build a simple fabric explorer with few codes.

![Fabric Explorer](https://github.com/ecsoya/spring-fabric-gateway/raw/master/images/explorer-1.png)

### References

1. [Hyperledger Fabric Gateway SDK for Java](https://github.com/hyperledger/fabric-gateway-java)
2. [Java SDK for Hyperledger Fabric](https://github.com/hyperledger/fabric-sdk-java)