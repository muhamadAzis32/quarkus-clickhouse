package org.iconpln.master_unit_upi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.Data;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.iconpln.master_unit_upi.entity.MasterUnitupi;
import org.iconpln.master_unit_upi.service.UnitUpiService;
import org.iconpln.util.ResponseModel;

import java.util.List;
import java.util.Optional;

/**
 * REST API untuk master_unitupi
 * Menggunakan Lombok
 */
@Slf4j
@RequiredArgsConstructor
@Path("/api/unitupi")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Master UnitUpi", description = "Master UnitUpi Controller")
public class UnitUpiController {

    private final UnitUpiService service;

    @GET
    public Response getAllUnits() {
        try {
            List<MasterUnitupi> units = service.getAllUnits();

            return Response.ok(new ResponseModel(
                            "OK",
                            true,
                            200,
                            units))
                    .build();

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

                return Response.ok(new ResponseModel(
                                "OK",
                                true,
                                200,
                                unit.get()))
                        .build();

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
    @Path("/search")
    public Response searchUnits(@QueryParam("q") String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ErrorResponse.of("Parameter 'q' harus diisi"))
                        .build();
            }

            List<MasterUnitupi> units = service.searchUnitsByNama(keyword);
            return Response.ok(new ResponseModel(
                            "OK",
                            true,
                            200,
                            units))
                    .build();


        } catch (Exception e) {
            log.error("Error pada searchUnits", e);
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