package es.po;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/******************************************
 * @author: 释然 (shiran@51huxin.com)
 * @createDate: 16-11-30
 * @company: (C) Copyright 阳光互信 2016
 * @since: JDK 1.8
 * @Description: 注释写这里
 ******************************************/
@Data
public class AggsPo {
    @SerializedName("key")
    private String projectName;
    @SerializedName("doc_count")
    private Long docCount;
}
