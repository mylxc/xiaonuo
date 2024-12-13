package com.jackli.common.page;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jackli.common.util.LongJsonSerializer;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = -1L;
    protected List<T> records;
    @JsonSerialize(using = LongJsonSerializer.class)
    protected long total;
    @JsonSerialize(using = LongJsonSerializer.class)
    protected long size;
    @JsonSerialize(using = LongJsonSerializer.class)
    protected long current;
    @JsonSerialize(using = LongJsonSerializer.class)
    protected  long pages;
    protected List<OrderItem> orders;

    /**
     * 将mybatis-plus的page转成自定义的PageResult，扩展了totalPage总页数，和rainBow彩虹条
     *
     * @author lijinchang
     * @date 2020/4/8 19:20
     */
    public PageResult(Page<T> page) {
        this.setRecords(page.getRecords());
        this.setTotal(Convert.toLong(page.getTotal()));
        this.setCurrent(Convert.toInt(page.getCurrent()));
        this.setSize(Convert.toInt(page.getSize()));
        this.setPages(PageUtil.totalPage(Convert.toInt(page.getTotal()),
                Convert.toInt(page.getSize())));
        this.setOrders(page.getOrders());
//        this.setRainbow(PageUtil.rainbow(Convert.toInt(page.getCurrent()),
//                Convert.toInt(this.getTotalPage()), RAINBOW_NUM));
    }
}
