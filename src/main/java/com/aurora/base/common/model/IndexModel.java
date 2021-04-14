package com.aurora.base.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author :PHQ
 * @date：2021/4/13
 **/
@Data
@Builder
public class IndexModel {

    public String type;
    public String searchType;
    public String fieldName;
    public String fieldValue;

    public String timeName;
    public String timeStartValue;
    public String timeEndValue;


}
