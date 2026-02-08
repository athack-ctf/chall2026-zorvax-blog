package com.athackctf.chall2025.jestersblog;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;


import java.util.List;
import java.util.Map;
@Service
public class BlogPostsService {

    @PersistenceContext
    private EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;

    public BlogPostsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public BlogPosts saveBlogPost(BlogPosts blogPost) {
        if (blogPost.getId() == 0) {
            entityManager.persist(blogPost);
        } else {
            entityManager.merge(blogPost);
        }
        return blogPost;
    }

    public Map<String, Object> getBlogPostById(String id) {
        if (entityManager == null) {
            throw new IllegalStateException("EntityManager is null");
        }
        String sql = "SELECT * FROM blogposts WHERE id = "+id + " LIMIT 1";

        System.out.println("Executing SQL: " + sql);

        try {
            return jdbcTemplate.queryForMap(sql);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<BlogPosts> getAllBlogPosts() {
        if (entityManager == null) {
            throw new IllegalStateException("EntityManager is null");
        }
        String jpql = "SELECT b FROM BlogPosts b";
        TypedQuery<BlogPosts> query = entityManager.createQuery(jpql, BlogPosts.class);
        return query.getResultList();
    }

    @Transactional
    public void deleteBlogPostById(int id) {
        BlogPosts blogPost = entityManager.find(BlogPosts.class, id);
        if (blogPost != null) {
            entityManager.remove(blogPost);
        }
    }
    public List<BlogPosts> searchById(String id) {
        return entityManager.createQuery("SELECT b FROM BlogPosts b WHERE b.id = :id", BlogPosts.class)
                .setParameter("id", Integer.parseInt(id))
                .getResultList();
    }
}
