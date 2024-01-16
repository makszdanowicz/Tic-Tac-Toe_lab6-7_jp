package com.pwr.server;

public abstract class Client {
    private String name;
    private String role;
    public Client(String name, String role)
    {
        this.name = name;
        this.role = role;
    }

    @Override
    public String toString() {
        return "name='" + name +
                "| role='" + role + '\n';
    }
}
