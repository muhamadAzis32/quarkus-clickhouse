package org.iconpln.util;

import lombok.Data;

/**
 * Generic DTO untuk Error Response
 *
 * Lombok @Data:
 *  - Getter, setter, equals, hashCode, toString
 * Lombok staticConstructor = "of":
 *  - Membuat factory method ErrorResponse.of(message)
 */
@Data(staticConstructor = "of")
public class ErrorResponse {
    private final String message;
}
