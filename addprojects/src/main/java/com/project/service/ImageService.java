package com.project.service;

import com.project.model.Image;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ImageService {
    public Image create(Image image);

    public List<Image> viewAll();

    public Image viewById(long id);

    public void deleteAll();
}
