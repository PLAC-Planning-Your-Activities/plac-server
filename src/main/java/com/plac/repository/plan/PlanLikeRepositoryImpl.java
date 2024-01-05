package com.plac.repository.plan;

import com.plac.domain.plan.PlanLike;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class PlanLikeRepositoryImpl implements PlanLikeRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public PlanLikeRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<PlanLike> findPlanLikesByUserIdWithPaging(Long userId, String beforePlanId, int pageSize) {
        Query query = new Query();
        Criteria criteria = Criteria.where("userId").is(userId);

        if (beforePlanId != null) {
            PlanLike beforePlanLike = mongoTemplate.findOne(
                    new Query(Criteria.where("planId").is(beforePlanId).andOperator(criteria))
                            .with(Sort.by(Sort.Direction.DESC, "_id"))
                            .limit(1),
                    PlanLike.class
            );

            if (beforePlanLike != null) {
                query.addCriteria(Criteria.where("_id").lt(new ObjectId(beforePlanLike.getId())));
            }
        }

        query.addCriteria(criteria);
        query.with(Sort.by(Sort.Direction.DESC, "_id"));
        query.limit(pageSize);

        return mongoTemplate.find(query, PlanLike.class);
    }

}
