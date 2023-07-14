package com.example.issueMgt.issue;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InMemoryIssueRepository extends CrudRepository<Issue, Long> {
}
