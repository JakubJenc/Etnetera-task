package com.etnetera.hr.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Simple REST controller for accessing application logic.
 *
 * @author Etnetera
 */
@RestController
public class JavaScriptFrameworkController extends EtnRestController {

    private final JavaScriptFrameworkRepository repository;

    @Autowired
    public JavaScriptFrameworkController(JavaScriptFrameworkRepository repository) {
        this.repository = repository;
    }

    @ModelAttribute
    LocalDate initLocalDate() {
        return LocalDate.now();
    }

    @GetMapping("/frameworks")
    public Iterable<JavaScriptFramework> frameworks() {
        return repository.findAll();
    }

    @GetMapping("frameworks/{frameworkId}")
    public Optional<JavaScriptFramework> framework(@PathVariable Long frameworkId) {
        return repository.findById(frameworkId);
    }

    @PostMapping("/frameworks")
    public ResponseEntity saveFramework(String name, String version, LocalDate deprecationDate, Integer hypeLevel, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ModelAttribute LocalDate beginning) {
        JavaScriptFramework frameworkToSave = new JavaScriptFramework(name, version, deprecationDate, hypeLevel);
        repository.save(frameworkToSave);
        return ResponseEntity.ok(frameworkToSave);
    }

    @PutMapping("/frameworks/{frameworkId}")
    public JavaScriptFramework updateFramework(@PathVariable Long frameworkId, String name, String version, LocalDate deprecationDate, Integer hypeLevel, JavaScriptFramework frameworkToUpdate) throws Exception {
        if (!repository.existsById(frameworkId)) {
            throw new NotFoundException("Framework has to exist in the database!");
        }
        if (repository.existsById(frameworkId)) {
            if (version != null) {
                frameworkToUpdate.setVersion(version);
            }
            if (deprecationDate != null) {
                frameworkToUpdate.setDeprecationDate(deprecationDate);
            }
            if (hypeLevel != null) {
                frameworkToUpdate.setHypeLevel(hypeLevel);
            }
            if (name == null) {
                frameworkToUpdate.setName(name);
            }
            repository.save(frameworkToUpdate);
            return frameworkToUpdate;
        } else
            return null;
    }

    @DeleteMapping("/frameworks/{frameworkId}")
    public void deleteFramework(@PathVariable Long frameworkId) {
        repository.deleteById(frameworkId);
    }

    @GetMapping("/frameworks/search/{keyword}")
    public List<JavaScriptFramework> searchInFrameworks(@PathVariable String keyword) {
        Iterable<JavaScriptFramework> frameworks = repository.findAll();
        List<JavaScriptFramework> result = new ArrayList<>();
        for (JavaScriptFramework framework : frameworks) {
            if (framework.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(framework);
            }
        }
        return result;
    }

}
