package controllers;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import models.Condition;
import models.Project;
import org.apache.commons.lang.StringUtils;
import play.mvc.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guoqingma on 16-11-28.
 */
public class ProjectController extends Controller {

    public static void condition(String projectId) {
        List<Condition> conditionList = Condition.getConditionListByProject(projectId);
        render(conditionList, projectId);
    }

    public static void codeSave(String code, String describe, String projectId, String id) {
        if (StringUtils.isNotEmpty(id)) {
            Condition condition = Condition.findById(id);
            condition.code = code;
            condition.describe = describe;
            condition.save();
        } else if (StringUtils.isNotEmpty(projectId)) {
            Condition.saveCondition(code, describe, projectId);
        }
        condition(projectId);
    }

    public static void delete(String id,String projectId) {
        Condition.deleteById(id);
        condition(projectId);
    }

    public static String getCondition(String id) {
        if (StringUtils.isNotEmpty(id)) {
            return getJsonString((Condition) Condition.findById(id));
        }
        return "";
    }

    public static void project() {
        List<Project> projectList = Project.getProjeList();
        render(projectList);
    }

    public static void save(String name, String describe) {
        Project.projectSave(name, describe);
        project();
    }

    private static String getJsonString(Condition condition) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", String.valueOf(condition.getId()));
        map.put("code", condition.code);
        map.put("describe", condition.describe);
        return new Gson().toJson(map);
    }
}
