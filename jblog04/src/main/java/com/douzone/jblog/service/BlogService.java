package com.douzone.jblog.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.jblog.repository.BlogRepository;
import com.douzone.jblog.repository.CategoryRepository;
import com.douzone.jblog.repository.PostRepository;
import com.douzone.jblog.vo.BlogVo;
import com.douzone.jblog.vo.CategoryVo;
import com.douzone.jblog.vo.PostVo;

@Service
public class BlogService {
	@Autowired
	private BlogRepository blogRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private PostRepository postRepository;

	public Map<String,Object> getAll(String id, Long categoryNo, Long postNo) {
		// System.out.println(categoryNo+"/"+postNo);
		
		if(postNo == 0L) {
			if(categoryNo == 0L) {
				categoryNo = categoryRepository.getDefultcategoryNo(id);
			}
			postNo = postRepository.getDefultPostNo(categoryNo);
		}
		
		if(0==categoryRepository.findIdandNo(id,categoryNo)) {
			return null;
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<CategoryVo> categoryList = null;
		categoryList = categoryRepository.getCategoryList(id);
		//if(categoryList==null) { return null; }
		result.put("categoryList", categoryList);
		
		List<PostVo> postList = null;
		postList = postRepository.getPostList(id,categoryNo);
		//if(postList==null) { return null; }
		result.put("postList", postList);
		
		PostVo post = null;
		post = postRepository.getPost(categoryNo,postNo);
		//if(post==null) { return null; }
		result.put("post", post);
		
		BlogVo blog = blogRepository.findOfId(id);
		result.put("blog", blog);
		
		return result;
	}
	
	
	public Boolean updateBlog(BlogVo blogVo) {
		return 1==blogRepository.updateBlog(blogVo);
	}


	public Boolean findId(String id) {
		return 0==blogRepository.findId(id);
	}


	public BlogVo blogInfo(String id) {
		return blogRepository.findOfId(id);
	}

	/////////////////////////// 파일업로드 시작 ////////////////////////////////////////////////
	private static final String SAVE_PATH = "/jblog-uploads";
	private static final String URL = "image";
	public String restore(MultipartFile multipartFile) {
		String url;
		
		try {
			if(multipartFile.isEmpty()) {
				return url = "";
			}

			String originFilename = multipartFile.getOriginalFilename();
			String extName = originFilename.substring( originFilename.lastIndexOf('.')+1);
			String saveFilename = generateSaveFilename(extName);
			long fileSize = multipartFile.getSize();

			System.out.println("######## " + originFilename);
			System.out.println("######## " + saveFilename);
			System.out.println("######## " + fileSize);
			
			byte[] fileData = multipartFile.getBytes();
			
			OutputStream os = new FileOutputStream(SAVE_PATH + "/" + saveFilename);
			os.write(fileData);
			os.close();
			
			url = URL + "/" + saveFilename;
			
		} catch (IOException e) {
			throw new RuntimeException("file upload error"+e);
		}
		return url;
	}
	private String generateSaveFilename(String extName) {
		String filenaem = "";
		
		Calendar calendar = Calendar.getInstance();
		filenaem += calendar.get(Calendar.YEAR);
		filenaem += calendar.get(Calendar.MONTH);
		filenaem += calendar.get(Calendar.DATE);
		filenaem += calendar.get(Calendar.HOUR);
		filenaem += calendar.get(Calendar.MINUTE);
		filenaem += calendar.get(Calendar.SECOND);
		filenaem += calendar.get(Calendar.MILLISECOND);
		filenaem += ("."+extName);
		
		return filenaem;
	}
	///////////////////////////// 파일업로드  끝 ////////////////////////////////////////////////


	public List<CategoryVo> blogCategory(String id) {
		return categoryRepository.getCategoryCountList(id);
	}

	public List<CategoryVo> categoryList(String id) {
		return categoryRepository.getCategoryList(id);
	}


	public Boolean insertCategoryVo(CategoryVo categoryVo) {
		return 0==categoryRepository.insert(categoryVo);
	}


	public Boolean insertPostVo(PostVo postVo) {
		return 0==postRepository.insert(postVo);
	}


	public Boolean deleteCategoryVo(Long no) {
		if(0==categoryRepository.postCount(no)) {
			return 0==categoryRepository.delete(no);
		}
		return false;
	}
	
	
}
