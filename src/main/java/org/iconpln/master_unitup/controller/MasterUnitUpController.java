package org.iconpln.master_unitup.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.Data;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.iconpln.master_unitupi.controller.UnitUpiController;
import org.iconpln.master_unitup.entity.MasterUnitUp;
import org.iconpln.master_unitup.service.MasterUnitUpService;
import org.iconpln.util.PagedResultDto;
import org.iconpln.util.ResponseModel;
import org.iconpln.util.ErrorResponse;

import java.util.List;

/**
 * REST API untuk master_unitup
 * Menggunakan Lombok
 */
@Slf4j
@RequiredArgsConstructor
@Path("/api/unitup")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Master UnitUp", description = "Master UnitUp Controller")
public class MasterUnitUpController {

    private final MasterUnitUpService service;

    @GET
    public Response getAllUnits() {
        try {
            List<MasterUnitUp> units = service.getAllUnits();

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
    @Path("/paginated")
    public Response getAllUnitsPaginated(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        try {
            log.info("REST: GET /api/unitup/paginated?page={}&size={}", page, size);

            PagedResultDto<MasterUnitUp> pagedResultDto = service.getAllUnitsPaginated(page, size);

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

//    /**
//     * Error Response DTO dengan Lombok
//     */
//    @Data(staticConstructor = "of")
//    public static class ErrorResponse {
//        private final String message;
//    }
}
