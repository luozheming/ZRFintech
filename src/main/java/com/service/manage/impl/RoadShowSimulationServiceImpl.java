package com.service.manage.impl;

import com.enums.VoicePrompt;
import com.pojo.RoadShowSimulation;
import com.service.manage.RoadShowSimulationService;
import com.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class RoadShowSimulationServiceImpl implements RoadShowSimulationService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CommonUtils commonUtils;

    @Override
    public List<RoadShowSimulation> list(String totalDuration) {
        Query query = new Query();
        if (!StringUtils.isEmpty(totalDuration)) {
            query.addCriteria(where("totalDuration").is(totalDuration));
        }
        return mongoTemplate.find(query, RoadShowSimulation.class);
    }

    @Override
    public void add(RoadShowSimulation roadShowSimulation) {
        String id = commonUtils.getNumCode();
        roadShowSimulation.setId(id);
        roadShowSimulation.setCreateTime(new Date());
        mongoTemplate.save(roadShowSimulation);
    }

    @Override
    public List<VoicePrompt> voicePromptList() {
        return Arrays.asList(VoicePrompt.values());
    }

}
