package com.sage.prometheus.poc;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SummaryRepository extends MongoRepository<Summary, String>{}
