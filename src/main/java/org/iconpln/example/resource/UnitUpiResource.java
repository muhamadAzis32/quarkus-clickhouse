package org.iconpln.example.resource;

import org.iconpln.example.entity.MasterUnitupi;
import org.iconpln.example.service.UnitUpiService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Optional;
/**
 * REST API untuk master_unitupi
 */
@Path("/api/unitupi")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UnitUpiResource {
    private static final Logger LOG = Logger.getLogger(UnitUpiResource.class);

    @Inject
    UnitUpiService service;

    /**
     * GET /api/unitupi - Mendapatkan semua data unit UPI
     */
    @GET
    public Response getAllUnits() {
        try {
            List<MasterUnitupi> units = service.getAllUnits();
            return Response.ok(units).build();
        } catch (Exception e) {
            LOG.error("Error pada getAllUnits", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    /**
     * Inner class untuk error response
     */
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
