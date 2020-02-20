package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Toto;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Toto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TotoRepository extends MongoRepository<Toto, String> {

}
