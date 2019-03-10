package com.cky.bookstore.web;

import java.sql.Connection;

public class ConnectionContext {


    private static ConnectionContext connectionContext = new ConnectionContext();
    private ConnectionContext(){}

    public static ConnectionContext getInstance() {
        return connectionContext;
    }

    private ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

    public void bind(Connection connection){
        connectionThreadLocal.set(connection);
    }


    public Connection get(){
        return connectionThreadLocal.get();
    }

    public void remove(){
        connectionThreadLocal.remove();
    }
}
