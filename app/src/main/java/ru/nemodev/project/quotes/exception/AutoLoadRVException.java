package ru.nemodev.project.quotes.exception;

public class AutoLoadRVException extends RuntimeException
{
    public AutoLoadRVException()
    {
        this("Exception in AutoLoadRecyclerView");
    }

    public AutoLoadRVException(String message)
    {
        super(message);
    }
}
