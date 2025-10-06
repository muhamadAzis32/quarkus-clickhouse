package org.iconpln.master_unitup.repository;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iconpln.master_unitup.entity.MasterUnitUp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository untuk akses data master_unitup di ClickHouse
 *
 * Lombok Annotations:
 * @Slf4j - Auto-generate logger
 * @RequiredArgsConstructor - Constructor injection untuk final fields
 * @ApplicationScoped - CDI bean scope
 */
@Slf4j
@RequiredArgsConstructor
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
     * Mengambil semua data yang tidak dihapus (deleted = 0)
     *
     * @return List of MasterUnitup
     * @throws SQLException jika terjadi error database
     */
    public List<MasterUnitUp> findAll() throws SQLException {
        String sql = "SELECT * FROM master_unitup FINAL where __deleted = 0";
        log.debug("Executing query: {}", sql);

        List<MasterUnitUp> results = new ArrayList<>();

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
     * Helper method untuk mapping ResultSet ke Entity
     * Menggunakan Lombok Builder pattern
     *
     * @param rs ResultSet dari query
     * @return MasterUnitup entity
     * @throws SQLException jika terjadi error saat mapping
     */
    private MasterUnitUp mapResultSetToEntity(ResultSet rs) throws SQLException {
        return MasterUnitUp.builder()
                .unitup(rs.getString("unitup"))
                .alamat(rs.getString("alamat"))
                .kota(rs.getString("kota"))
                .manager(rs.getString("manager"))
                .nama(rs.getString("nama"))
                .namaSingkat(rs.getString("nama_singkat"))
                .satuan(rs.getString("satuan"))
                .telepon(rs.getString("telepon"))
                .unitap(rs.getString("unitap"))
                .deleted(rs.getInt("__deleted"))
                .build();
    }

}
