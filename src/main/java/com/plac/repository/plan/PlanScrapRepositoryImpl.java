package com.plac.repository.plan;

import com.plac.domain.plan.PlanLike;
import com.plac.domain.plan.PlanScrap;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class PlanScrapRepositoryImpl implements PlanScrapRepositoryCutom {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public PlanScrapRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<PlanScrap> findPlanLikesByUserIdWithPaging(Long userId, String beforePlanId, int pageSize) {
        Query query = new Query();
        Criteria criteria = Criteria.where("userId").is(userId);

        if (beforePlanId != null) {
            PlanLike beforePlanScrap = mongoTemplate.findOne(
                    new Query(Criteria.where("planId").is(beforePlanId).andOperator(criteria))
                            .with(Sort.by(Sort.Direction.DESC, "_id"))
                            .limit(1),
                    PlanLike.class
            );

            if (beforePlanScrap != null) {
                query.addCriteria(Criteria.where("_id").lt(new ObjectId(beforePlanScrap.getId())));
            }
        }

        query.addCriteria(criteria);
        query.with(Sort.by(Sort.Direction.DESC, "_id")); // ID 기준 내림차순 정렬
        query.limit(pageSize);

        return mongoTemplate.find(query, PlanScrap.class);
    }
}
