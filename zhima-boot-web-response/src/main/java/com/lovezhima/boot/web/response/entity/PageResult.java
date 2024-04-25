package com.lovezhima.boot.web.response.entity;

import lombok.*;

/**
 * 分页数据
 *
 * @author king on 2023/6/25
 * @since 2023.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> extends BasicResult<T> {

    /**
     * 偏移量
     */
    private Integer offset;

    /**
     * 限制
     */
    private Integer limit;

    /**
     * 当前页
     */
    private Integer currentPage;
    /**
     * 总页数
     */
    private Integer totalPage;
}
