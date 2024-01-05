package com.plac.repository.plan;

import com.plac.domain.plan.Plan;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class PlanRepositoryImpl implements PlanRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public PlanRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Plan> findPlansByUserIdWithPaging(Long userId, String beforePlanId, int pageSize) {
        Query query = new Query();
        Criteria criteria = Criteria.where("userId").is(userId);

        if (beforePlanId != null) {
            query.addCriteria(Criteria.where("_id").lt(new ObjectId(beforePlanId)));
        }
        query.with(Sort.by(Sort.Direction.DESC, "_id"));
        query.limit(pageSize);

        query.addCriteria(criteria);
        return mongoTemplate.find(query, Plan.class);
    }

}
