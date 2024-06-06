package com.underwaterimageprocessor.imageprocessor.repository;

import com.underwaterimageprocessor.imageprocessor.model.OrgImage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends MongoRepository<OrgImage, ObjectId> {
}
