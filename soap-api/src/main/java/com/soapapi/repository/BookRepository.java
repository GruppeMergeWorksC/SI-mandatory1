package com.soapapi.repository;

import com.soapapi.library.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository {

    JdbcTemplate jdbcTemplate;

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long createBook(Book book) {
        String sql = "INSERT INTO tbook (cTitle, nAuthorId, nPublishingYear, nPublishingCompanyID) VALUES (?, ?, ?, ?)";

        boolean authorExists = jdbcTemplate.queryForObject(
                "SELECT * FROM tauthor WHERE nAuthorID = ?",
                new Object[]{book.getAuthorId()},
                (rs, rowNum) -> rs.next()
        );

        boolean pubCoExists = jdbcTemplate.queryForObject(
                "SELECT * FROM tpublishingcompany WHERE nPublishingCompanyID = ?",
                new Object[]{book.getPublishingCompanyId()},
                (rs, rowNum) -> rs.next()
        );

        if(!authorExists) {
            throw new IllegalArgumentException("Author with ID " + book.getAuthorId() + " does not exist.");
        }
        if(!pubCoExists) {
            throw new IllegalArgumentException("Publishing company with ID " + book.getPublishingCompanyId());
        }

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, book.getTitle());
            ps.setLong(2, book.getAuthorId());
            ps.setInt(3, book.getPublishingYear());
            ps.setLong(4, book.getPublishingCompanyId());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("No generated key was returned.");
        }

        return key.longValue();
    }

    public Book getBookById(Long id) {
        String sql = "SELECT * FROM tbook WHERE nBookID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Book book = new Book();
            book.setId(rs.getLong("id"));
            book.setTitle(rs.getString("cTitle"));
            book.setAuthorId(rs.getLong("nAuthorId"));
            book.setPublishingYear(rs.getInt("nPublishingYear"));
            book.setPublishingCompanyId(rs.getLong("nPublishingCompanyId"));
            return book;
        });
    }

    public boolean updateBook(Long bookId, String title, Long authorId, Integer publishingYear, Long publishingCompanyId) {
        List<String> sets = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if(title != null) {
            sets.add("nTitle = ?");
            params.add(title);
        }
        if(authorId != null) {
            sets.add("nAuthorID = ?");
            params.add(authorId);
        }
        if(publishingYear != null) {
            sets.add("nPublishingYear = ?");
            params.add(publishingYear);
        }
        if(publishingCompanyId != null) {
            sets.add("nPublishingCompanyID = ?");
            params.add(publishingCompanyId);
        }

        String sql = "UPDATE tbook SET " + String.join(", ", sets) + " WHERE nBookID = ?";
        params.add(bookId);

        return jdbcTemplate.update(sql, params.toArray()) > 0;
    }

    public boolean deleteBook(Long bookId) {
        String sql = "DELETE FROM tbook WHERE nBookID = ?";
        return jdbcTemplate.update(sql, bookId) > 0;
    }
}
