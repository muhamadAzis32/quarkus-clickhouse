package org.iconpln.mon_janji_bayar.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.iconpln.mon_janji_bayar.entity.MonJanjiBayar;
import org.iconpln.mon_janji_bayar.service.MonJanjiBayarService;
import org.iconpln.util.ErrorResponse;
import org.iconpln.util.PagedResultDto;
import org.iconpln.util.ResponseModel;

@Slf4j
@Path("/api/mon-janji-bayar")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Mon Janji Bayar", description = "Mon Janji Bayar Controller")
public class MonJanjiBayarController {

    @Inject
    MonJanjiBayarService service;

    @GET
    public Response getAll(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        try {
            log.info("REST: GET /api/mon-janji-bayar?page={}&size={}", page, size);

            PagedResultDto<MonJanjiBayar> dataMonJanjiBayar = service.getAll(page, size);

            return Response.ok(new ResponseModel(
                            "OK",
                            true,
                            200,
                            dataMonJanjiBayar))
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
