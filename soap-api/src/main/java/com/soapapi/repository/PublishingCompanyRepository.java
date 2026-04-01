package com.soapapi.repository;

import com.soapapi.library.PublishingCompany;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class PublishingCompanyRepository {

    private final JdbcTemplate jdbcTemplate;

    public PublishingCompanyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long createPublishingCompany(PublishingCompany publishingCompany) {
        String sql = "INSERT INTO tpublishingcompany (cName) VALUES (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, publishingCompany.getName());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if(key == null) {
            throw new IllegalStateException("No generated key was returned.");
        }
        return key.longValue();
    }

    public PublishingCompany getPublishingCompanyById(Long id) {
        String sql = "SELECT * FROM tpublishingcompany WHERE nPublishingCompanyID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {)
            PublishingCompany publishingCompany = new PublishingCompany();
            publishingCompany.setId(rs.getLong("id"));
            publishingCompany.setName(rs.getString("cName"));
            return publishingCompany;
        });
    }

    public List<PublishingCompany> getAllPublishingCompanies() {
        String sql = "SELECT * FROM tpublishingcompany";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            PublishingCompany publishingCompany = new PublishingCompany();
            publishingCompany.setId(rs.getLong("id"));
            publishingCompany.setName(rs.getString("cName"));
            return publishingCompany;
        });
    }

    public boolean updatePublishingCompany(Long id, String name) {
        String sql = "UPDATE tpublishingcompany SET cName = ? WHERE nPublishingCompanyID = ?";
        int rowsAffected = jdbcTemplate.update(sql, name, id);
        return rowsAffected > 0;
    }

    public boolean deletePublishingCompany(Long id) {
        String sql = "DELETE FROM tpublishingcompany WHERE nPublishingCompanyID = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

}
