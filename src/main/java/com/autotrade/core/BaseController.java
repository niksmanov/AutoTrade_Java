package com.autotrade.core;

public abstract class BaseController
{
    protected Response added(boolean ok)
    {
        return result(ok, Messages.INFO_ENTITY_ADDED, Messages.ERROR_ENTITY_EXISTS);
    }

    protected Response deleted(boolean ok)
    {
        return result(ok, Messages.INFO_ENTITY_DELETED, Messages.ERROR_DELETE_PROBLEM);
    }

    protected Response error(String error)
    {
        var response = new Response(false);
        response.getErrors().add(error);
        return response;
    }

    protected Response result()
    {
        return new Response();
    }

    protected Response result(boolean ok)
    {
        return new Response(ok);
    }

    protected Response result(boolean ok, Object data)
    {
        return new Response(ok, data);
    }

    protected Response result(boolean ok, String success, String failure)
    {
        var response = new Response(ok);
        response.getErrors().add(ok ? success : failure);
        return response;
    }

    protected Response result(boolean ok, Object data, String message)
    {
        var response = new Response(ok, data);
        response.getErrors().add(message);
        return response;
    }
}
