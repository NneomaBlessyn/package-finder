package com.nneomablessyn.packagefinder.commons.utils;


import com.nneomablessyn.packagefinder.constants.Constant;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public final class PaginationUtil {

    private PaginationUtil() {
        throw new IllegalStateException("This class cannot be instantiated");
    }

    public static int getMaxPageLimit(int pageSize) {
        return Math.min(Math.abs(pageSize), Constant.MAX_PAGE_SIZE);
    }

    public static int getPageNum(int pageNum) {
        return Math.max(pageNum, Constant.DEFAULT_PAGE_NUM);
    }

    public static PageRequest getPageRequest(int size, int page) {
        int maxPageLimit = getMaxPageLimit(size);
        int pageNumber = getPageNum(page);
        return PageRequest.of(pageNumber >= 1 ? pageNumber - 1 : pageNumber, maxPageLimit,
                Sort.by(Sort.Direction.DESC, "createdAt"));
    }

}
