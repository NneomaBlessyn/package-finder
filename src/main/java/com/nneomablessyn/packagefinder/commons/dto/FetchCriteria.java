package com.nneomablessyn.packagefinder.commons.dto;

import com.nneomablessyn.packagefinder.constants.Constant;
import lombok.Data;

@Data
public class FetchCriteria {
    private int page = Constant.DEFAULT_PAGE_NUM;

    private int size = Constant.DEFAULT_PAGE_SIZE;
}
