package com.example.rbac.controller;

import com.example.rbac.model.UiComponent;
import com.example.rbac.model.UiComponentValue;
import com.example.rbac.model.UiPage;
import com.example.rbac.model.UiSubmission;
import com.example.rbac.repository.UiComponentRepository;
import com.example.rbac.repository.UiComponentValueRepository;
import com.example.rbac.repository.UiPageRepository;
import com.example.rbac.repository.UiSubmissionRepository;
import com.nimbusds.jose.shaded.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ui")
@RequiredArgsConstructor
public class UiSubmissionController {

    private final UiPageRepository pageRepo;
    private final UiComponentRepository compRepo;
    private final UiSubmissionRepository subRepo;
    private final UiComponentValueRepository valRepo;

    @PostMapping("/submit/{pageId}")
    public UiSubmission handleSubmission(@PathVariable("pageId") Long pageId, @RequestBody Map<String, Object> data) {
        UiPage page = pageRepo.findById(pageId)
                .orElseThrow(() -> new RuntimeException("Page not found"));

        UiSubmission submission = new UiSubmission();
        submission.setPage(page);
        subRepo.save(submission);

        data.forEach((key, value) -> {
            UiComponent component = compRepo.findByPageId(pageId)
                    .stream()
                    .filter(c -> c.getKeyName().equals(key))
                    .findFirst()
                    .orElse(null);

            if (component != null) {
                UiComponentValue compValue = new UiComponentValue();
                compValue.setSubmission(submission);
                compValue.setComponent(component);
                compValue.setValue(new Gson().toJson(value));
                valRepo.save(compValue);
            }
        });

        return submission;
    }

    @GetMapping("/submission/{id}")
    public List<UiComponentValue> getSubmissionValues(@PathVariable("pageId") Long id) {
        return valRepo.findAll().stream()
                .filter(v -> v.getSubmission().getId().equals(id))
                .toList();
    }
}

