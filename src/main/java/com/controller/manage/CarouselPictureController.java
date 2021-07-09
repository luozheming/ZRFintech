package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.pojo.CarouselPicture;
import com.service.manage.CarouselPictureService;
import com.utils.CommonUtils;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/carouselPicture")
public class CarouselPictureController {

    @Autowired
    private CarouselPictureService carouselPictureService;
    @Autowired
    private CommonUtils commonUtils;
    @Value("${carouselPictureFilePath}")
    private String carouselPictureFilePath;

    @GetMapping("/list")
    public String list(Integer photoType, Integer status, String pageLocation) {
        List<CarouselPicture> carouselPictures = carouselPictureService.list(photoType, status, pageLocation);
        OutputFormate outputFormate = new OutputFormate(carouselPictures, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    @PostMapping("/status")
    public String status(String id, Integer status) {
        carouselPictureService.status(id, status);
        return ErrorCode.SUCCESS.toJsonString();
    }

    @PostMapping("/add")
    public String add(MultipartFile photoFile, CarouselPicture carouselPicture) {
        try {
            String filePath = carouselPictureFilePath;
            if (null != photoFile) {
                commonUtils.uploadData(photoFile, filePath);
                carouselPicture.setPhotoRoute(filePath + "/" + photoFile.getOriginalFilename());
            }
            carouselPictureService.add(carouselPicture);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        return ErrorCode.SUCCESS.toJsonString();
    }

    @PostMapping("/edit")
    public String edit(MultipartFile photoFile, CarouselPicture carouselPicture) {
        try {
            String filePath = carouselPictureFilePath;
            if (null != photoFile) {
                commonUtils.uploadData(photoFile, filePath);
                carouselPicture.setPhotoRoute(filePath + "/" + photoFile.getOriginalFilename());
            }
            carouselPictureService.edit(carouselPicture);
        } catch (Exception e) {
            return ErrorCode.OTHEREEEOR.toJsonString();
        }
        return ErrorCode.SUCCESS.toJsonString();
    }

    @GetMapping("/detail")
    public String detail(String id) {
        CarouselPicture carouselPicture = carouselPictureService.detail(id);
        OutputFormate outputFormate = new OutputFormate(carouselPicture, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

}
