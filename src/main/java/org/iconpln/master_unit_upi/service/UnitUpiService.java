package org.iconpln.master_unit_upi.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.enterprise.context.ApplicationScoped;
import org.iconpln.master_unit_upi.entity.MasterUnitupi;
import org.iconpln.master_unit_upi.repository.UnitUpiRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service layer untuk business logic master_unitupi
 * Menggunakan Lombok
 */
@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class UnitUpiService {

    private final UnitUpiRepository repository;

    /**
     * Mendapatkan semua data unit UPI
     */
    public List<MasterUnitupi> getAllUnits() {
        try {
            log.info("Mengambil semua data unit UPI");
            return repository.findAll();
        } catch (SQLException e) {
            log.error("Error saat mengambil semua data", e);
            throw new RuntimeException("Gagal mengambil data: " + e.getMessage(), e);
        }
    }

    /**
     * Mendapatkan unit UPI berdasarkan kode
     */
    public Optional<MasterUnitupi> getUnitByCode(String unitupi) {
        try {
            log.info("Mengambil data unit UPI dengan kode: {}", unitupi);
            return repository.findByUnitupi(unitupi);
        } catch (SQLException e) {
            log.error("Error saat mengambil data berdasarkan kode", e);
            throw new RuntimeException("Gagal mengambil data: " + e.getMessage(), e);
        }
    }

    /**
     * Mencari unit UPI berdasarkan nama
     */
    public List<MasterUnitupi> searchUnitsByNama(String keyword) {
        try {
            log.info("Mencari data unit UPI dengan keyword: {}", keyword);
            return repository.searchByNama(keyword);
        } catch (SQLException e) {
            log.error("Error saat mencari data", e);
            throw new RuntimeException("Gagal mencari data: " + e.getMessage(), e);
        }
    }

}