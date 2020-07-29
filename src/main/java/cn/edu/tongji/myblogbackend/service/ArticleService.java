package cn.edu.tongji.myblogbackend.service;

import cn.edu.tongji.myblogbackend.dao.*;
import cn.edu.tongji.myblogbackend.entity.*;
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
