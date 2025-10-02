package org.iconpln.util;

public record ResponseModel (
        String message,
        Boolean success,
        Integer status,
        Object data
) {}
