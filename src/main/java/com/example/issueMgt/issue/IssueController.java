package com.example.issueMgt.issue;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/issues")
@EnableMethodSecurity
public class IssueController {

    private final IssueService service;
    public IssueController(IssueService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('issues:view')")
    public ResponseEntity<List<Issue>> findAll() {
        List<Issue> items = service.findAll();
        return ResponseEntity.ok().body(items);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('issues:view')")
    public ResponseEntity<Issue> find(@PathVariable("id") Long id) {
        Optional<Issue> issue = service.find(id);
        return ResponseEntity.of(issue);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('issues:create')")
    public ResponseEntity<Issue> create(@Valid @RequestBody Issue issue) {
        Issue created = service.create(issue);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('issues:update')")
    public ResponseEntity<Issue> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody Issue updatedItem) {

        Optional<Issue> updated = service.update(id, updatedItem);

        return updated
                .map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> {
                    Issue created = service.create(updatedItem);
                    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(created.id())
                            .toUri();
                    return ResponseEntity.created(location).body(created);
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('issues:delete')")
    public ResponseEntity<Issue> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        Map<String, String> map = new HashMap<>(errors.size());
        errors.forEach((error) -> {
            String key = ((FieldError) error).getField();
            String val = error.getDefaultMessage();
            map.put(key, val);
        });
        return ResponseEntity.badRequest().body(map);
    }
}
