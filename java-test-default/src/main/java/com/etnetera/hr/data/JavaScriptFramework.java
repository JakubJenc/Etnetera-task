package com.etnetera.hr.data;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

/**
 * Simple data entity describing basic properties of every JavaScript framework.
 *
 * @author Etnetera
 */
@Entity
public class JavaScriptFramework {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 6)
    private double version;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deprecationDate;

    private int hypeLevel;

    public JavaScriptFramework() {
    }

    public JavaScriptFramework(String name) {
        this.name = name;
    }

    public JavaScriptFramework(String name, double version, LocalDate deprecationDate, int hypeLevel) {
        this.name = name;
        this.version = version;
        this.deprecationDate = deprecationDate;
        this.hypeLevel = hypeLevel;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public LocalDate getDeprecationDate() {
        return deprecationDate;
    }

    public void setDeprecationDate(LocalDate deprecationDate) {
        this.deprecationDate = deprecationDate;
    }

    public int getHypeLevel() {
        return hypeLevel;
    }

    public void setHypeLevel(int hypeLevel) {
        this.hypeLevel = hypeLevel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "JavaScriptFramework [id=" + id + ", name=" + name + "]";
    }

}
