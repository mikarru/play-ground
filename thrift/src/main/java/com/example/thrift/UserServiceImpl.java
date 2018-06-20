package com.example.thrift;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class UserServiceImpl implements TUserService.Iface {
    private final AtomicLong nextUserId = new AtomicLong(0L);
    private final ConcurrentMap<Long, TUser> store = new ConcurrentHashMap<>();

    public UserServiceImpl() {
    }

    public TUser createUserWithAge(String name, int age) throws org.apache.thrift.TException {
        long userId = nextUserId.getAndIncrement();
        TUser user = new TUser(userId, name);
        user.setAge(age);
        store.put(userId, user);
        return user;
    }

    public TUser createUser(String name) throws org.apache.thrift.TException {
        return createUserWithAge(name, -1);
    }

    public TUser findUserById(long userId) throws TUserNotFoundException, org.apache.thrift.TException {
        TUser user = store.get(userId);
        if (store == null) {
            throw new TUserNotFoundException("User not found: userId=" + userId);
        }
        return user;
    }

    public TUser removeUser(long userId) throws TUserNotFoundException, org.apache.thrift.TException {
        TUser user = store.remove(userId);
        if (user == null) {
            throw new TUserNotFoundException("User not found: userId=" + userId);
        }
        return user;
    }

    public boolean existUser(long userId) throws org.apache.thrift.TException {
        return store.containsKey(userId);
    }
}
