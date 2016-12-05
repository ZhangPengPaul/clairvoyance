package es.po;

import com.google.gson.annotations.Expose;
import io.searchbox.annotations.JestId;
import lombok.Data;

import java.util.Date;

/******************************************
 * @author: 释然 (shiran@51huxin.com)
 * @createDate: 16-11-25
 * @company: (C) Copyright 阳光互信 2016
 * @since: JDK 1.8
 * @Description: 注释写这里
 ******************************************/
@Data
public class TestPo {
    @JestId
    private String id;
    private String code;
    private String project;
    private String message;
    @Expose
    private Date ctime;
}
