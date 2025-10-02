package org.iconpln.master_unitup.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.enterprise.context.ApplicationScoped;
import org.iconpln.master_unitup.entity.MasterUnitUp;
import org.iconpln.master_unitup.repository.MasterUnitUpRepository;

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
public class MasterUnitUpService {

    private final MasterUnitUpRepository repository;

    /**
     * Mendapatkan semua data unit UP
     */
    public List<MasterUnitUp> getAllUnits() {
        try {
            log.info("Mengambil semua data unit UP");
            return repository.findAll();
        } catch (SQLException e) {
            log.error("Error saat mengambil semua data", e);
            throw new RuntimeException("Gagal mengambil data: " + e.getMessage(), e);
        }
    }
}
