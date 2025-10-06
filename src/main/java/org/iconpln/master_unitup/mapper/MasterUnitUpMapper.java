package org.iconpln.master_unitup.mapper;

import org.iconpln.master_unitup.entity.MasterUnitUp;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mapper untuk mengubah ResultSet menjadi entity MasterUnitUp
 */
public class MasterUnitUpMapper {

    private MasterUnitUpMapper() {
        // private constructor agar class ini tidak bisa diinstansiasi
    }

    public static MasterUnitUp fromResultSet(ResultSet rs) throws SQLException {
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