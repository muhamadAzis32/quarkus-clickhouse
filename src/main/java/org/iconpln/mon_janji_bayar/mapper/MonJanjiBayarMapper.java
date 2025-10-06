package org.iconpln.mon_janji_bayar.mapper;

import org.iconpln.master_unitup.entity.MasterUnitUp;
import org.iconpln.mon_janji_bayar.entity.MonJanjiBayar;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MonJanjiBayarMapper {

    private MonJanjiBayarMapper() {
        // private constructor agar class ini tidak bisa diinstansiasi
    }

    public static MonJanjiBayar fromResultSet(ResultSet rs) throws SQLException {
        return MonJanjiBayar.builder()
                .id(rs.getString("id"))
                .idpelanggan(rs.getString("idpelanggan"))
                .kodePetugas(rs.getString("kode_petugas"))
                .rbm(rs.getString("rbm"))
                .rpTagihan(rs.getString("rp_tagihan"))
                .statusWo(rs.getString("status_wo"))
                .tglJanjiBayar(rs.getTimestamp("tgl_janji_bayar") != null
                        ? rs.getTimestamp("tgl_janji_bayar").toLocalDateTime().toLocalDate()
                        : null)
                .tglPelaksanaanPra(rs.getTimestamp("tgl_pelaksanaan_pra") != null
                        ? rs.getTimestamp("tgl_pelaksanaan_pra").toLocalDateTime().toLocalDate()
                        : null)
                .thblrek(rs.getString("thblrek"))
                .unitup(rs.getString("unitup"))
                .idRbm(rs.getString("id_rbm"))
                .namaUnitap(rs.getString("nama_unitap"))
                .namaUnitup(rs.getString("nama_unitup"))
                .namaUnitupi(rs.getString("nama_unitupi"))
                .unitap(rs.getString("unitap"))
                .unitupi(rs.getString("unitupi"))
                .deleted(rs.getInt("__deleted"))
                .build();
    }
}
