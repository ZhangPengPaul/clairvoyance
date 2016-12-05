package models.es;

import io.searchbox.annotations.JestId;
import lombok.Data;

/******************************************
 * @author: 释然 (shiran@51huxin.com)
 * @createDate: 16-12-5
 * @company: (C) Copyright 阳光互信 2016
 * @since: JDK 1.8
 * @Description: 注释写这里
 ******************************************/
@Data
public class ProjectDoc {
    @JestId
    private String id;
    public String project;//项目
    public String code;//状态码
    public String describe;//描述
}
