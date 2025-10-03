package org.iconpln.master_unit_upi.repository;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iconpln.master_unit_upi.entity.MasterUnitupi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository untuk akses data master_unitupi di ClickHouse
 *
 * Lombok Annotations:
 * @Slf4j - Auto-generate logger
 * @RequiredArgsConstructor - Constructor injection untuk final fields
 * @ApplicationScoped - CDI bean scope
 */
@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class UnitUpiRepository {

    /**
     * Inject datasource ClickHouse yang sudah dikonfigurasi
     * @DataSource("clickhouse") = mengambil config dari application.properties
     * dengan prefix: quarkus.datasource.clickhouse.*
     */
    @Inject
    @DataSource("clickhouse")
    AgroalDataSource clickhouseDataSource;

    /**
     * Mengambil semua data yang tidak dihapus (deleted = 0)
     *
     * @return List of MasterUnitupi
     * @throws SQLException jika terjadi error database
     */
    public List<MasterUnitupi> findAll() throws SQLException {
        String sql = "SELECT * FROM master_unitupi where __deleted = 0";
        log.debug("Executing query: {}", sql);

        List<MasterUnitupi> results = new ArrayList<>();

        try (Connection conn = clickhouseDataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(mapResultSetToEntity(rs));
            }

            log.debug("Found {} records", results.size());
        }

        return results;
    }

    /**
     * Mengambil data berdasarkan unitupi (primary key)
     *
     * @param unitupi Kode unit UPI
     * @return Optional of MasterUnitupi
     * @throws SQLException jika terjadi error database
     */
    public Optional<MasterUnitupi> findByUnitupi(String unitupi) throws SQLException {
        String sql = "SELECT * FROM master_unitupi FINAL WHERE unitupi = ? AND __deleted = 0";
        log.debug("Finding by unitupi: {}", unitupi);

        try (Connection conn = clickhouseDataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, unitupi);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    MasterUnitupi entity = mapResultSetToEntity(rs);
                    log.debug("Found unit: {}", entity.getNama());
                    return Optional.of(entity);
                }
            }
        }

        log.debug("Unit not found: {}", unitupi);
        return Optional.empty();
    }


    /**
     * Mencari berdasarkan nama (menggunakan LIKE)
     *
     * @param keyword Keyword pencarian
     * @return List of MasterUnitupi
     * @throws SQLException jika terjadi error database
     */
    public List<MasterUnitupi> searchByNama(String keyword) throws SQLException {
        String sql = "SELECT * FROM master_unitupi WHERE nama LIKE ? AND __deleted = 0 ORDER BY unitupi";
        log.debug("Searching by nama with keyword: {}", keyword);

        List<MasterUnitupi> results = new ArrayList<>();

        try (Connection conn = clickhouseDataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapResultSetToEntity(rs));
                }
            }
        }

        log.debug("Found {} units matching keyword: {}", results.size(), keyword);
        return results;
    }

    /**
     * Helper method untuk mapping ResultSet ke Entity
     * Menggunakan Lombok Builder pattern
     *
     * @param rs ResultSet dari query
     * @return MasterUnitupi entity
     * @throws SQLException jika terjadi error saat mapping
     */
    private MasterUnitupi mapResultSetToEntity(ResultSet rs) throws SQLException {
        return MasterUnitupi.builder()
                .unitupi(rs.getString("unitupi"))
                .alamat(rs.getString("alamat"))
                .kota(rs.getString("kota"))
                .manager(rs.getString("manager"))
                .nama(rs.getString("nama"))
                .namaSingkat(rs.getString("nama_singkat"))
                .satuan(rs.getString("satuan"))
                .telepon(rs.getString("telepon"))
//                .tsMs(rs.getLong("__ts_ms"))
                .deleted(rs.getInt("__deleted"))
                .build();
    }
}
