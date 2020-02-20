package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Toto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Toto} entity.
 */
public interface TotoSearchRepository extends ElasticsearchRepository<Toto, String> {
}
