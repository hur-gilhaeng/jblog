package com.douzone.jblog.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.CategoryVo;

@Repository
public class CategoryRepository {
	@Autowired
	private SqlSession sqlSession;

	public int insert(CategoryVo vo) {
		return sqlSession.insert("category.insert",vo);
	}

	public List<CategoryVo> getCategoryList(String id) {
		return sqlSession.selectList("category.getCategoryList", id);
	}

	public Long getDefultcategoryNo(String id) {
		return sqlSession.selectOne("category.getDefultcategoryNo", id);
	}

	public List<CategoryVo> getCategoryCountList(String id) {
		return sqlSession.selectList("category.getCategoryCountList", id);
	}

	public int postCount(Long no) {
		return sqlSession.selectOne("category.postCount", no);
	}

	public int delete(Long no) {
		return sqlSession.delete("category.delete", no);
	}

	public int findIdandNo(String id, Long categoryNo) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("categoryNo", categoryNo);
		parameter.put("id", id);
		return sqlSession.selectOne("category.findIdandNo", parameter);
	}
}
