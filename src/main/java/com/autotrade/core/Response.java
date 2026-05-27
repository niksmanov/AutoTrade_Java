package com.autotrade.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Response
{
    private boolean succeeded;
    private Object data;
    private List<String> errors = new ArrayList<>();

    public Response(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public Response(boolean succeeded, Object data) {
        this.succeeded = succeeded;
        this.data = data;
    }

    public static Response error(String error) {
        var response = new Response(false);
        response.getErrors().add(error);
        return response;
    }

    public static Response errors(Collection<String> errors) {
        var response = new Response(false);
        response.getErrors().addAll(errors);
        return response;
    }
}
