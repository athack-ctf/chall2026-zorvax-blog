package com.athackctf.chall2025.jestersblog;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;


import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    @PersistenceContext
    private EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;

    public UsersService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean validateUser(String username, String password) {
        try {
            String query = "SELECT u.password FROM Users u WHERE u.username = :username";

            List<String> result = entityManager.createQuery(query, String.class)
                    .setParameter("username", username)
                    .getResultList();

            return !result.isEmpty() && result.get(0).equals(password);
        } catch (Exception e) {
            return false;
        }
    }
}
