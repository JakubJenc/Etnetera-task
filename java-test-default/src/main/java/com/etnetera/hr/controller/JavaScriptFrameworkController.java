package com.etnetera.hr.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;

import java.time.LocalDate;
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
    public Optional<JavaScriptFramework> framework(@PathVariable long frameworkId) {
        return repository.findById(frameworkId);
    }

    @PostMapping("/frameworks")
    public JavaScriptFramework saveFramework(String name, double version, LocalDate deprecationDate, int hypeLevel) {
        JavaScriptFramework frameworkToSave = new JavaScriptFramework(name, version, deprecationDate, hypeLevel);
        repository.save(frameworkToSave);
        return frameworkToSave;
    }

    @PutMapping("/frameworks/{frameworkId}")
    public JavaScriptFramework updateFramework(@PathVariable long frameworkId, String name, double version,
                                               LocalDate deprecationDate, int hypeLevel, JavaScriptFramework frameworkToUpdate) throws Exception{
        if (!repository.existsById(frameworkId)) {
            throw new NotFoundException("Framework has to exist in the database!");
        }
        //JavaScriptFramework frameworkToUpdate = repository.findById(frameworkId);
        frameworkToUpdate.setName(name);
        frameworkToUpdate.setVersion(version);
        frameworkToUpdate.setDeprecationDate(deprecationDate);
        frameworkToUpdate.setHypeLevel(hypeLevel);
        repository.save(frameworkToUpdate);
        return frameworkToUpdate;
    }

    @DeleteMapping("/frameworks/{frameworkId}")
    public void deleteFramework(@PathVariable long frameworkId){
        repository.deleteById(frameworkId);
    }

}
