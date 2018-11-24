package ru.nemodev.project.quotes.core.load;

public class LoadEventInfo
{
    private int offset;
    private int limit;

    public LoadEventInfo(int offset, int limit)
    {
        this.offset = offset;
        this.limit = limit;
    }

    public int getOffset()
    {
        return offset;
    }

    public int getLimit()
    {
        return limit;
    }
}
