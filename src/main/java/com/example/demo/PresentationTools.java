package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PresentationTools {
    private List<Presentation> presentations = new ArrayList<>();

    public PresentationTools() {
        var p1 = new Presentation("Building Microservices with Spring Boot",
                "https://www.youtube.com/watch?v=vtPkZShrvXQ", 2016);
        var p2 = new Presentation("Cloud Native Java with Spring Boot and Spring Cloud",
                "https://www.youtube.com/watch?v=5y0k3wYp6k8", 2017);
        var p3 = new Presentation("Spring Boot 2: New Features and Updates",
                "https://www.youtube.com/watch?v=9SGDpanrc8U", 2018);
        var p4 = new Presentation("Spring Boot in Action",
                "https://www.youtube.com/watch?v=vtPkZShrvXQ", 2019);
        var p5 = new Presentation("Spring Boot 2.3: What's New",
                "https://www.youtube.com/watch?v=Z1ktxiqyiLA", 2020);

        this.presentations.addAll(List.of(p1, p2, p3, p4, p5));
    }

    // ✅ New method: return ALL presentations
    public List<Presentation> getPresentations() {
        return presentations;
    }

    // Filter by year
    public List<Presentation> getPresentations(int year) {
        return presentations.stream()
                            .filter(p -> p.year() == year)
                            .toList();
    }

    // Explicit year filter
    public List<Presentation> getPresentationsByYear(int year) {
        return presentations.stream()
                            .filter(p -> p.year() == year)
                            .toList();
    }

    // Return as maps
    public List<Map<String, Object>> getPresentationsAsMap(int year) {
        return presentations.stream()
                            .filter(p -> p.year() == year)
                            .map(p -> {
                                Map<String, Object> map = new HashMap<>();
                                map.put("title", p.title());
                                map.put("url", p.url());
                                map.put("year", p.year());
                                return map;
                            })
                            .collect(Collectors.toList());
    }
}
