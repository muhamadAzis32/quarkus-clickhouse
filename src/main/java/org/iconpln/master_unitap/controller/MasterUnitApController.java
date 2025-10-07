package org.iconpln.master_unitap.controller;

import io.smallrye.common.constraint.NotNull;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.config.inject.ConfigProperty;
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
import org.mindrot.jbcrypt.BCrypt;

@Slf4j
@Path("/api/unitap")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Master", description = "List Data Master Clickhouse")
public class MasterUnitApController {

    @Inject
    MasterUnitApService service;

    @Inject
    @ConfigProperty(name = "app.secret.key")
    String secretKeyNewAp2t;

    @GET
    public Response getAll(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @NotBlank @HeaderParam("X-Secret-Key") String header_key) {
        try {

            // cek header_key
            boolean checkHeader = BCrypt.checkpw(secretKeyNewAp2t, header_key);
            if (!checkHeader) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new ResponseModel("UNAUTHORIZED",
                                false,
                                401,
                                null))
                        .build();
            }

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
