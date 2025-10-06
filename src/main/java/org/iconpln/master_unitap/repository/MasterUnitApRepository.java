package org.iconpln.master_unitap.repository;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.iconpln.master_unitap.entity.MasterUnitAp;
import org.iconpln.master_unitap.mapper.MasterUnitApMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ApplicationScoped
public class MasterUnitApRepository {

    @Inject
    @DataSource("clickhouse")
    AgroalDataSource clickhouseDataSource;

    /**
     * Mengambil data unitap dengan pagination
     */
    public List<MasterUnitAp> findAll(int page, int size) throws SQLException {
        int offset = (page - 1) * size;

        String sql = "SELECT * FROM master_unitap FINAL WHERE __deleted = 0 ORDER BY unitap LIMIT ? OFFSET ?";

        log.debug("Executing paginated query: page={}, size={}, offset={}", page, size, offset);

        List<MasterUnitAp> results = new ArrayList<>();

        try (Connection conn = clickhouseDataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, size);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(MasterUnitApMapper.fromResultSet(rs));
                }
            }

            log.debug("Found {} records for page {}", results.size(), page);
        }

        return results;
    }

    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM master_unitap WHERE __deleted = 0";
        log.debug("Counting total records");

        try (Connection conn = clickhouseDataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                long total = rs.getLong("total");
                log.debug("Total records: {}", total);
                return total;
            }
        }

        return 0;
    }
}
