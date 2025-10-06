package org.iconpln.users.controller;


import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.iconpln.users.entity.User;
import org.iconpln.users.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST API Resource untuk mengakses data dari ClickHouse dan PostgreSQL
 *
 * Base Path: /api/data
 * Semua endpoint dibawah path ini
 */
//@Path("/api/data")
@Tag(name = "Data Users", description = "Endpoints untuk mengakses ClickHouse dan PostgreSQL")
public class UserController {

    /**
     * Inject PostgreSQL Repository
     */
    @Inject
    UserRepository userRepository;


// =====================================
    // POSTGRESQL ENDPOINTS
    // =====================================

    /**
     * GET /api/data/postgresql/users
     * Mengambil semua data user dari PostgreSQL
     *
     * Query Parameters:
     * - active (optional): filter user aktif (true/false)
     * - page (optional): nomor halaman untuk pagination
     * - size (optional): jumlah data per halaman
     */
    @GET
    @Path("/postgresql/users")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get all Users from PostgreSQL",
            description = "Mengambil data user dengan optional filtering dan pagination"
    )
    public Response getAllUsers(
            @QueryParam("active") Boolean active,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size) {
        try {
            List<User> users;

            // Cek apakah ada filter active
            if (active != null && active) {
                users = userRepository.findActiveUsers();
            }
            // Cek apakah ada pagination
            else if (page != null && size != null) {
                users = userRepository.findWithPagination(page, size);
            }
            // Ambil semua data
            else {
                users = userRepository.findAllSorted();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Users retrieved successfully");
            response.put("count", users.size());
            response.put("data", users);

            return Response.ok(response).build();

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error fetching users");
            errorResponse.put("error", e.getMessage());

            return Response
                    .serverError()
                    .entity(errorResponse)
                    .build();
        }
    }

    /**
     * GET /api/data/postgresql/users/{username}
     * Mengambil user berdasarkan username
     */
    @GET
    @Path("/postgresql/users/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get User by username from PostgreSQL")
    public Response getUserByUsername(@PathParam("username") String username) {
        try {
            User user = userRepository.findByUsername(username);

            if (user != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", user);

                return Response.ok(response).build();
            } else {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity(Map.of("success", false, "message", "User not found"))
                        .build();
            }
        } catch (Exception e) {
            return Response
                    .serverError()
                    .entity(Map.of("success", false, "error", e.getMessage()))
                    .build();
        }
    }

    /**
     * GET /api/data/postgresql/users/search
     * Search user berdasarkan keyword di username
     */
    @GET
    @Path("/postgresql/users/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Search users by username keyword")
    public Response searchUsers(@QueryParam("keyword") String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("success", false, "message", "Keyword is required"))
                        .build();
            }

            List<User> users = userRepository.searchByUsername(keyword);

            return Response.ok(Map.of(
                    "success", true,
                    "count", users.size(),
                    "data", users
            )).build();

        } catch (Exception e) {
            return Response
                    .serverError()
                    .entity(Map.of("success", false, "error", e.getMessage()))
                    .build();
        }
    }

    /**
     * POST /api/data/postgresql/users
     * Menambah user baru
     *
     * @Transactional diperlukan untuk operasi write
     */
    @POST
    @Path("/postgresql/users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Operation(summary = "Create new user in PostgreSQL")
    public Response createUser(User user) {
        try {
            // Validasi input
            if (user.getUsername() == null || user.getEmail() == null) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("success", false, "message", "Username and email are required"))
                        .build();
            }

            // Cek apakah username sudah ada
            if (userRepository.isUsernameExists(user.getUsername())) {
                return Response
                        .status(Response.Status.CONFLICT)
                        .entity(Map.of("success", false, "message", "Username already exists"))
                        .build();
            }

            // Simpan user
            User savedUser = userRepository.save(user);

            return Response
                    .status(Response.Status.CREATED)
                    .entity(Map.of(
                            "success", true,
                            "message", "User created successfully",
                            "data", savedUser
                    ))
                    .build();

        } catch (Exception e) {
            return Response
                    .serverError()
                    .entity(Map.of("success", false, "error", e.getMessage()))
                    .build();
        }
    }

    // =====================================
    // HEALTH CHECK & STATISTICS
    // =====================================

    /**
     * GET /api/data/health
     * Check koneksi kedua database
     */
    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Check database connections and statistics")
    public Response healthCheck() {
        Map<String, Object> health = new HashMap<>();

        try {
            // Test PostgreSQL
            long postgresCount = userRepository.count();
            long activeUsers = userRepository.countActiveUsers();
            health.put("postgresql", Map.of(
                    "status", "connected",
                    "total_users", postgresCount,
                    "active_users", activeUsers
            ));
        } catch (Exception e) {
            health.put("postgresql", Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }

        return Response.ok(health).build();
    }
}
