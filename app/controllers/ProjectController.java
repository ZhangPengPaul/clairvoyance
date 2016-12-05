package controllers;

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

    /**
     * 状态码列表
     *
     * @param projectId
     */
    public static void condition(String projectId) {
        if (StringUtils.isNotEmpty(projectId)) {
            List<Condition> conditionList = Condition.getConditionListByProject(projectId);
            Project project = Project.findById(projectId);
            render(conditionList, projectId, project);
        } else {
            redirect("/404");
        }
    }

    /**
     * 状态码修改、保存
     *
     * @param code
     * @param describe
     * @param projectId
     * @param id
     */
    public static void codeSave(String code, String describe, String projectId, String id) {
        if (StringUtils.isNotEmpty(id)) {
            Condition condition = Condition.findById(id);
            condition.code = code;
            condition.describe = describe;
            condition.save();
        } else {
            if (StringUtils.isNotEmpty(projectId) && StringUtils.isNotEmpty(code)) {
                Condition.saveCondition(code, describe, projectId);
            }
        }
        condition(projectId);
    }

    /**
     * 删除状态码
     *
     * @param id
     * @param projectId
     */
    public static void delete(String id, String projectId) {
        if (StringUtils.isNotEmpty(id)) {
            Condition.deleteById(id);
        }
        condition(projectId);
    }

    /**
     * 根据id获取状态码
     *
     * @param id
     * @return
     */
    public static String getCondition(String id) {
        if (StringUtils.isNotEmpty(id)) {
            return getJsonString((Condition) Condition.findById(id));
        }
        return "";
    }

    /**
     * 项目列表
     */
    public static void project() {
        //todo 根据用户获取自己的项目
        List<Project> projectList = Project.getProjeList();
        render(projectList);
    }

    /**
     * 保存项目
     *
     * @param name
     * @param describe
     */
    public static void save(String name, String describe) {
        if (StringUtils.isNotEmpty(name)) {
            Project.projectSave(name, describe);
        }
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
