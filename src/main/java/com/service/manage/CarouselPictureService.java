package com.service.manage;

import com.pojo.CarouselPicture;

import java.util.List;

public interface CarouselPictureService {
    List<CarouselPicture> list(Integer photoType, Integer status, String pageLocation);
    void add(CarouselPicture carouselPicture);
    void status(String id, Integer status);
    void edit(CarouselPicture carouselPicture);
    CarouselPicture detail(String id);
    List<String> pageLocationList(Integer photoType);
}
