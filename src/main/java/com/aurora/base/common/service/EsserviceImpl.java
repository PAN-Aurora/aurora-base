package com.aurora.base.common.service;

import com.alibaba.fastjson.JSONObject;
import com.aurora.base.common.model.EsPageModel;
import com.aurora.base.common.model.IndexModel;
import com.aurora.base.common.util.StringUtil;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.elasticsearch.common.Table.TIMESTAMP;

@Service
public class EsserviceImpl implements  EsService {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(EsserviceImpl.class);

    @Autowired
    private TransportClient transportClient ;


    /**
     * 创建索引
     *
     * @param index
     * @return
     */
    public  boolean createIndex(String index) {
        if (!isIndexExist(index)) {
            LOGGER.info("索引不存在！");
        }
        CreateIndexResponse indexresponse = transportClient.admin().indices().prepareCreate(index).execute().actionGet();
        LOGGER.info("执行建立成功？" + indexresponse.isAcknowledged());
        return indexresponse.isAcknowledged();
    }


    /**
     * 判断索引是否存在
     *
     * @param index
     * @return
     */
    public  boolean isIndexExist(String index) {
        IndicesExistsResponse inExistsResponse = transportClient.admin().indices().exists(new IndicesExistsRequest(index)).actionGet();
        if (inExistsResponse.isExists()) {
            LOGGER.info("Index [" + index + "] is exist!");
        } else {
            LOGGER.info("Index [" + index + "] is not exist!");
        }
        return inExistsResponse.isExists();
    }

    /**
     * @Description: 判断index下指定type是否存在
     */
    public boolean isTypeExist(String index, String type) {
        return isIndexExist(index)
                ? transportClient.admin().indices().prepareTypesExists(index).setTypes(type).execute().actionGet().isExists()
                : false;
    }

    /**
     * 数据添加，正定ID
     *
     * @param jsonObject 要增加的数据
     * @param index      索引，类似数据库
     * @param type       类型，类似表
     * @param id         数据ID
     * @return
     */
    public  String addData(JSONObject jsonObject, String index, String type, String id) {
        IndexResponse response = transportClient.prepareIndex(index, type, id).setSource(jsonObject).get();
        LOGGER.info("addData response status:{},id:{}", response.status().getStatus(), response.getId());
        return response.getId();
    }

    /**
     * 数据添加
     *
     * @param jsonObject 要增加的数据
     * @param index      索引，类似数据库
     * @param type       类型，类似表
     * @return
     */
    @Override
    public  String addData(JSONObject jsonObject, String index, String type) {
        return addData(jsonObject, index, type, UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
    }

    /**
     * 通过ID删除数据
     *
     * @param index 索引，类似数据库
     * @param type  类型，类似表
     * @param id    数据ID
     */
    @Override
    public  void deleteDataById(String index, String type, String id) {

        DeleteResponse response = transportClient.prepareDelete(index, type, id).execute().actionGet();

        LOGGER.info("deleteDataById response status:{},id:{}", response.status().getStatus(), response.getId());
    }

    /**
     * 根据查询结果删除数据
     * @param index  索引
     * @param list   条件集合
     */
    public  void deleteDataByQuery(String index, List<IndexModel> list) {

        //组装删除过滤条件
        final BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        for(IndexModel model:list){
            if("rangeQuery".equals(model.getSearchType()) && "time".equals(model.getType())){
              final QueryBuilder rangeQueryBuilder = QueryBuilders
                      .rangeQuery(model.getTimeName())
                      .from(model.getTimeStartValue())
                      .to(model.getTimeEndValue());
                boolQueryBuilder.filter(rangeQueryBuilder);
            }

            if("termQuery".equals(model.getSearchType())){
                final QueryBuilder tagTermQuery = QueryBuilders
                        .termQuery(model.getFieldName(), model.getFieldValue());
                boolQueryBuilder.filter(tagTermQuery);
            }

        }

        // 删除请求
        BulkByScrollResponse response = new DeleteByQueryRequestBuilder(transportClient, DeleteByQueryAction.INSTANCE)
                .filter(boolQueryBuilder)
                .source(index)
                .get();
        long deleted = response.getDeleted();

        LOGGER.info("deleteDataById response status:{},id:{}", response.getStatus(), deleted);
    }

    /**
     * 通过ID 更新数据
     *
     * @param jsonObject 要增加的数据
     * @param index      索引，类似数据库
     * @param type       类型，类似表
     * @param id         数据ID
     * @return
     */
    @Override
    public  void updateDataById(JSONObject jsonObject, String index, String type, String id) {

        UpdateRequest updateRequest = new UpdateRequest();

        updateRequest.index(index).type(type).id(id).doc(jsonObject);

        transportClient.update(updateRequest);

    }



    /**
     * 通过ID获取数据
     *
     * @param index  索引，类似数据库
     * @param type   类型，类似表
     * @param id     数据ID
     * @param fields 需要显示的字段，逗号分隔（缺省为全部字段）
     * @return
     */
    @Override
    public Map<String, Object> searchDataById(String index, String type, String id, String fields) {

        GetRequestBuilder getRequestBuilder = transportClient.prepareGet(index, type, id);

        if (StringUtil.isNotEmpty(fields)) {
            getRequestBuilder.setFetchSource(fields.split(","), null);
        }

        GetResponse getResponse = getRequestBuilder.execute().actionGet();

        return getResponse.getSource();
    }


    /**
     * 使用分词查询,并分页
     *
     * @param index          索引名称
     * @param type           类型名称,可传入多个type逗号分隔
     * @param startPage      当前页
     * @param pageSize       每页显示条数
     * @param query          查询条件
     * @param fields         需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField      排序字段
     * @param highlightField 高亮字段
     * @return
     */
    @Override
    public  EsPageModel searchDataPage(
            String index,
            String type,
            int startPage,
            int pageSize,
            QueryBuilder query,
            String fields,
            String sortField,
            String highlightField) {
        SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch(index);
        if (StringUtil.isNotEmpty(type)) {
            searchRequestBuilder.setTypes(type.split(","));
        }
        //搜索类型 默认使用QUERY_THEN_FETCH
        searchRequestBuilder.setSearchType(SearchType.QUERY_THEN_FETCH);

        // 需要显示的字段，逗号分隔（缺省为全部字段）
        if (StringUtil.isNotEmpty(fields)) {
            searchRequestBuilder.setFetchSource(fields.split(","), null);
        }

        //排序字段
        if (StringUtil.isNotEmpty(sortField)) {
            searchRequestBuilder.addSort(sortField, SortOrder.DESC);
        }

        // 高亮（xxx=111,aaa=222）
        if (StringUtil.isNotEmpty(highlightField)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();

            //highlightBuilder.preTags("<span style='color:red' >");//设置前缀
            //highlightBuilder.postTags("</span>");//设置后缀

            // 设置高亮字段
            highlightBuilder.field(highlightField);
            searchRequestBuilder.highlighter(highlightBuilder);
        }

        //searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery());
        if(query!=null) {
            searchRequestBuilder.setQuery(query);
        }
        // 分页应用
        searchRequestBuilder.setFrom(startPage).setSize(pageSize);

        // 设置是否按查询匹配度排序
        searchRequestBuilder.setExplain(true);

        //打印的内容 可以在 Elasticsearch head 和 Kibana  上执行查询
        LOGGER.info("\n{}", searchRequestBuilder);

        // 执行搜索,返回搜索响应信息
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        long totalHits = searchResponse.getHits().totalHits;
        long length = searchResponse.getHits().getHits().length;

        LOGGER.debug("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);

        if (searchResponse.status().getStatus() == 200) {
            // 解析对象
            List<Map<String, Object>> sourceList = setSearchResponse(searchResponse, highlightField);

            return new EsPageModel(startPage, pageSize, (int) totalHits, sourceList);
        }

        return null;

    }
    /**
     * 使用分词查询,并分页
     *
     * @param index          索引名称
     * @param type           类型名称,可传入多个type逗号分隔
     * @param startPage      当前页
     * @param pageSize       每页显示条数
     * @param query          查询条件
     * @return
     */
    @Override
    public EsPageModel searchDataPage(
            String index,
            String type,
            int startPage,
            int pageSize,
            QueryBuilder query) {
        SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch(index);
        if (StringUtil.isNotEmpty(type)) {
            searchRequestBuilder.setTypes(type.split(","));
        }
        //搜索类型 默认使用QUERY_THEN_FETCH
        searchRequestBuilder.setSearchType(SearchType.QUERY_THEN_FETCH);

        if(query!=null) {
            searchRequestBuilder.setQuery(query);
        }
        // 分页应用
        searchRequestBuilder.setFrom(startPage).setSize(pageSize);

        // 设置是否按查询匹配度排序
        searchRequestBuilder.setExplain(true);

        //打印的内容 可以在 Elasticsearch head 和 Kibana  上执行查询
        LOGGER.info("\n{}", searchRequestBuilder);

        // 执行搜索,返回搜索响应信息
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        long totalHits = searchResponse.getHits().totalHits;
        long length = searchResponse.getHits().getHits().length;

        LOGGER.debug("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);

        if (searchResponse.status().getStatus() == 200) {
            // 解析对象
            List<Map<String, Object>> sourceList = setSearchResponse(searchResponse);

            return new EsPageModel(startPage, pageSize, (int) totalHits, sourceList);
        }

        return null;

    }


    /**
     * 使用分词查询
     *
     * @param index          索引名称
     * @param type           类型名称,可传入多个type逗号分隔
     * @param query          查询条件
     * @param size           文档大小限制
     * @param fields         需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField      排序字段
     * @param highlightField 高亮字段
     * @return
     */
    @Override
    public  List<Map<String, Object>> searchListData(
            String index, String type, QueryBuilder query, Integer size,
            String fields, String sortField, String highlightField) {

        SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch(index);
        if (StringUtil.isNotEmpty(type)) {
            searchRequestBuilder.setTypes(type.split(","));
        }

        if (StringUtil.isNotEmpty(highlightField)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            // 设置高亮字段
            highlightBuilder.field(highlightField);
            searchRequestBuilder.highlighter(highlightBuilder);
        }

        searchRequestBuilder.setQuery(query);

        if (StringUtil.isNotEmpty(fields)) {
            searchRequestBuilder.setFetchSource(fields.split(","), null);
        }
        searchRequestBuilder.setFetchSource(true);

        if (StringUtil.isNotEmpty(sortField)) {
            searchRequestBuilder.addSort(sortField, SortOrder.DESC);
        }

        if (size != null && size > 0) {
            searchRequestBuilder.setSize(size);
        }

        //打印的内容 可以在 Elasticsearch head 和 Kibana  上执行查询
        LOGGER.info("\n{}", searchRequestBuilder);

        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        long totalHits = searchResponse.getHits().totalHits;
        long length = searchResponse.getHits().getHits().length;

        LOGGER.info("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);

        if (searchResponse.status().getStatus() == 200) {
            // 解析对象
            return setSearchResponse(searchResponse, highlightField);
        }
        return null;

    }


    /**
     * 高亮结果集 特殊处理
     *
     * @param searchResponse
     * @param highlightField
     */
    private static List<Map<String, Object>> setSearchResponse(SearchResponse searchResponse, String highlightField) {
        List<Map<String, Object>> sourceList = new ArrayList<Map<String, Object>>();
        StringBuffer stringBuffer = new StringBuffer();

        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            searchHit.getSourceAsMap().put("id", searchHit.getId());

            if (StringUtil.isNotEmpty(highlightField)) {

                System.out.println("遍历 高亮结果集，覆盖 正常结果集" + searchHit.getSourceAsMap());
                Text[] text = searchHit.getHighlightFields().get(highlightField).getFragments();

                if (text != null) {
                    for (Text str : text) {
                        stringBuffer.append(str.string());
                    }
                    //遍历 高亮结果集，覆盖 正常结果集
                    searchHit.getSourceAsMap().put(highlightField, stringBuffer.toString());
                }
            }
            sourceList.add(searchHit.getSourceAsMap());
        }
        return sourceList;
    }
    /**
     * 从es结果获取list集合
     * @param searchResponse
     * @return
     */
    private static List<Map<String, Object>> setSearchResponse(SearchResponse searchResponse) {
        List<Map<String, Object>> sourceList = new ArrayList<Map<String, Object>>();
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            searchHit.getSourceAsMap().put("id", searchHit.getId());
            sourceList.add(searchHit.getSourceAsMap());
        }
        return sourceList;
    }

}
