package com.controller.manage;

import com.alibaba.fastjson.JSONObject;
import com.dto.outdto.OutputFormate;
import com.enums.VoicePrompt;
import com.pojo.RoadShowSimulation;
import com.service.manage.RoadShowSimulationService;
import com.utils.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roadShowSimulation")
public class RoadShowSimulationController {
    @Autowired
    private RoadShowSimulationService roadShowSimulationService;

    @GetMapping("/list")
    public String list(String totalDuration) {
        List<RoadShowSimulation> roadShowSimulations = roadShowSimulationService.list(totalDuration);
        OutputFormate outputFormate = new OutputFormate(roadShowSimulations, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    @GetMapping("/voicePromptList")
    public String voicePromptList() {
        List<VoicePrompt> voicePrompts = roadShowSimulationService.voicePromptList();
        OutputFormate outputFormate = new OutputFormate(voicePrompts, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
        return JSONObject.toJSONString(outputFormate);
    }

    @PostMapping("/add")
    public String add(RoadShowSimulation roadShowSimulation) {
        roadShowSimulationService.add(roadShowSimulation);
        return ErrorCode.SUCCESS.toJsonString();
    }

}
