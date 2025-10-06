package org.iconpln.master_unitap.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.iconpln.master_unitap.entity.MasterUnitAp;
import org.iconpln.master_unitap.service.MasterUnitApService;
import org.iconpln.util.ErrorResponse;
import org.iconpln.util.PagedResultDto;
import org.iconpln.util.ResponseModel;

@Slf4j
@Path("/api/unitap")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Master UnitAp", description = "Master UnitAp Controller")
public class MasterUnitApController {

    @Inject
    MasterUnitApService service;

    @GET
    public Response getAll(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        try {
            log.info("REST: GET /api/unitap?page={}&size={}", page, size);

            PagedResultDto<MasterUnitAp> dataUnitAp = service.getAll(page, size);

            return Response.ok(new ResponseModel(
                            "OK",
                            true,
                            200,
                            dataUnitAp))
                    .build();

        } catch (IllegalArgumentException e) {
            log.warn("Invalid pagination parameters: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorResponse.of(e.getMessage()))
                    .build();
        } catch (Exception e) {
            log.error("Error pada getAll unitap", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorResponse.of(e.getMessage()))
                    .build();
        }
    }
}
