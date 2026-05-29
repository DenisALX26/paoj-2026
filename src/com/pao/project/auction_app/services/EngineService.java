package com.pao.project.auction_app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.pao.project.auction_app.models.engines.Engine;
import com.pao.project.auction_app.repository.EngineRepository;

public class EngineService {
    private static EngineService instance;
    private final EngineRepository engineRepository = new EngineRepository();
    private final List<Engine> allEngines = new ArrayList<>();

    private EngineService() {
        allEngines.addAll(engineRepository.findAll());
    }

    public static EngineService getInstance() {
        if (instance == null) {
            instance = new EngineService();
        }
        return instance;
    }

    public Engine getEngineById(UUID id) {
        return engineRepository.findById(id).orElse(null);
    }

    public void addEngine(Engine engine) {
        allEngines.add(engine);
        
        engineRepository.save(engine);
    }
}