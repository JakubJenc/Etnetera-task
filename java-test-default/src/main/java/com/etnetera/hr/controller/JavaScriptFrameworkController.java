package com.etnetera.hr.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/frameworks")
    public Iterable<JavaScriptFramework> frameworks() {
        return repository.findAll();
    }

    @GetMapping("frameworks/{frameworkId}")
    public Optional<JavaScriptFramework> framework(@PathVariable Long frameworkId) {
        return repository.findById(frameworkId);
    }

    @PostMapping("/frameworks")
    public ResponseEntity saveFramework(String name, Float version, Integer hypeLevel) {
        JavaScriptFramework frameworkToSave = new JavaScriptFramework(name, version, hypeLevel);
        repository.save(frameworkToSave);
        return ResponseEntity.ok(frameworkToSave);
    }

    @PutMapping("/frameworks/{frameworkId}")
    public JavaScriptFramework updateFramework(@PathVariable Long frameworkId, String name, Float version,
                                               Integer hypeLevel, JavaScriptFramework frameworkToUpdate) throws Exception {
        if (!repository.existsById(frameworkId)) {
            throw new NotFoundException("Framework has to exist in the database!");
        }
        //JavaScriptFramework frameworkToUpdate = repository.findById(frameworkId);
        frameworkToUpdate.setName(name);
        frameworkToUpdate.setVersion(version);
        frameworkToUpdate.setHypeLevel(hypeLevel);
        repository.save(frameworkToUpdate);
        return frameworkToUpdate;
    }

    @DeleteMapping("/frameworks/{frameworkId}")
    public void deleteFramework(@PathVariable Long frameworkId) {
        repository.deleteById(frameworkId);
    }

    @GetMapping("/frameworks/search")
    public List<JavaScriptFramework> searchInFrameworks(String keyword) {
        Iterable<JavaScriptFramework> frameworks = repository.findAll();
        List<JavaScriptFramework> result = new ArrayList<>();
        for (JavaScriptFramework framework : frameworks) {
            if (framework.getName().contains(keyword)) {
                result.add(framework);
            }
        }
        return result;
    }

}
