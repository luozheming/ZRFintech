package com.service;

import com.pojo.VIPCard;

import java.util.List;

public interface VIPCardService {
    VIPCard detail(String id);
    List<VIPCard> list();
}
