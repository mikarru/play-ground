package com.example.thrift;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

public class UserServiceServer {
    private TUserService.Iface userService;
    public UserServiceServer(TUserService.Iface userService) {
        this.userService = userService;
    }
    public void serve() throws Exception  {
        TUserService.Processor processor = new TUserService.Processor(userService);
        TServerTransport serverTransport = new TServerSocket(9090);
        TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));
        server.serve();
    }

    public static void main(String[] args) throws Exception {
        new UserServiceServer(new UserServiceImpl()).serve();
    }
}
