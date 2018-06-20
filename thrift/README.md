# thrift

## build

### 1. generate java classes from thrift definitions

```
$ thrift --gen java -out src/main/java UserService.thrift
```

### 2. build java

```
$ mvn clean package
```
