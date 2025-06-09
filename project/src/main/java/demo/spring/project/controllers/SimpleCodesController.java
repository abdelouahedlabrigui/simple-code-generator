package demo.spring.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.spring.project.models.CodeMetadata;
import demo.spring.project.models.SimpleCode;
import demo.spring.project.repositories.CodeMetadataRepository;
import demo.spring.project.repositories.SimpleCodeRepository;

@RestController
@RequestMapping("/api/codes")
public class SimpleCodesController {
    @Autowired
    private SimpleCodeRepository simpleCodeRepository;
    @Autowired
    private CodeMetadataRepository codeMetadataRepository;

    @PostMapping("post-simple-code")
    public ResponseEntity<String> postSimpleCode(@RequestBody SimpleCode simpleCode){
        try {
            simpleCode.setId(null);
            simpleCodeRepository.save(simpleCode);
            return ResponseEntity.ok("saved data");            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("get-simple-code")
    public ResponseEntity<List<SimpleCode>> getSimpleCode(){
        try {
            List<SimpleCode> simpleCodes = simpleCodeRepository.getLimitDesc();
            if (simpleCodes.size() > 0) {
                return ResponseEntity.ok(simpleCodes);
            } else {
                return ResponseEntity.noContent().build();
            }            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("get-simple-code-by-classname")
    public ResponseEntity<List<SimpleCode>> getSimpleCodeByClassName(@RequestParam String classname){
        try {
            List<SimpleCode> simpleCodes = simpleCodeRepository.searchCodeByClassNameLike(classname);
            if (simpleCodes.size() > 0) {
                return ResponseEntity.ok(simpleCodes);
            } else {
                return ResponseEntity.noContent().build();
            }            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("post-code-metadata")
    public ResponseEntity<String> postCodeMetadata(@RequestBody CodeMetadata codeMetadata){
        try {
            codeMetadata.setId(null);
            codeMetadataRepository.save(codeMetadata);
            return ResponseEntity.ok("added data.");
                        
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("get-code-metadata")
    public ResponseEntity<List<CodeMetadata>> getCodeMetadata(){
        try {
            List<CodeMetadata> simpleCodes = codeMetadataRepository.getLimitDesc();
            if (simpleCodes.size() > 0) {
                return ResponseEntity.ok(simpleCodes);
            } else {
                return ResponseEntity.noContent().build();
            }            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("get-code-metadata-by-entity")
    public ResponseEntity<List<CodeMetadata>> getCodeMetadataByEntity(@RequestParam String entity){
        try {
            List<CodeMetadata> simpleCodes = codeMetadataRepository.searchCodeMetadataByEntityNameLike(entity);
            if (simpleCodes.size() > 0) {
                return ResponseEntity.ok(simpleCodes);
            } else {
                return ResponseEntity.noContent().build();
            }            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
