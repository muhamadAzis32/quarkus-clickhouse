package org.iconpln.master_unitap.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MasterUnitAp {

    private String unitap;

    private String alamat;
    private String kota;
    private String manager;
    private String nama;

    @JsonProperty("nama_singkat")
    private String namaSingkat;

    private String satuan;
    private String telepon;
    private String unitupi;

    @JsonProperty("__deleted")
    @Builder.Default
    private Integer deleted = 0;

}
