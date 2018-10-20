package com.turingoal.mengbao.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * 收藏Bean
 */
@Data
public class CollectBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title; // title
}
