package com.soapapi.repository;

import com.soapapi.library.PublishingCompany;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
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
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            PublishingCompany publishingCompany = new PublishingCompany();
            publishingCompany.setId(rs.getLong("nPublishingCompanyID"));
            publishingCompany.setName(rs.getString("cName"));
            return publishingCompany;
        });
    }

    public List<PublishingCompany> getAllPublishingCompanies() {
        String sql = "SELECT * FROM tpublishingcompany";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            PublishingCompany publishingCompany = new PublishingCompany();
            publishingCompany.setId(rs.getLong("nPublishingCompanyID"));
            publishingCompany.setName(rs.getString("cName"));
            return publishingCompany;
        });
    }

    public boolean updatePublishingCompany(Long id, String name) {
        List<String> sets = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if(name != null) {
            sets.add("cName = ?");
            params.add(name);
        }

        if(sets.isEmpty()) {
            return false;
        }

        String sql = "UPDATE tpublishingcompany SET " + String.join(", ", sets) + " WHERE nPublishingCompanyID = ?";
        params.add(id);

        return jdbcTemplate.update(sql, params.toArray()) > 0;
    }

    public boolean deletePublishingCompany(Long id) {
        String sql = "DELETE FROM tpublishingcompany WHERE nPublishingCompanyID = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    public boolean existsById(Long pubCoId) {
        String sql = "SELECT COUNT(*) FROM tpublishingcompany WHERE nPublishingCompanyID = ?";
        Integer count = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                pubCoId
        );
        return count != null && count > 0;
    }

}
