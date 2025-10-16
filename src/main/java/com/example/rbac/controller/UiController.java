package com.example.rbac.controller;

import com.example.rbac.model.UiComponent;
import com.example.rbac.model.UiPage;
import com.example.rbac.repository.UiComponentRepository;
import com.example.rbac.repository.UiPageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ui")
@RequiredArgsConstructor
public class UiController {

    private final UiPageRepository pageRepo;
    private final UiComponentRepository compRepo;

    @PostMapping("/pages")
    public UiPage createPage(@RequestBody UiPage page) {
        return pageRepo.save(page);
    }

    @PostMapping("/components/{pageId}")
    public List<UiComponent> addComponents(@PathVariable("pageId") Long pageId, @RequestBody List<UiComponent> components) {
        UiPage page = pageRepo.findById(pageId)
                .orElseThrow(() -> new RuntimeException("Page not found"));
        components.forEach(c -> c.setPage(page));
        return compRepo.saveAll(components);
    }

    @GetMapping("/pages/{pageId}")
    public Map<String, Object> getPage(@PathVariable("pageId") Long pageId) {
        UiPage page = pageRepo.findById(pageId).orElseThrow();
        List<UiComponent> comps = compRepo.findByPageId(pageId);
        return Map.of("page", page, "components", comps);
    }
}

