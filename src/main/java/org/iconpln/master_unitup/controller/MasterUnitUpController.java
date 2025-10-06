package org.iconpln.master_unitup.controller;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.iconpln.master_unitup.entity.MasterUnitUp;
import org.iconpln.master_unitup.service.MasterUnitUpService;
import org.iconpln.util.PagedResultDto;
import org.iconpln.util.ResponseModel;
import org.iconpln.util.ErrorResponse;

@Slf4j
@Path("/api/unitup")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Master UnitUp", description = "Master UnitUp Controller")
public class MasterUnitUpController {

    @Inject
    MasterUnitUpService service;

    /**
     * GET /api/unitup?page=1&size=10
     * Mendapatkan data unitup dengan pagination
     *
     * @param page Nomor halaman (default: 1)
     * @param size Jumlah data per halaman (default: 10, max: 100)
     * @return Response dengan Result
     */
    @GET
    public Response getAll(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        try {
            log.info("REST: GET /api/unitup?page={}&size={}", page, size);

            PagedResultDto<MasterUnitUp> dataUnitUp = service.getAll(page, size);

            return Response.ok(new ResponseModel(
                            "OK",
                            true,
                            200,
                            dataUnitUp))
                    .build();

        } catch (IllegalArgumentException e) {
            log.warn("Invalid pagination parameters: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorResponse.of(e.getMessage()))
                    .build();
        } catch (Exception e) {
            log.error("Error pada getAll unitup", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorResponse.of(e.getMessage()))
                    .build();
        }
    }

}
