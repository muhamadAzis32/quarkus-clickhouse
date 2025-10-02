package org.iconpln.example.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.Data;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.iconpln.example.entity.MasterUnitupi;
import org.iconpln.example.service.UnitUpiService;

import java.util.List;
import java.util.Optional;

/**
 * REST API untuk master_unitupi
 * Menggunakan Lombok untuk cleaner code
 */
@Slf4j
@RequiredArgsConstructor
@Path("/api/unitupi")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UnitUpiResource {

    private final UnitUpiService service;

    @GET
    public Response getAllUnits() {
        try {
            List<MasterUnitupi> units = service.getAllUnits();
            return Response.ok(units).build();
        } catch (Exception e) {
            log.error("Error pada getAllUnits", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorResponse.of(e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{code}")
    public Response getUnitByCode(@PathParam("code") String code) {
        try {
            Optional<MasterUnitupi> unit = service.getUnitByCode(code);
            if (unit.isPresent()) {
                return Response.ok(unit.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ErrorResponse.of("Unit UPI dengan kode " + code + " tidak ditemukan"))
                        .build();
            }
        } catch (Exception e) {
            log.error("Error pada getUnitByCode", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorResponse.of(e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/kota/{kota}")
    public Response getUnitsByKota(@PathParam("kota") String kota) {
        try {
            List<MasterUnitupi> units = service.getUnitsByKota(kota);
            return Response.ok(units).build();
        } catch (Exception e) {
            log.error("Error pada getUnitsByKota", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorResponse.of(e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/search")
    public Response searchUnits(@QueryParam("q") String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ErrorResponse.of("Parameter 'q' harus diisi"))
                        .build();
            }

            List<MasterUnitupi> units = service.searchUnitsByNama(keyword);
            return Response.ok(units).build();
        } catch (Exception e) {
            log.error("Error pada searchUnits", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorResponse.of(e.getMessage()))
                    .build();
        }
    }

    @POST
    public Response createUnit(MasterUnitupi unit) {
        try {
            MasterUnitupi created = service.createUnit(unit);
            return Response.status(Response.Status.CREATED)
                    .entity(created)
                    .build();
        } catch (IllegalArgumentException e) {
            log.warn("Validasi gagal pada createUnit", e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ErrorResponse.of(e.getMessage()))
                    .build();
        } catch (Exception e) {
            log.error("Error pada createUnit", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorResponse.of(e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{code}")
    public Response updateUnit(@PathParam("code") String code, MasterUnitupi unit) {
        try {
            MasterUnitupi updated = service.updateUnit(code, unit);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            log.warn("Validasi gagal pada updateUnit", e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ErrorResponse.of(e.getMessage()))
                    .build();
        } catch (Exception e) {
            log.error("Error pada updateUnit", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorResponse.of(e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{code}")
    public Response deleteUnit(@PathParam("code") String code) {
        try {
            service.deleteUnit(code);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            log.warn("Validasi gagal pada deleteUnit", e);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ErrorResponse.of(e.getMessage()))
                    .build();
        } catch (Exception e) {
            log.error("Error pada deleteUnit", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorResponse.of(e.getMessage()))
                    .build();
        }
    }

    /**
     * Error Response DTO dengan Lombok
     */
    @Data(staticConstructor = "of")
    public static class ErrorResponse {
        private final String message;
    }
}