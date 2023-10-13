package com.biricik.autogenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.biricik.autogenerator.JavaClassGenerator;
import com.biricik.autogenerator.model.ClassInfo;

@Controller
public class JavaClassGeneratorController {
    @Autowired
    private JavaClassGenerator javaClassGenerator;

    @GetMapping("/generate")
    public String showGeneratorForm(Model model) {
        model.addAttribute("classInfo", new ClassInfo());

        return "generate-form";
    }

    @PostMapping("/dowloandJava")
    public ResponseEntity<byte[]> generateJavaClass(@ModelAttribute ClassInfo classInfo) {
        byte[] javaClassBytes = javaClassGenerator.generateJavaClass(classInfo.getClassName(), classInfo.getFields(),
                classInfo.getTableName(), classInfo.getTablePrimaryKey(), classInfo.isLoadMethod(),
                classInfo.isSaveMethod(), classInfo.isUpdateMethod(), classInfo.isDeleteMethod(),
                classInfo.isFindMethod(), classInfo.getFindFields(), classInfo.getFindMethodName());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + classInfo.getClassName() + ".java");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(javaClassBytes);

    }

}
