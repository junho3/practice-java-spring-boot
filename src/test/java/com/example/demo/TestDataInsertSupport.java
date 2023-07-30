package com.example.demo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class TestDataInsertSupport {

    @Autowired
    protected EntityManagerFactory entityManagerFactory;

    @Autowired
    protected JPAQueryFactory jpaQueryFactory;

    private EntityTransaction entityTransaction;

    private EntityManager entityManager;

    protected <T> T save(T entity) {
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();
            entityManager.persist(entity);
            entityManager.flush(); // transaction commit 시 자동으로 flush 발생시키나 명시적으로 선언
            entityTransaction.commit();
            entityManager.clear();
        } catch (Exception e) {
            entityTransaction.rollback();
        }

        return entity;
    }

    protected <T> Iterable<T> saveAll(Iterable<T> entities) {
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();

        for (T entity : entities) {
            try {
                entityTransaction.begin();
                entityManager.persist(entity);
                entityManager.flush(); // transaction commit 시 자동으로 flush 발생시키나 명시적으로 선언
                entityTransaction.commit();
                entityManager.clear();

            } catch (Exception e) {
                entityTransaction.rollback();
            }
        }
        return entities;
    }
}
