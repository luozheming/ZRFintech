package com.service.manage;

import com.pojo.City;

import java.util.List;

public interface CityService {
    List<City> list(Integer cityType);
    void edit(City city);
    void add(City city);
}
