# Spring Fabric Gateway

Provided Spring MVC services and spring boot starter based on [fabric-gateway-java](https://github.com/hyperledger/fabric-gateway-java)

### Dependencies

1. Build your fabric network with Hyperledger Fabric 1.4.0 +.

2. Optional, install the default common [Chaincode](https://github.com/ecsoya/spring-fabric-gateway/raw/master/spring-fabric-gateway/src/chaincode/common/chaincode.go).

### How to use

##### Spring Boot Configure:

Firstly, add the following dependency to your project's `pom.xml` file:

```
<dependency>
	<groupId>io.github.ecsoya</groupId>
	<artifactId>fabric-gateway-spring-boot-starter</artifactId>
	<version>1.0.0</version>
</dependency>
```

Secondly, configure fabric network in `application.yml`

```
# Fabric Network Configure      
spring:         
   fabric:
      chaincode: your chaincodename
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

### References

1. [Hyperledger Fabric Gateway SDK for Java](https://github.com/hyperledger/fabric-gateway-java)
2. [Java SDK for Hyperledger Fabric](https://github.com/hyperledger/fabric-sdk-java)