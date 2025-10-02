package org.iconpln.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.iconpln.example.entity.MasterUnitupi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.iconpln.example.repository.UnitUpiRepository;
import org.jboss.logging.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service layer untuk business logic master_unitupi
 */
@ApplicationScoped
public class UnitUpiService {

    private static final Logger LOG = Logger.getLogger(UnitUpiService.class);

    @Inject
    UnitUpiRepository repository;

    /**
     * Mendapatkan semua data unit UPI
     */
    public List<MasterUnitupi> getAllUnits() {
        try {
            LOG.info("Mengambil semua data unit UPI");
            return repository.findAll();
        } catch (SQLException e) {
            LOG.error("Error saat mengambil semua data", e);
            throw new RuntimeException("Gagal mengambil data: " + e.getMessage(), e);
        }
    }

}
