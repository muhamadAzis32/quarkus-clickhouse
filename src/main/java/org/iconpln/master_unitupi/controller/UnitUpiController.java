package org.iconpln.master_unitupi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.Data;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.iconpln.master_unitupi.entity.MasterUnitupi;
import org.iconpln.master_unitupi.service.UnitUpiService;
import org.iconpln.util.ErrorResponse;
import org.iconpln.util.PagedResultDto;
import org.iconpln.util.ResponseModel;

import java.util.List;
import java.util.Optional;

/**
 * REST API untuk master_unitupi
 * Menggunakan Lombok
 */
@Slf4j
@RequiredArgsConstructor
@Path("/api/uniupi")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Master", description = "List Data Master Clickhouse")
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

    /**
     * GET /api/unitupi/paginated?page=1&size=10
     * Mendapatkan data unit UPI dengan pagination
     *
     * @param page Nomor halaman (default: 1)
     * @param size Jumlah data per halaman (default: 10, max: 100)
     * @return Response dengan PagedResult
     */
    @GET
    @Path("/paginated")
    public Response getAllUnitsPaginated(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        try {
            log.info("REST: GET /api/unitupi/paginated?page={}&size={}", page, size);

            PagedResultDto<MasterUnitupi> pagedResultDto = service.getAllUnitsPaginated(page, size);

            return Response.ok(new ResponseModel(
                            "OK",
                            true,
                            200,
                            pagedResultDto))
                    .build();

        } catch (IllegalArgumentException e) {
            log.warn("Invalid pagination parameters: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorResponse.of(e.getMessage()))
                    .build();

        } catch (Exception e) {
            log.error("Error pada getAllUnitsPaginated", e);
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


}