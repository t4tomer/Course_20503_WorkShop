
// orginal code of Image class
package com.project.model;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Blob;
import java.util.Date;

@Entity
//create table called image_table in the Project_Data_Base

@Table(name = "image_table")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    private Blob image;

    private Date date = new Date();

    public Image() {
        System.out.println("New image empty constructor");
    }

    public Image(long NewId, Date newDate, Blob newImage) {
        this.id = NewId;
        this.date = newDate;
        this.image = newImage;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public Date getDate() {
        return date;
    }
}
