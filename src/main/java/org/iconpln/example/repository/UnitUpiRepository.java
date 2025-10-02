package org.iconpln.example.repository;

import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import org.iconpln.example.entity.MasterUnitupi;

@ApplicationScoped
public class UnitUpiRepository {

    @Inject
    AgroalDataSource dataSource;

    /**
     * Mengambil semua data yang tidak dihapus (deleted = 0)
     */
    public List<MasterUnitupi> findAll() throws SQLException {
        String sql = "SELECT * FROM master_unitupi";

        List<MasterUnitupi> results = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(mapResultSetToEntity(rs));
            }
        }

        return results;
    }


    /**
     * Helper method untuk mapping ResultSet ke Entity
     */
    private MasterUnitupi mapResultSetToEntity(ResultSet rs) throws SQLException {
        MasterUnitupi entity = new MasterUnitupi();
        entity.setUnitupi(rs.getString("unitupi"));
        entity.setAlamat(rs.getString("alamat"));
        entity.setKota(rs.getString("kota"));
        entity.setManager(rs.getString("manager"));
        entity.setNama(rs.getString("nama"));
        entity.setNamaSingkat(rs.getString("nama_singkat"));
        entity.setSatuan(rs.getString("satuan"));
        entity.setTelepon(rs.getString("telepon"));
        entity.setTsMs(rs.getLong("__ts_ms"));
        entity.setDeleted(rs.getInt("__deleted"));
        return entity;
    }

}
