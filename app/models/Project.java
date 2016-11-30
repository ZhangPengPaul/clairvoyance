package models;

import com.google.code.morphia.annotations.Entity;
import play.modules.morphia.Model;

import java.util.Date;
import java.util.List;

/**
 * Created by guoqingma on 16-11-30.
 * 项目
 */

@Entity
public class Project extends Model {


    public String name;  //项目名
    public String describe; //描述
    public Date createDate;

    //public User user;

    public Project(String name, String describe) {
        this.name = name;
        this.describe = describe;
        this.createDate = new Date();
    }

    public static void projectSave(String name, String describe) {
        Project project = new Project(name, describe);
        project.save();
    }

    public static List<Project> getProjeList() {
        return Project.findAll();
    }

}
