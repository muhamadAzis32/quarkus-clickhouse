package org.iconpln.mon_janji_bayar.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonJanjiBayar {

    private String id;
    private String idpelanggan;
    private String kodePetugas;
    private String rbm;
    private String rpTagihan;
    private String statusWo;

    @JsonProperty("tgl_janji_bayar")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tglJanjiBayar;

    @JsonProperty("tgl_pelaksanaan_pra")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tglPelaksanaanPra;

    private String thblrek;
    private String unitup;
    private String idRbm;
    private String namaUnitap;
    private String namaUnitup;
    private String namaUnitupi;
    private String unitap;
    private String unitupi;

    @JsonProperty("__deleted")
    @Builder.Default
    private Integer deleted = 0;
}
