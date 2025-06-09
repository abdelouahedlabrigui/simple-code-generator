package demo.spring.project.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.spring.project.models.CountryCode;
import demo.spring.project.repositories.CountryCodeRepository;
@RestController
@RequestMapping("/api/country_code")
public class CountryCodeController {
    @Autowired
    private CountryCodeRepository repository;

    @PostMapping("/add")
    public ResponseEntity<CountryCode> createRecord(@RequestBody CountryCode index) {
        try {
            index.setOriginal_id(null);
            CountryCode savedQuery = repository.save(index);
            return ResponseEntity.ok(savedQuery);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<CountryCode>> getAll(){
        try {
            List<CountryCode> queries = repository.findAll();
            if (queries.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(queries);            
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<CountryCode> get(@RequestParam Long id){
        try {
            if (id == null) {
                return ResponseEntity.notFound().build();
            } else {
                return repository.findById(id)
                        .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                        .orElseGet(() -> {
                            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                        });            
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}