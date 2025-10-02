package org.iconpln.example.repository;

import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iconpln.example.entity.MasterUnitupi;

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
     * DataSource akan di-inject otomatis oleh Quarkus
     * Final = required field untuk @RequiredArgsConstructor
     */
    private final AgroalDataSource dataSource;

    /**
     * Mengambil semua data yang tidak dihapus (deleted = 0)
     *
     * @return List of MasterUnitupi
     * @throws SQLException jika terjadi error database
     */
    public List<MasterUnitupi> findAll() throws SQLException {
        String sql = "SELECT * FROM master_unitupi ";
        log.debug("Executing query: {}", sql);

        List<MasterUnitupi> results = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
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
        String sql = "SELECT * FROM master_unitupi WHERE unitupi = ? AND deleted = 0";
        log.debug("Finding by unitupi: {}", unitupi);

        try (Connection conn = dataSource.getConnection();
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
     * Mencari berdasarkan kota
     *
     * @param kota Nama kota
     * @return List of MasterUnitupi
     * @throws SQLException jika terjadi error database
     */
    public List<MasterUnitupi> findByKota(String kota) throws SQLException {
        String sql = "SELECT * FROM master_unitupi WHERE kota = ? AND deleted = 0 ORDER BY unitupi";
        log.debug("Finding by kota: {}", kota);

        List<MasterUnitupi> results = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, kota);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapResultSetToEntity(rs));
                }
            }
        }

        log.debug("Found {} units in kota: {}", results.size(), kota);
        return results;
    }

    /**
     * Mencari berdasarkan nama (menggunakan LIKE)
     *
     * @param keyword Keyword pencarian
     * @return List of MasterUnitupi
     * @throws SQLException jika terjadi error database
     */
    public List<MasterUnitupi> searchByNama(String keyword) throws SQLException {
        String sql = "SELECT * FROM master_unitupi WHERE nama LIKE ? AND deleted = 0 ORDER BY unitupi";
        log.debug("Searching by nama with keyword: {}", keyword);

        List<MasterUnitupi> results = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
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
     * Insert data baru ke ClickHouse
     *
     * @param entity Entity yang akan disimpan
     * @throws SQLException jika terjadi error database
     */
    public void insert(MasterUnitupi entity) throws SQLException {
        String sql = """
            INSERT INTO master_unitupi 
            (unitupi, alamat, kota, manager, nama, nama_singkat, satuan, telepon, __ts_ms, deleted) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        log.debug("Inserting new unit: {}", entity.getUnitupi());

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, entity.getUnitupi());
            stmt.setString(2, entity.getAlamat());
            stmt.setString(3, entity.getKota());
            stmt.setString(4, entity.getManager());
            stmt.setString(5, entity.getNama());
            stmt.setString(6, entity.getNamaSingkat());
            stmt.setString(7, entity.getSatuan());
            stmt.setString(8, entity.getTelepon());
            stmt.setLong(9, entity.getTsMs() != null ? entity.getTsMs() : System.currentTimeMillis());
            stmt.setInt(10, entity.getDeleted() != null ? entity.getDeleted() : 0);

            int rowsAffected = stmt.executeUpdate();
            log.debug("Insert successful. Rows affected: {}", rowsAffected);
        }
    }

    /**
     * Update data (di ClickHouse dengan ReplacingMergeTree = insert row baru)
     *
     * @param entity Entity yang akan diupdate
     * @throws SQLException jika terjadi error database
     */
    public void update(MasterUnitupi entity) throws SQLException {
        // Di ClickHouse dengan ReplacingMergeTree, update = insert dengan __ts_ms baru
        log.debug("Updating unit: {}", entity.getUnitupi());
        entity.setTsMs(System.currentTimeMillis());
        insert(entity);
    }

    /**
     * Soft delete (set deleted = 1 dengan timestamp baru)
     *
     * @param unitupi Kode unit yang akan dihapus
     * @throws SQLException jika terjadi error database
     */
    public void softDelete(String unitupi) throws SQLException {
        String sql = """
            INSERT INTO master_unitupi 
            (unitupi, alamat, kota, manager, nama, nama_singkat, satuan, telepon, __ts_ms, deleted) 
            SELECT unitupi, alamat, kota, manager, nama, nama_singkat, satuan, telepon, ?, 1 
            FROM master_unitupi 
            WHERE unitupi = ? AND deleted = 0 
            LIMIT 1
            """;

        log.debug("Soft deleting unit: {}", unitupi);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, System.currentTimeMillis());
            stmt.setString(2, unitupi);

            int rowsAffected = stmt.executeUpdate();
            log.debug("Soft delete successful. Rows affected: {}", rowsAffected);
        }
    }

    /**
     * Count total records yang aktif
     *
     * @return Total records
     * @throws SQLException jika terjadi error database
     */
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM master_unitupi WHERE deleted = 0";
        log.debug("Counting total records");

        try (Connection conn = dataSource.getConnection();
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
                .tsMs(rs.getLong("__ts_ms"))
                .deleted(rs.getInt("__deleted"))
                .build();
    }
}
