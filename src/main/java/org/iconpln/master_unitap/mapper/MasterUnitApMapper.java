package org.iconpln.master_unitap.mapper;

import org.iconpln.master_unitap.entity.MasterUnitAp;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mapper untuk mengubah ResultSet menjadi entity MasterUnitUp
 */
public class MasterUnitApMapper {

    private MasterUnitApMapper() {
        // private constructor agar class ini tidak bisa diinstansiasi
    }

    public static MasterUnitAp fromResultSet(ResultSet rs) throws SQLException {
        return MasterUnitAp.builder()
                .unitap(rs.getString("unitap"))
                .alamat(rs.getString("alamat"))
                .kota(rs.getString("kota"))
                .manager(rs.getString("manager"))
                .nama(rs.getString("nama"))
                .namaSingkat(rs.getString("nama_singkat"))
                .satuan(rs.getString("satuan"))
                .telepon(rs.getString("telepon"))
                .unitap(rs.getString("unitupi"))
                .deleted(rs.getInt("__deleted"))
                .build();
    }
}