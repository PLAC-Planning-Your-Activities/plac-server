package com.plac.domain.plan.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TagListRequest {

    private List<String> tags;

}
