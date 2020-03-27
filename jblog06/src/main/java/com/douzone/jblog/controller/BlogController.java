package com.douzone.jblog.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.jblog.service.BlogService;
import com.douzone.jblog.vo.BlogVo;
import com.douzone.jblog.vo.CategoryVo;
import com.douzone.jblog.vo.PostVo;
import com.douzone.jblog.vo.UserVo;
import com.douzone.security.AuthUser;

@Controller
@RequestMapping( "/{id:(?!assets).*}" )
public class BlogController {
	@Autowired
	private BlogService blogService;

	@RequestMapping( {"", "/{pathNo1}", "/{pathNo1}/{pathNo2}" } )
	public String blogMain(
			@PathVariable String id,
			@PathVariable Optional<Long> pathNo1,
			@PathVariable Optional<Long> pathNo2,
			ModelMap modelMap ) {

		if(blogService.findId(id)) {
			return "redirect:/";
		}

		Long categoryNo = 0L;
		Long postNo = 0L;

		if( pathNo2.isPresent() ) {
			postNo = pathNo2.get();
			categoryNo = pathNo1.get();
		} else if( pathNo1.isPresent() ){
			categoryNo = pathNo1.get();
		}

		Map<String, Object> result = blogService.getAll( id, categoryNo, postNo );
		
		if(result == null) {
			return "redirect:/"+id;
		}
		
		//result.put("id", id);

		//if( result.get("post") == null ) { return "redirect:/"+id; }

		modelMap.putAll( result );
		return "blog/blog-main";
	}
	
	
	@RequestMapping( value ="/admin/basic" , method = RequestMethod.GET )
	public String adminBasic( 
			@PathVariable String id, 
			@AuthUser UserVo authUser, 
			Model model) {
		
		if(authUser==null) {
			return "redirect:/"+id;
		} else if(!id.equals(authUser.getId())) {
			return "redirect:/"+id;
		}
		
		BlogVo blogVo = blogService.blogInfo(id);
		model.addAttribute("blog", blogVo);
		
		return "blog/blog-admin-basic";
	}
	
	
	@RequestMapping( value = "/admin/basic", method = RequestMethod.POST)
	public String adminConfig(
			@PathVariable String id, 
			BlogVo blogVo, 
			@RequestParam(value="logo-file") MultipartFile multipartFile ) {
		
		if(!multipartFile.isEmpty()) {
			String url = blogService.restore(multipartFile);
			blogVo.setLogo(url);
		}
		
		System.out.println(blogVo);
		blogService.updateBlog(blogVo);
		
		return "redirect:/"+id+"/admin/basic";
	}
	
	@RequestMapping( value = "/admin/category", method = RequestMethod.GET )
	public String adminCategory( 
			@PathVariable String id, 
			@AuthUser UserVo authUser, 
			Model model) {
		
		if(authUser==null) {
			return "redirect:/"+id;
		} else if(!id.equals(authUser.getId())) {
			return "redirect:/"+id;
		}
		BlogVo blogVo = blogService.blogInfo(id);
		model.addAttribute("blog", blogVo);
		
		List<CategoryVo> cList = blogService.blogCategory(id);
		model.addAttribute("cList", cList);
		
		return "blog/blog-admin-category";
	}
	
	@RequestMapping( value = "/admin/category", method = RequestMethod.POST )
	public String adminCategoryInsert( 
			@PathVariable String id, 
			CategoryVo categoryVo) {
		
		//System.out.println(categoryVo);
		blogService.insertCategoryVo(categoryVo);
		
		return "redirect:/"+id+"/admin/category";
	}
	
	@RequestMapping("/admin/delete/{no}")
	public String adminCategoryDelete(
			@PathVariable String id,
			@AuthUser UserVo authUser,
			@PathVariable("no") Long no) {
		
		if(authUser==null) {
			return "redirect:/"+id;
		} else if(!id.equals(authUser.getId())) {
			return "redirect:/"+id;
		}
		
		blogService.deleteCategoryVo(no);
		
		return "redirect:/"+id+"/admin/category";
	}
	
	@RequestMapping( value = "/admin/write", method = RequestMethod.GET )
	public String adminWrite(
			@PathVariable String id, 
			@AuthUser UserVo authUser, 
			Model model) {
		
		if(authUser==null) {
			return "redirect:/"+id;
		} else if(!id.equals(authUser.getId())) {
			return "redirect:/"+id;
		}
		BlogVo blogVo = blogService.blogInfo(id);
		model.addAttribute("blog", blogVo);
		
		List<CategoryVo> cList = blogService.categoryList(id);
		model.addAttribute("cList", cList);
		
		return "blog/blog-admin-write";
	}
	
	@RequestMapping( value = "/admin/write", method = RequestMethod.POST )
	public String adminWriteAction(
			@PathVariable String id, 
			PostVo postVo) {
		
		//System.out.println(postVo);
		if("".equals(postVo.getTitle())) { postVo.setTitle("noTitle"); }
		//System.out.println(postVo);
		blogService.insertPostVo(postVo);
		
		return "redirect:/"+id+"/admin/write";
	}
	
}
