package org.iconpln.master_unitup.repository;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.iconpln.master_unitup.entity.MasterUnitUp;
import org.iconpln.master_unitup.mapper.MasterUnitUpMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ApplicationScoped
public class MasterUnitUpRepository {

    /**
     * Inject datasource ClickHouse yang sudah dikonfigurasi
     * @DataSource("clickhouse") = mengambil config dari application.properties
     * dengan prefix: quarkus.datasource.clickhouse.*
     */
    @Inject
    @DataSource("clickhouse")
    AgroalDataSource clickhouseDataSource;

    /**
     * Mengambil data dengan pagination
     */
    public List<MasterUnitUp> findAll(int page, int size) throws SQLException{
        int offset = (page - 1) * size;

        String sql = "SELECT * FROM master_unitup FINAL WHERE __deleted = 0 ORDER BY unitup LIMIT ? OFFSET ?";

        log.debug("Executing paginated query: page={}, size={}, offset={}", page, size, offset);

        List<MasterUnitUp> results = new ArrayList<>();

        try (Connection conn = clickhouseDataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, size);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(MasterUnitUpMapper.fromResultSet(rs));
                }
            }

            log.debug("Found {} records for page {}", results.size(), page);
        }

        return results;
    }

    /**
     * Count total records yang aktif
     *
     */
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM master_unitup WHERE __deleted = 0";
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
