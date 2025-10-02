package org.iconpln.example.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.enterprise.context.ApplicationScoped;
import org.iconpln.example.entity.MasterUnitupi;
import org.iconpln.example.repository.UnitUpiRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service layer untuk business logic master_unitupi
 * Menggunakan Lombok untuk cleaner code
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
     * Mendapatkan unit UPI berdasarkan kota
     */
    public List<MasterUnitupi> getUnitsByKota(String kota) {
        try {
            log.info("Mengambil data unit UPI di kota: {}", kota);
            return repository.findByKota(kota);
        } catch (SQLException e) {
            log.error("Error saat mengambil data berdasarkan kota", e);
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

    /**
     * Membuat unit UPI baru menggunakan Builder pattern
     */
    public MasterUnitupi createUnit(MasterUnitupi unit) {
        try {
            validateUnit(unit);

            Optional<MasterUnitupi> existing = repository.findByUnitupi(unit.getUnitupi());
            if (existing.isPresent()) {
                throw new IllegalArgumentException("Unit UPI dengan kode " + unit.getUnitupi() + " sudah ada");
            }

            log.info("Membuat unit UPI baru dengan kode: {}", unit.getUnitupi());

            // Set default values jika null
            if (unit.getTsMs() == null) {
                unit.setTsMs(System.currentTimeMillis());
            }
            if (unit.getDeleted() == null) {
                unit.setDeleted(0);
            }

            repository.insert(unit);
            return unit;
        } catch (SQLException e) {
            log.error("Error saat membuat data", e);
            throw new RuntimeException("Gagal membuat data: " + e.getMessage(), e);
        }
    }

    /**
     * Update unit UPI
     */
    public MasterUnitupi updateUnit(String unitupi, MasterUnitupi unit) {
        try {
            Optional<MasterUnitupi> existing = repository.findByUnitupi(unitupi);
            if (existing.isEmpty()) {
                throw new IllegalArgumentException("Unit UPI dengan kode " + unitupi + " tidak ditemukan");
            }

            unit.setUnitupi(unitupi);
            validateUnit(unit);

            log.info("Mengupdate unit UPI dengan kode: {}", unitupi);
            repository.update(unit);
            return unit;
        } catch (SQLException e) {
            log.error("Error saat mengupdate data", e);
            throw new RuntimeException("Gagal mengupdate data: " + e.getMessage(), e);
        }
    }

    /**
     * Menghapus unit UPI (soft delete)
     */
    public void deleteUnit(String unitupi) {
        try {
            Optional<MasterUnitupi> existing = repository.findByUnitupi(unitupi);
            if (existing.isEmpty()) {
                throw new IllegalArgumentException("Unit UPI dengan kode " + unitupi + " tidak ditemukan");
            }

            log.info("Menghapus unit UPI dengan kode: {}", unitupi);
            repository.softDelete(unitupi);
        } catch (SQLException e) {
            log.error("Error saat menghapus data", e);
            throw new RuntimeException("Gagal menghapus data: " + e.getMessage(), e);
        }
    }

    /**
     * Validasi data unit UPI
     */
    private void validateUnit(MasterUnitupi unit) {
        if (unit.getUnitupi() == null || unit.getUnitupi().trim().isEmpty()) {
            throw new IllegalArgumentException("Kode unit UPI harus diisi");
        }
        if (unit.getNama() == null || unit.getNama().trim().isEmpty()) {
            throw new IllegalArgumentException("Nama unit UPI harus diisi");
        }
    }
}