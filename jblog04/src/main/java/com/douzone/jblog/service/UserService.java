package com.douzone.jblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.jblog.repository.BlogRepository;
import com.douzone.jblog.repository.CategoryRepository;
import com.douzone.jblog.repository.UserRepository;
import com.douzone.jblog.vo.UserVo;
import com.douzone.jblog.vo.BlogVo;
import com.douzone.jblog.vo.CategoryVo;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BlogRepository blogRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	public Boolean join(UserVo userVo) {
		
		BlogVo blogVo = blogInsert(userVo.getId());
		CategoryVo categoryVo = categoryInsert(userVo.getId());
		
		Boolean result = false;
		result = 1==userRepository.insert(userVo);
		result = 1==blogRepository.insert(blogVo);
		result = 1==categoryRepository.insert(categoryVo);
		
		return result;
	}

	private CategoryVo categoryInsert(String id) {
		CategoryVo vo = new CategoryVo();
		vo.setId(id);
		vo.setName("미분류");
		vo.setDescription("-기본 카테고리");
		return vo;
	}

	private BlogVo blogInsert(String id) {
		BlogVo vo = new BlogVo();
		vo.setId(id);
		vo.setTitle(id+"님의 블로그.");
		vo.setLogo("images/spring-logo.jpg");
		return vo;
	}

	public UserVo getUser(UserVo vo) {
		return userRepository.findByIdAndPassword(vo);
	}
	
}
