package com.example.thrift;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;

public class UserServiceClient {
    public static void main(String[] args) throws Exception {
        TTransport transport = new TSocket("localhost", 9090);
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        TUserService.Client client = new TUserService.Client(protocol);

        perform(client);

        transport.close();
    }

    public static void perform(TUserService.Client client) throws Exception {
        TUser tom = client.createUser("tom");
        System.out.println(tom);
        boolean exists = client.existUser(tom.getId());
        if (exists) {
            System.out.println(tom.getName() + " exists");
        } else {
            System.out.println(tom.getName() + " does not exist");
        }
        client.removeUser(tom.getId());
        exists = client.existUser(tom.getId());
        if (exists) {
            System.out.println(tom.getName() + " exists");
        } else {
            System.out.println(tom.getName() + " does not exist");
        }
    }
}
