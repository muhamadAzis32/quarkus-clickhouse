package org.iconpln.mon_janji_bayar.repository;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.iconpln.master_unitup.entity.MasterUnitUp;
import org.iconpln.master_unitup.mapper.MasterUnitUpMapper;
import org.iconpln.mon_janji_bayar.entity.MonJanjiBayar;
import org.iconpln.mon_janji_bayar.mapper.MonJanjiBayarMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ApplicationScoped
public class MonJanjiBayarRepository {

    @Inject
    @DataSource("clickhouse")
    AgroalDataSource clickhouseDataSource;


    /**
     * Mengambil data mon janji bayar dengan pagination
     */
    public List<MonJanjiBayar> findAll(int page, int size) throws SQLException {
        int offset = (page - 1) * size;

        String sql = "SELECT * FROM mon_janji_bayar FINAL WHERE __deleted = 0 ORDER BY id LIMIT ? OFFSET ?";

        log.debug("Executing paginated query: page={}, size={}, offset={}", page, size, offset);

        List<MonJanjiBayar> results = new ArrayList<>();

        try (Connection conn = clickhouseDataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, size);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(MonJanjiBayarMapper .fromResultSet(rs));
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
        String sql = "SELECT COUNT(*) as total FROM mon_janji_bayar WHERE __deleted = 0";
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
