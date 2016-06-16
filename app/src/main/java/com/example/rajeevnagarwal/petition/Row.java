package com.example.rajeevnagarwal.petition;

/**
 * Created by Rajeev Nagarwal on 6/11/2016.
 */
public class Row {
    private String id;
    private String heading;
    public Row(String a,String b)
    {
        this.id = a;
        this.heading = b;
    }
    public String getId()
    { return id;}
    public String getHeading()
    {
        return heading;
    }


}
