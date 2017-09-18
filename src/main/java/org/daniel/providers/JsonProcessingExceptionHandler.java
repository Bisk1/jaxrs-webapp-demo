package org.daniel.providers;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

// I want to inform users of their mistakes in JSON syntax, so I must implement ExceptionMapper
@Provider
public class JsonProcessingExceptionHandler implements ExceptionMapper<JsonProcessingException> {

    @Context
    private HttpHeaders headers;

    public Response toResponse(JsonProcessingException ex){
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Invalid JSON: " + ex.getOriginalMessage())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN)
                .build();
    }

}