package org.iconpln.users.repository;


import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.PersistenceUnit;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.iconpln.users.entity.User;

import java.util.List;

/**
 * Repository untuk PostgreSQL menggunakan Panache
 * @PersistenceUnit("postgresql") = menentukan persistence unit yang digunakan
 */
@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    /**
     * Mencari user berdasarkan username
     * Method find() dari Panache menggunakan format:
     * find("fieldName", value)
     *
     * @param username - username yang dicari
     * @return User atau null jika tidak ditemukan
     */
    public User findByUsername(String username) {
        // find() mengembalikan PanacheQuery
        // firstResult() mengambil hasil pertama atau null
        return find("username", username).firstResult();
    }

    /**
     * Mencari user berdasarkan email
     *
     * @param email - email yang dicari
     * @return User atau null jika tidak ditemukan
     */
    public User findByEmail(String email) {
        return find("email", email).firstResult();
    }

    /**
     * Mencari semua user yang aktif
     *
     * @return List<User> - daftar user aktif
     */
    public List<User> findActiveUsers() {
        // Menggunakan query dengan kondisi WHERE
        return list("isActive", true);
    }

    /**
     * Mencari semua user dengan sorting berdasarkan username
     *
     * @return List<User> - daftar user terurut
     */
    public List<User> findAllSorted() {
        // Sort.by("fieldName") untuk ascending
        // Sort.descending("fieldName") untuk descending
        return listAll(Sort.by("username"));
    }

    /**
     * Mencari user dengan pagination
     *
     * @param page - nomor halaman (dimulai dari 0)
     * @param size - jumlah data per halaman
     * @return List<User> - daftar user sesuai halaman
     */
    public List<User> findWithPagination(int page, int size) {
        // find().page(pageIndex, pageSize).list()
        return findAll()
                .page(page, size)
                .list();
    }

    /**
     * Mencari user dengan query JPQL custom
     * Contoh: mencari user yang username mengandung kata tertentu
     *
     * @param keyword - kata kunci pencarian
     * @return List<User> - hasil pencarian
     */
    public List<User> searchByUsername(String keyword) {
        // Menggunakan query JPQL
        // :keyword adalah named parameter
        return list("username LIKE ?1", "%" + keyword + "%");
    }

    /**
     * Menghitung jumlah user aktif
     *
     * @return long - jumlah user aktif
     */
    public long countActiveUsers() {
        return count("isActive", true);
    }

    /**
     * Menyimpan atau update user
     * @Transactional diperlukan untuk operasi write (INSERT/UPDATE/DELETE)
     *
     * @param user - user yang akan disimpan
     * @return User - user yang sudah disimpan dengan ID
     */
    @Transactional
    public User save(User user) {
        // persist() untuk menyimpan entity baru atau update entity existing
        // Jika ID null = INSERT
        // Jika ID ada = UPDATE
        persist(user);
        return user;
    }

    /**
     * Menghapus user berdasarkan ID
     *
     * @param id - ID user yang akan dihapus
     * @return boolean - true jika berhasil dihapus
     */
    @Transactional
    public boolean deleteById(Long id) {
        // deleteById() dari Panache
        // Return true jika data ditemukan dan dihapus
        return deleteById(id);
    }

    /**
     * Mengaktifkan/nonaktifkan user
     *
     * @param id - ID user
     * @param isActive - status aktif
     */
    @Transactional
    public void updateStatus(Long id, boolean isActive) {
        // update() method dari Panache untuk bulk update
        update("isActive = ?1 where id = ?2", isActive, id);
    }

    /**
     * Contoh query dengan multiple conditions
     * Mencari user aktif dengan email domain tertentu
     *
     * @param domain - domain email (contoh: "example.com")
     * @return List<User> - hasil pencarian
     */
    public List<User> findActiveUsersByEmailDomain(String domain) {
        return list(
                "isActive = ?1 AND email LIKE ?2",
                true,
                "%" + domain
        );
    }

    /**
     * Check apakah username sudah digunakan
     *
     * @param username - username yang akan dicek
     * @return boolean - true jika sudah ada
     */
    public boolean isUsernameExists(String username) {
        // count() untuk menghitung jumlah data
        // Jika > 0 berarti sudah ada
        return count("username", username) > 0;
    }

    /**
     * Mengambil semua user dengan eager loading (jika ada relasi)
     * Ini contoh jika nanti User punya relasi @OneToMany atau @ManyToOne
     *
     * @return List<User>
     */
    public List<User> findAllWithDetails() {
        // Jika User punya relasi, bisa pakai query JPQL dengan JOIN FETCH
        // return list("SELECT u FROM User u LEFT JOIN FETCH u.roles");

        // Untuk saat ini karena tidak ada relasi:
        return listAll();
    }
}