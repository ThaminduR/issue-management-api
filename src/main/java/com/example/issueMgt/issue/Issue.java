package com.example.issueMgt.issue;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;

public record Issue(@Id Long id,
                   @NotNull(message = "name is required") @Pattern(regexp = "^[a-zA-Z ]+$", message = "name must be a string") String name,
                   @NotNull(message = "description is required") @Pattern(regexp = "^[a-zA-Z ]+$", message = "description must be a string") String description,
                   @NotNull(message = "issue link is required") @URL(message = "Issue link must be a URL") String issueLink) {

    public Issue(
            Long id,
            String name,
            String description,
            String issueLink
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.issueLink = issueLink;
    }

    @Override
    public Long id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public String issueLink() {
        return issueLink;
    }

    public Issue updateWith(Issue item) {
        return new Issue(
                this.id,
                item.name,
                item.description,
                item.issueLink
        );
    }
}

