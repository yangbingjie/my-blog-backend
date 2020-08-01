package cn.edu.tongji.myblogbackend.service;

import cn.edu.tongji.myblogbackend.dao.*;
import cn.edu.tongji.myblogbackend.entity.*;
import cn.edu.tongji.myblogbackend.utils.TimeUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.common.collect.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;


@Service
public class ArticleService {
    @Autowired
    ArticleDAO articleDAO;
    @Autowired
    FileService fileService;
    @Autowired
    LikeArticleDAO likeArticleDAO;
    @Autowired
    StarArticleDAO starArticleDAO;
    @Autowired
    ChooseTagDAO chooseTagDAO;
    @Autowired
    TagDao tagDao;
    @Autowired
    UserService userService;

    public JSONObject searchArticle(String searchType, String query, String userId){
        JSONObject result = new JSONObject();
        JSONArray articleList = new JSONArray();
        Map<String, JSONObject> allTagMap = new TreeMap<String, JSONObject>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        switch (searchType){
            case "all":
                list = articleDAO.searchByAll("%"+query+"%");
                break;
            case "title":
                list = articleDAO.searchByTitle("%"+query+"%");
                break;
            case "author_name":
                list = articleDAO.searchByAuthorName("%"+query+"%");
                break;
            case "arthor_id":
                list = articleDAO.searchByAuthorId(query);
                break;
        }
        for (int i = 0; i < list.size(); ++i){
            JSONObject article = new JSONObject();
            String articleId = (String)list.get(i).get("article_id");
            article.put("article_id", articleId);
            JSONArray tagList = getTagList(articleId);
            JSONObject jo;
            String tagName;
            for (int j = 0; j < tagList.size(); j++) {
                jo = (JSONObject) tagList.get(j);
                tagName = jo.getString("tag_name");
                if (allTagMap.containsKey(tagName)){
                    jo = allTagMap.get(tagName);
                    jo.put("count", 1 + (int)jo.get("count"));
                    allTagMap.put(tagName, jo);
                }else{
                    jo.put("count", 1);
                    allTagMap.put(tagName, jo);
                }
            }
            article.put("tag_list", tagList);
            String authorId = (String) list.get(i).get("author_id");
            UserEntity user = userService.getByUserId(authorId);
            article.put("author_name", user.getUsername());
            article.put("author_avatar", user.getAvatar());
            article.put("title", list.get(i).get("title"));
            article.put("view_count", list.get(i).get("view_count"));
            article.put("like_count", getLikeCount(articleId));
            article.put("star_count", getStarCount(articleId));
            article.put("is_like", isLike(articleId, userId));
            article.put("is_star", isStar(articleId, userId));
            article.put("update_time", TimeUtils.format((Timestamp)list.get(i).get("update_time")));
            article.put("cover", list.get(i).get("cover"));
            article.put("preview", list.get(i).get("preview"));
            articleList.add(article);
        }

        List<Map.Entry<String,JSONObject>> li = new ArrayList<Map.Entry<String,JSONObject>>(allTagMap.entrySet());

        Collections.sort(li,new Comparator<Map.Entry<String,JSONObject>>() {
            @Override
            public int compare(Map.Entry<String, JSONObject> o1, Map.Entry<String, JSONObject> o2) {
                Integer n1 = (int) o1.getValue().get("count");
                Integer n2 = (int) o2.getValue().get("count");
                return n2.compareTo(n1);
            }

        });

        result.put("article_list", articleList);
        JSONArray allTagList = new JSONArray();
        for (int i = 0; i < li.size(); i++) {
            allTagList.add(li.get(i).getValue());
        }
        result.put("all_tag_list", allTagList);
        return result;
    }

    public Long getLikeCount(String articleId){
        return likeArticleDAO.countByAndArticleId(articleId);
    }
    public Long getStarCount(String articleId){
        return starArticleDAO.countByAndArticleId(articleId);
    }
    public LikeArticleEntity isLike(String articleId, String userId){return likeArticleDAO.getByArticleIdAndUserId(articleId, userId);}
    public StarArticleEntity isStar(String articleId, String userId){return starArticleDAO.getByArticleIdAndUserId(articleId, userId);}
    public Tuple<Long, Boolean> likeArticle(String articleId, String userId){
        ArticleEntity articleEntity = checkArticleAccess(articleId, userId);
        if (articleEntity != null){
            LikeArticleEntity likeArticleEntity = isLike(articleId, userId);
            if (likeArticleEntity == null){
                Timestamp time = new Timestamp(System.currentTimeMillis());
                likeArticleEntity = new LikeArticleEntity();
                likeArticleEntity.setArticleId(articleId);
                likeArticleEntity.setUserId(userId);
                likeArticleEntity.setLikeTime(time);
                likeArticleDAO.save(likeArticleEntity);
                return new Tuple<Long, Boolean>(likeArticleDAO.countByAndArticleId(articleId), true);
            }else{
                likeArticleDAO.delete(likeArticleEntity);
                return new Tuple<Long, Boolean>(likeArticleDAO.countByAndArticleId(articleId), false);
            }
        }else{
            return new Tuple<Long, Boolean>((long) -1, false);
        }
    }
    public Tuple<Long, Boolean> starArticle(String articleId, String userId){
        ArticleEntity articleEntity = checkArticleAccess(articleId, userId);
        if (articleEntity != null) {
            StarArticleEntity starArticleEntity = isStar(articleId, userId);
            if (starArticleEntity == null){
                Timestamp time = new Timestamp(System.currentTimeMillis());
                starArticleEntity = new StarArticleEntity();
                starArticleEntity.setArticleId(articleId);
                starArticleEntity.setUserId(userId);
                starArticleEntity.setStarTime(time);
                starArticleDAO.save(starArticleEntity);
                return new Tuple<Long, Boolean>(starArticleDAO.countByAndArticleId(articleId), true);
            }else{
                starArticleDAO.delete(starArticleEntity);
                return new Tuple<Long, Boolean>(starArticleDAO.countByAndArticleId(articleId), false);
            }
        }else{
            return new Tuple<Long, Boolean>((long) -1, false);
        }
    }
    public boolean isExist(String articleId){
        Optional<ArticleEntity> articleEntity = articleDAO.findById(articleId);
        return articleEntity.isPresent();
    }
    public boolean isAuthor(String articleId, String userId){
        Optional<ArticleEntity> articleEntity = articleDAO.findById(articleId);
        if (articleEntity.isPresent()){
            String authorId = articleEntity.get().getAuthorId();
            return authorId.equals(userId);
        }
        return true;
    }

    public void updateTagList(JSONArray newTagList, String articleId){
        // newTagList: [{"tag_id":null, "tag_name":"C++"}, {"tag_id":"323dfs", "tag_name":"C--"}]
        List<ChooseTagEntity> oldTagList = chooseTagDAO.getByArticleId(articleId);
        Set<String> set = new HashSet<String>();
        List<String> list = new ArrayList<String>();

        // 遍历newTagList
        // tag_id为null的tag_name存入一个List<String>
        // tag_id不为null的tag_id存入一个Map<String, String>
        for (int i = 0; i < newTagList.size(); i++) {
            JSONObject object = newTagList.getJSONObject(i);
            if (object.getString("tag_id") == null){
                list.add(object.getString("tag_name"));
            }else{
                set.add(object.getString("tag_id"));
            }
        }

        // 遍历List<String>，在tag表里查找tag_name，
        // 存在返回tagEntity
        // 不存在则在chooseTag表里新建一行，然后返回tagEntity
        // 用返回的tagEntity的tag_id和articleId在chooseTag表里新建一行
        for (int i = 0; i < list.size(); i++) {
            TagEntity tagEntity = tagDao.getByTagName(list.get(i));
            if (tagEntity == null){
                tagEntity = new TagEntity();
                tagEntity.setTagName(list.get(i));
                tagDao.save(tagEntity);
            }
            ChooseTagEntity chooseTagEntity = new ChooseTagEntity();
            chooseTagEntity.setArticleId(articleId);
            chooseTagEntity.setTagId(tagEntity.getTagId());
            chooseTagDAO.save(chooseTagEntity);
        }

        // 遍历oldTagList，如果在Set<String>找不到相同的tag_id就删除chooseTag
        for (int i = 0; i < oldTagList.size(); i++) {
            if (!set.contains(oldTagList.get(i).getTagId())){
                ChooseTagEntity chooseTagEntity =
                        chooseTagDAO.getByArticleIdAndTagId(articleId, oldTagList.get(i).getTagId());
                chooseTagDAO.delete(chooseTagEntity);
            }
        }
    }
    public JSONArray getTagList(String articleId){
        //[{"tag_id":null, "tag_name":"C++"}, {"tag_id":"323dfs", "tag_name":"C--"}]
        JSONArray jsonArray = new JSONArray();
        List<Map<String, String>> list = chooseTagDAO.getTagList(articleId);
        for (int i = 0; i < list.size(); i++) {
            JSONObject jo = new JSONObject();
            jo.put("tag_id", list.get(i).get("tag_id"));
            jo.put("tag_name", list.get(i).get("tag_name"));
            jsonArray.add(jo);
        }
        return jsonArray;
    }
    public String saveArticle(JSONObject jsonObject){
        ArticleEntity articleEntity = new ArticleEntity();
        Timestamp time = new Timestamp(System.currentTimeMillis());
        articleEntity.setAuthorId(jsonObject.getString("author_id"));
        articleEntity.setTitle(jsonObject.getString("title"));
        articleEntity.setContentHtml(jsonObject.getString("content_html"));
        articleEntity.setContentMarkdown(jsonObject.getString("content_markdown"));
        articleEntity.setIsPublic(jsonObject.getInteger("is_public"));
        articleEntity.setPreview(jsonObject.getString("preview"));
        articleEntity.setCover(jsonObject.getString("cover"));
        String folder = jsonObject.getString("img_folder");
        articleEntity.setImgFolder(folder);
        if (jsonObject.getString("article_id") == null) {
            articleEntity.setCreateTime(time);
            articleEntity.setUpdateTime(time);
            articleEntity.setViewCount(0);
            articleDAO.save(articleEntity);
        } else {
            articleEntity.setUpdateTime(time);
            articleEntity.setArticleId(jsonObject.getString("article_id"));
            articleDAO.updateArticle(articleEntity.getTitle(),
                    articleEntity.getContentHtml(), articleEntity.getContentMarkdown(),
                    articleEntity.getPreview(), articleEntity.getIsPublic(),
                    articleEntity.getUpdateTime(),
                    articleEntity.getCover(),articleEntity.getArticleId());
        }
        JSONArray tag_array = jsonObject.getJSONArray("tag_list");
        updateTagList(tag_array, articleEntity.getArticleId());
        JSONArray img_array = jsonObject.getJSONArray("img_list");
        fileService.removeUnusedArticleImg(img_array, folder);
        return articleEntity.getArticleId();
    }

    public ArticleEntity checkArticleAccess(String articleId, String userId){
        Optional<ArticleEntity> articleEntityOptional = articleDAO.findById(articleId);
        if (articleEntityOptional.isPresent()){
            ArticleEntity articleEntity = articleEntityOptional.get();
            if (articleEntity.getIsPublic() == 1 || articleEntity.getAuthorId().equals(userId)){
                return articleEntity;
            }
        }
        return null;
    }
    public Integer editAuth(String articleId) {
        Optional<ArticleEntity> articleEntityOptional = articleDAO.findById(articleId);
        if (articleEntityOptional.isPresent()){
            ArticleEntity articleEntity = articleEntityOptional.get();
            Integer isPublic = articleEntity.getIsPublic();
            if (isPublic == 1){
                isPublic = 0;
            }else if(isPublic == 0){
                isPublic = 1;
            }
            articleEntity.setIsPublic(isPublic);
            articleDAO.save(articleEntity);
            return isPublic;
        }
        return -1;
    }
    public ArticleEntity getArticleDetails(String articleId, String userId){
        ArticleEntity articleEntity = checkArticleAccess(articleId, userId);
        if (articleEntity != null){
            articleEntity.setViewCount(articleEntity.getViewCount() + 1);
            articleDAO.save(articleEntity);
            return articleEntity;
        }
        return null;
    }
}
