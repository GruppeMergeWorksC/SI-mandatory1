package com.soapapi.repository;

import com.soapapi.exception.ValidationException;
import com.soapapi.library.Author;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AuthorRepository{

    private final JdbcTemplate jdbcTemplate;

    public AuthorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long createAuthor(Author author) {
        String sql = "INSERT INTO tauthor (cName, cSurname) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, author.getName());
            ps.setString(2, author.getSurname());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("No generated key was returned.");
        }
        return key.longValue();
    }

    public Author getAuthorById(Long id) {
        String sql = "SELECT * FROM tauthor WHERE nAuthorID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Author author = new Author();
            author.setId(rs.getLong("nAuthorID"));
            author.setName(rs.getString("cName"));
            author.setSurname(rs.getString("cSurname"));
            return author;
        });
    }

    public List<Author> getAllAuthors() {
        String sql = "SELECT * FROM tauthor";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Author author = new Author();
            author.setId(rs.getLong("nAuthorID"));
            author.setName(rs.getString("cName"));
            author.setSurname(rs.getString("cSurname"));
            return author;
        });
    }

    public boolean updateAuthor(Long id, String name, String surname) {
        List<String> sets = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (name != null) {
            if(name.isBlank()){return false;}
            sets.add("cName = ?");
            params.add(name);
        }
        if (surname != null) {
            if(surname.isBlank()){return false;}
            sets.add("cSurname = ?");
            params.add(surname);
        }

        if (sets.isEmpty()) {
            return false;
        }

        String sql = "UPDATE tauthor SET " + String.join(", ", sets) + " WHERE nAuthorId = ?";
        params.add(id);

        return jdbcTemplate.update(sql, params.toArray()) > 0;
    }

    public boolean deleteAuthor(Long id) {
        String sql = "DELETE FROM tauthor WHERE nAuthorID = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    public boolean existsById(Long authorId) {
        String sql = "SELECT COUNT(*) FROM tauthor WHERE nAuthorID = ?";
        Integer count = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                authorId
        );
        return count != null && count > 0;
    }
}
