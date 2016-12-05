package es.common;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.*;
import io.searchbox.core.search.aggregation.TermsAggregation;
import models.es.AggsResult;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author guanghui
 * @version 1.0
 * @since 16/11/25 上午10:05
 */
public class ElasticClient {

    private static Logger logger = LoggerFactory.getLogger(ElasticClient.class);
    private static boolean isMultiThreaded = true;
    private static JestClient client;
    private static ExecutorService executorService = Executors.newFixedThreadPool(16);

    static {
        Set<String> urls = new HashSet<>();
        urls.add("http://localhost:9205");
//        urls.add("http://10.1.21.85:9200");
        JestClientFactory factory = new JestClientFactory();
        HttpClientConfig config = new HttpClientConfig.Builder(urls).maxTotalConnection(100)
                .defaultMaxTotalConnectionPerRoute(1000).multiThreaded(isMultiThreaded).connTimeout(10000)
                .readTimeout(1000000).build();
        factory.setHttpClientConfig(config);
        client = factory.getObject();
    }

    public static JestClient getClient() {
        return client;
    }

    public static void createDocuments(Object docObject, String indexName, String typeName) {
        Index index = new Index.Builder(docObject).index(indexName).type(typeName).build();
        try {
            JestResult result = client.execute(index);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("", e);
        }
    }

    public static void updateDocuments(String docId, String updateScript, String indexName, String typeName) {
        try {
            Update update = new Update.Builder(updateScript).index(indexName).type(typeName).id(docId).build();
            JestResult rs = client.execute(update);
            System.out.println(rs.getJsonString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleltDocuments(String docId, String indexName, String typeName) {
        try {
            client.execute(new Delete.Builder(docId).index(indexName).type(typeName).build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JestResult getDocument(String docId, String indexName, String typeName) {
        JestResult result = null;
        try {
            result = client.execute(new Get.Builder(indexName, docId).type(typeName).build());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static JsonObject searchByAggs(String aggsField, int pageNum, int pageSize, String indexName, String typeName) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        String query = "";
//        String project = "";
//        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//        boolQuery.must((QueryBuilders.matchQuery("code", query)));
//        boolQuery.must(QueryBuilders.matchQuery("project", project));
//        searchSourceBuilder.query(boolQuery);

        AggregationBuilder aggregation = AggregationBuilders.terms(Result.AGGS).field(aggsField).size(20);
        searchSourceBuilder.aggregation(aggregation);
        int from = 0;
        if (pageNum > 1) {
            from = (pageNum - 1) * pageSize;
        }
        searchSourceBuilder.size(pageSize);
        searchSourceBuilder.from(from);

        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(indexName).addType(typeName).build();

        SearchResult result = ElasticClient.getClient().execute(search);
        if (result.isSucceeded()) {
            JsonArray asJsonArray = result.getJsonObject().getAsJsonObject(Result.HITS).getAsJsonArray(Result.HITS);
            JsonObject resultJson = new JsonObject();

            resultJson.add("result", asJsonArray);
            resultJson.addProperty("total", result.getJsonObject().getAsJsonObject(Result.HITS).get(Result.TOTAL).getAsInt());

            TermsAggregation terms = result.getAggregations().getTermsAggregation(Result.AGGS);
            StringBuilder aggsResult = new StringBuilder();
            Gson gson = new Gson();
            JsonArray aggsArray = new JsonArray();
            for (TermsAggregation.Entry e : terms.getBuckets()) {
                AggsResult aggsPo = new AggsResult();
                aggsPo.setProjectName(e.getKey());
                aggsPo.setDocCount(e.getCount());
                aggsArray.add(gson.toJsonTree(aggsPo));
            }

            resultJson.add("aggs", aggsArray);
            return resultJson;
        }
        return null;
    }

    public static String explain(SearchSourceBuilder builder, String id, String index, String type) {
        Explain search = new Explain.Builder(index, type, id, builder.toString()).build();
        JestResult result;
        try {
            result = client.execute(search);
        } catch (IOException e) {
            logger.error("Execute explain error.", e);
            throw new RuntimeException("Execute explain error.", e);
        }
        if (!result.isSucceeded()) {
            logger.error("Execute explain failure.");
            throw new RuntimeException(result.getErrorMessage());
        }
        return result.getJsonObject().toString();
    }
}
