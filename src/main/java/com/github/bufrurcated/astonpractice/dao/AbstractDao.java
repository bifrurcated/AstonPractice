package com.github.bufrurcated.astonpractice.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractDao {
    private final SessionFactory sessionFactory;
    protected Session sessionRemove;

    public AbstractDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session openSession() {
        return sessionFactory.openSession();
    }

    public void inTransaction(Consumer<Session> action) {
        sessionFactory.inTransaction(action);
    }

    public <R> R fromTransaction(Function<Session, R> action) {
        return sessionFactory.fromTransaction(action);
    }
}
