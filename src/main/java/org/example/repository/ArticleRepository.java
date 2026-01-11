package org.example.repository;

import jakarta.persistence.Id;
import org.example.model.DataBaseArticle;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<DataBaseArticle, Id> {
}
