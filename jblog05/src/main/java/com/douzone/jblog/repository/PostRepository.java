package com.douzone.jblog.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.PostVo;

@Repository
public class PostRepository {
	
	@Autowired
	private SqlSession sqlSession;

	public List<PostVo> getPostList(String id, Long categoryNo) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("id", id);
		parameter.put("categoryNo", categoryNo);
		return sqlSession.selectList("post.getPostList", parameter);
	}

	public PostVo getPost(Long categoryNo, Long postNo) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("categoryNo", categoryNo);
		parameter.put("postNo", postNo);
		return sqlSession.selectOne("post.getPost",parameter);
	}

	public Long getDefultPostNo(Long categoryNo) {
		return sqlSession.selectOne("post.getDefultPostNo", categoryNo);
	}

	public int insert(PostVo postVo) {
		return sqlSession.insert("post.insert",postVo);
	}
	
}
