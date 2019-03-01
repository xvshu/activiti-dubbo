package com.eloancn.framework.activiti.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 分页工具
 *
 * @author xvshu
 */
public class PageUtil {

    public static int PAGE_SIZE = 10;

    public static int[] init(Page<?> page, HttpServletRequest request) {
        int pageNumber = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("p"), "1"));
        page.setPageNo(pageNumber);
        int pageSize = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("ps"), String.valueOf(PAGE_SIZE)));
        page.setPageSize(pageSize);
        int firstResult = page.getFirst() - 1;
        int maxResults = page.getPageSize();
        return new int[]{firstResult, maxResults};
    }

    public static List page(List all,int pageNo ,int pagesize){
        pageNo=pageNo-1;
        Integer fromIndex = pageNo * pagesize;
        int totalCount = all.size();
        //如果总数少于PAGE_SIZE,为了防止数组越界,toIndex直接使用totalCount即可
        int toIndex = Math.min(totalCount, (pageNo + 1) * pagesize);
        List<Long> subList = all.subList(fromIndex, toIndex);
        return subList;

    }

}
