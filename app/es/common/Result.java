package es.common;

import com.google.gson.Gson;
import lombok.Data;
import models.es.AggsResult;
import models.es.ProjectDoc;

import java.util.List;

/**
 * TODO {file desc}
 *
 * @author GaoJian
 * @version 0.1
 * @since 16/4/6 上午11:36
 */
@Data
public class Result {
    public static final String HITS = "hits";
    public static final String AGGS = "aggs";
    public static final String TOTAL = "total";

    private boolean ok; // true表示批量全部成功，false 相反
    private String error; // 失败信息

    private List<AggsResult> aggs;//聚合结果

    private List<ProjectDoc> projectDocs;//查询结果


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
