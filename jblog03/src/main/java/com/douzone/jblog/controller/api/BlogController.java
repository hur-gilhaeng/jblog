package com.douzone.jblog.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.douzone.jblog.dto.JsonResult;
import com.douzone.jblog.service.BlogService;
import com.douzone.jblog.vo.CategoryVo;


@RestController("BlogApiController")
@RequestMapping("/{id:(?!assets).*}/api")
public class BlogController {
	
	@Autowired
	private BlogService blogService;
	
	@GetMapping("/admin/category/list")
	public JsonResult categoryList(@PathVariable String id) {
		List<CategoryVo> list = blogService.blogCategory(id);
		return JsonResult.success(list);
	}
	
	@PostMapping("/admin/category/add")
	public JsonResult categoryAdd(@PathVariable String id,@RequestBody CategoryVo vo) {
		vo.setId(id);
		blogService.insertCategoryVo(vo);
		vo.setCountPost(0);
		return JsonResult.success(vo);
	}
	
	@DeleteMapping("/admin/category/delete/{no}")
	public JsonResult categoryDelete(
		@PathVariable("no") Long no) {
		boolean result = blogService.deleteCategoryVo(no);
		System.out.println(result);
		return JsonResult.success(result ? no : -1);
	}
}

