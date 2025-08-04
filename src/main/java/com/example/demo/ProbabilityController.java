package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

@RestController
public class ProbabilityController {
    private final Random random = new Random();

    @GetMapping("/api/choose")
    public ResponseEntity<String> chooseByProbability(
            @RequestParam List<String> items,
            @RequestParam List<Double> weights) {

        if (items.size() != weights.size() || items.isEmpty()) {
            return ResponseEntity.badRequest().body("items と weights は同じ長さのリストで、空であってはいけません");
        }

        // 累積確率に変換
        NavigableMap<Double, String> map = new TreeMap<>();
        double total = 0.0;
        for (int i = 0; i < weights.size(); i++) {
            total += weights.get(i);
            map.put(total, items.get(i));
        }

        // ランダムに選択
        double r = random.nextDouble() * total;
        String selected = map.higherEntry(r).getValue();
        return ResponseEntity.ok(selected);
    }
}
