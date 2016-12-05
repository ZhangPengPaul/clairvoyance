package es;

import com.google.gson.*;
import es.common.ElasticClient;
import es.po.AggsPo;
import es.po.TestPo;
import io.searchbox.client.JestResult;
import org.junit.Test;
import play.test.UnitTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TestOptionES extends UnitTest {

    @Test
    public void testInsert() {
        for (int i = 0; i < 20; i++) {
            TestPo test = new TestPo();
            test.setId(i + "");
            test.setCode("000" + i);
            test.setMessage("testMessage=" + i);
            test.setCtime(new Date());
            if (i % 3 == 0) {
                test.setProject("Project0");
            } else if (i % 3 == 1) {
                test.setProject("Project1");
            } else {
                test.setProject("Project2");
            }
            ElasticClient.createDocuments(test, "test_index", "test");
        }
    }

    @Test
    public void testUpdate() {
        String updateScript = "{\"doc\":{\"message\":\"new_message\"}}";

        ElasticClient.updateDocuments("testId", updateScript, "test_index", "test");
    }

    @Test
    public void testDelete() {
        ElasticClient.deleltDocuments("testId", "test_index", "test");
    }

    @Test
    public void testGet() {
        JestResult result = ElasticClient.getDocument("testId", "test_index", "test");
        TestPo po = result.getSourceAsObject(TestPo.class);
        if (po != null) {
            System.out.println(po.toString());
        }
    }


    @Test
    public void testsearchAggs() {
        try {
            ExclusionStrategy myExclusionStrategy = new ExclusionStrategy() {

                public boolean shouldSkipField(FieldAttributes fa) {
                    return fa.getName().equalsIgnoreCase("ctime");
                }

                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }

            };

            Gson gson = new GsonBuilder().setExclusionStrategies(myExclusionStrategy).create();

            JsonObject result = ElasticClient.searchByAggs("project", 1, 10, "test_index", "test");

            JsonArray asJsonArray = (JsonArray) result.get("result");
            List<TestPo> poList = new ArrayList<>();
            JsonObject json;
            JsonObject source;
            for (JsonElement element : asJsonArray) {
                source = element.getAsJsonObject().getAsJsonObject("_source");
                TestPo po = gson.fromJson(source, TestPo.class);
                poList.add(po);
            }

            List<AggsPo> aggsPoList = new ArrayList<>();
            JsonArray aggsJsonArray = (JsonArray) result.get("aggs");
            for (JsonElement element : aggsJsonArray) {
                AggsPo aggsPo = gson.fromJson(element, AggsPo.class);
                aggsPoList.add(aggsPo);
            }

            System.out.println(11);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
