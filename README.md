# Intro

There may be different requirements to a distributed application, but most of the time the main goal is to be able to scale the application horizontally effectively when the need comes. 

That means that specific format in which data is transmitted over the network or the way client-server interactions are organized is not that important or relevant to the business logic. In practice, however, distributed architecture comes with a significant overhead:

##### Under- and Over-utilization of resources

Usually, you need to know in advance the parts of your application that you want to make distributed. It's not trivial to distribute a piece of business logic on a whim. There is a certain amount of guessing to it which leads to issues such as Under- and Over-utilization of resources.

##### Duplication

Public interface for distributed logic is defined in multiple places: in the actual implementation and  in the configuration that describes the server and the client.
Code generation tools such as Apache Thrift help with this to a degree.

---

XRMI solves the cross-cutting concern of distributed computing with Aspect Oriented Programming. It allows to focus on the business logic, making it easy to extract any piece of logic from the application, deploy and scale it independently. Without changes to application's code.

Let's say you have a Java application for online store and at some point WarehouseService class becomes CPU demanding, due to new features and increased load. With XRMI you can transparently catch all calls to methods of this service:

```java
    InetSocketAddress remoteAddress = new InetSocketAddress("www.examle.com", 12345);
    bindInterceptor(
            Matchers.subclassesOf(WarehouseService.class),
            Matchers.any(),
            new RemoteCallInterceptor(remoteAddress)
    );
```

`RemoteCallInterceptor` is the XRMI component that redirects the call. Then you can start WarehouseService class as a server, using:

```java
    XrmiServer.start(injector, 12345);
```

Where `injector` is the dependency injection container that can return an instance of WarehouseService.

There is an example application available [here](https://github.com/lcf/xrmi-demo).

# Possible applications

Similarly, AOP can be used for other aspects of infrastructure related to distributed architecture:

- timeout configuration 
- circuit breaker and fault tolerance
- automatic service discovery and load balancing
- logging and metrics collection 
- security
 
# Limitations

1. As with Java RMI, all classes that are transfered over network using XRMI must implement `java.io.Serializable`.

2. This proof of concept relies on Google Guice as Dependency Injection container and for AOP Matchers, although it may be abstracted away in the future.
