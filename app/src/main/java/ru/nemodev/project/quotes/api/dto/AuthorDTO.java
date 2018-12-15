package ru.nemodev.project.quotes.api.dto;

import java.io.Serializable;

public class AuthorDTO implements Serializable
{
    private Long id;
    private String fullName;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }
}
