package com.service.manage;

import com.enums.VoicePrompt;
import com.pojo.RoadShowSimulation;

import java.util.List;
import java.util.Map;

public interface RoadShowSimulationService {
    List<RoadShowSimulation> list(String totalDuration);
    void add(RoadShowSimulation roadShowSimulation);
    List<VoicePrompt> voicePromptList();
}
