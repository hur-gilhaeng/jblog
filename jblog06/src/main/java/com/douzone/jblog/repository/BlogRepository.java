package com.douzone.jblog.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.BlogVo;

@Repository
public class BlogRepository {
	@Autowired
	private SqlSession sqlSession;

	public int insert(BlogVo vo) {
		return sqlSession.insert("blog.insert",vo);
	}

	public int findId(String id) {
		return sqlSession.selectOne("blog.findId",id);
	}

	public BlogVo findOfId(String id) {
		return sqlSession.selectOne("blog.findOfId",id);
	}

	public int updateBlog(BlogVo blogVo) {
		return sqlSession.update("blog.update",blogVo);
	}
}
