package com.example.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CharacterService {
    private final DndApiClient apiClient;
    private final Random random = new Random();
    @Autowired
    public CharacterService(DndApiClient dndApiClient)
    {
        this.apiClient=dndApiClient;
    }
    public CharacterJson generate(String race,String characterClass,int level)
    {
        CharacterJson characterJson = new CharacterJson();
        characterJson.setgenerate(race,characterClass,level);
        Map<String,Integer>stats = Map.of(
                "STR", roll(),
                "DEX", roll(),
                "CON", roll(),
                "INT", roll(),
                "WIS", roll(),
                "CHA", roll()
        );
        characterJson.setStats(stats);
        characterJson.setRacialTraits(apiClient.getRaceTraits(race));
        characterJson.setClassFeatures(apiClient.getClassFeatures(characterClass,level));
        int baseHP = 6 +(level-1)*(6+modifier(stats.get("CON")));
        characterJson.setHitPoints(baseHP);
        characterJson.setSpellsKnown(apiClient.getSpellsByClassAndLevel(characterClass,level));
        characterJson.setEquipment(apiClient.getStartingEquipment(characterClass));
        return characterJson;
    }
    private int roll(){
        return random.nextInt(20)+1;
    }
    private int modifier(int stat) {
        return (stat - 10) / 2;
    }
}
