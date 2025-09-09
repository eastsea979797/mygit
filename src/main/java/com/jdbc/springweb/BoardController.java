package com.jdbc.springweb;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jdbc.dao.BoardDAO;
import com.jdbc.dto.BoardDTO;
import com.jdbc.util.MyUtil;

@Controller
public class BoardController {
	
	@Autowired
	@Qualifier("boardDAO")
	BoardDAO dao;
		
	@Autowired
	MyUtil myUtil;
	
	@RequestMapping(value = "/",method = RequestMethod.GET)
	public String home() {
		
		return "index";
		
	}
	
//	@RequestMapping(value = "/abc/created", 
//			method = {RequestMethod.GET})
//	public String created() {
//		
//		return "bbs/created";
//		
//	}
	
	@RequestMapping(value = "/created.action")
	public ModelAndView created() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("bbs/created");
		
		return mav;
	}
	
	
	@RequestMapping(value = "/created_ok.action",
			method = RequestMethod.POST)
	public String created_ok(BoardDTO dto,
			HttpServletRequest req) {
		int maxNum = dao.getMaxNum();
		
		dto.setNum(maxNum + 1);
		dto.setIpAddr(req.getRemoteAddr());
		
		dao.insertData(dto);
		
		return "redirect:/list.action";
	}
	
	
	@RequestMapping(value="/list.action" ,
			method = {RequestMethod.GET})//문제생김
	public String list(HttpServletRequest req) throws Exception {
		
		String cp = req.getContextPath();

		String pageNum = req.getParameter("pageNum");
		
		int currentPage = 1;
		
		if(pageNum!=null) {
			currentPage = Integer.parseInt(pageNum);
		}
		
		String searchKey = req.getParameter("searchKey");
		String searchValue = req.getParameter("searchValue");
		
		if(searchValue!=null){
			
			if(req.getMethod().equalsIgnoreCase("GET")){
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}
			
		}else{//검색을 안함 ->그럼 초기화시키기.
			searchKey = "subject";
			searchValue = ""; //null(객체가 존재안함)을 쓰면 안되고 ""(객체는존재)를 써야함.
			
		}
		
		
		//전체데이터 개수를 구하기
		int dataCount = dao.getDataCount(searchKey, searchValue);
		//한페이지에 보여줄 데이터의 개수
		int numPerPage = 5;
		//전체페이지의 개수
		int totalPage = myUtil.getPageCount(numPerPage, dataCount);
		//삭제로 인한 마지막페이지 정리
		if(currentPage>totalPage) {
			currentPage = totalPage;
		}
		
		int start = (currentPage-1) * numPerPage +1;
		int end = currentPage * numPerPage;
		
		List<BoardDTO> lists = 
				dao.getLists(start, end, searchKey, searchValue);
		
		String param = "";
		if(searchValue!=null && !searchValue.equals("")) {
			param = "searchKey=" + searchKey;
			param+= "&searchValue=" + 
					URLEncoder.encode(searchValue,"UTF-8");
		}//param에 searchKey=name&searchValue=suzi가 들어있음
		
		//페이징처리
		String listUrl = cp + "/list.action";
		if(!param.equals("")) {
			listUrl += "?" + param;
		}
		
		String pageIndexList =
				myUtil.pageIndexList(currentPage, totalPage, listUrl);
		
		//글보기주소
		String articleUrl = cp + "/article.action?pageNum=" + currentPage;
		
		if(!param.equals("")) {
			articleUrl += "&" + param;
		}
		
		//포워딩 페이지에 넘길 데이터들(4개)
		req.setAttribute("lists", lists);
		req.setAttribute("pageIndexList", pageIndexList);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("articleUrl", articleUrl);
		
		return "bbs/list";
		
	}
	
	@RequestMapping(value = "/article.action",
			method = {RequestMethod.GET})
	public ModelAndView article(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		
		String cp = req.getContextPath();
		
		int num = Integer.parseInt(req.getParameter("num"));
		String pageNum = req.getParameter("pageNum");
		
		String searchKey = req.getParameter("searchKey");
		String searchValue = req.getParameter("searchValue");
		
		if(searchValue!=null) {
			if(req.getMethod().equalsIgnoreCase("GET")) {
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}
		}else {
			searchKey = "subject";
			searchValue = "";
		}
		
		dao.updateHitCount(num);
		
		BoardDTO dto = dao.getReadData(num);
		
		if(dto==null) {
			//return "redirect:/list.action"; ==
			
			ModelAndView mav = new ModelAndView();
			mav.setViewName("redirect:/list.action");
			
			return mav;
		}
		
		int lineSu = dto.getContent().split("\n").length;
		
		dto.setContent(dto.getContent().replaceAll("\n", "<br/>"));
		
		String param ="pageNum=" + pageNum;
		
		if(searchValue!=null && !searchValue.equals("")) {
			param+= "&searchKey=" + searchKey;
			param+= "&searchValue=" +
					URLEncoder.encode(searchValue, "UTF-8");
		}
		
		/*
		req.setAttribute("dto", dto);
		req.setAttribute("params", param);
		req.setAttribute("lineSu", lineSu);
		req.setAttribute("pageNum", pageNum);
					
		return "bbs/article";
		== */
		
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("dto", dto);
		mav.addObject("params", param);
		mav.addObject("lineSu", lineSu);
		mav.addObject("pageNum", pageNum);
		
		mav.setViewName("bbs/article");
		
		return mav;
	}
	
	@RequestMapping(value = "/updated.action",
			method = {RequestMethod.GET})
	public String updated(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		
		String cp = req.getContextPath();
		
		int num = Integer.parseInt(req.getParameter("num"));
		String pageNum = req.getParameter("pageNum");
		String searchKey = req.getParameter("searchKey");
		String searchValue = req.getParameter("searchValue");
		
		if (searchValue!=null) {
			if(req.getMethod().equalsIgnoreCase("get")) {
				searchValue = 
						URLDecoder.decode(searchValue, "UTF-8");
				}
		}else {
			searchKey = "subject";
			searchValue = "";
		}
	
		BoardDTO dto = dao.getReadData(num);
		
		if(dto==null) {
			return "redirect:/list.action";
		}
		
		String param = "pageNum=" + pageNum;
		
		if(searchValue!=null && !searchValue.equals("")) {
			param += "&searchKey=" + searchKey;
			param += "&searchValue=" +
					URLEncoder.encode(searchValue, "UTF-8");
		}
		
		req.setAttribute("dto", dto);
		req.setAttribute("pageNum", pageNum);
		req.setAttribute("params", param);
		req.setAttribute("searchKey", searchKey);
		req.setAttribute("searchValue", searchValue);
		
		return "bbs/updated";
		
	}
	
	@RequestMapping(value = "/updated_ok.action",
			method = {RequestMethod.POST,RequestMethod.GET})
	public String updated_ok(BoardDTO dto,HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		
		
		String pageNum = req.getParameter("pageNum");
		String searchKey = req.getParameter("searchKey");
		String searchValue = req.getParameter("searchValue");
		
		//BoardDTO dto = new BoardDTO();
		
		dto.setNum(Integer.parseInt(req.getParameter("num")));
		dto.setSubject(req.getParameter("subject"));
		dto.setName(req.getParameter("name"));
		dto.setEmail(req.getParameter("email"));
		dto.setPwd(req.getParameter("pwd"));
		dto.setContent(req.getParameter("content"));
		
		dao.updateData(dto);
		
		String param = "pageNum=" + pageNum;
		
		if(searchValue!=null && !searchValue.equals("")) {
			param += "&searchKey=" + searchKey;
			param += "&searchValue=" +
					URLEncoder.encode(searchValue, "UTF-8");
		}
					
		
		return "redirect:/list.action?" + param;
		
		
	}
	
	@RequestMapping(value = "/deleted_ok.action",
			method = {RequestMethod.POST,RequestMethod.GET})
	public String deleted_ok(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
	
	int num = Integer.parseInt(req.getParameter("num"));
	String pageNum = req.getParameter("pageNum");
	String searchKey = req.getParameter("searchKey");
	String searchValue = req.getParameter("searchValue");
	
	
	//삭제하기
	dao.deleteData(num);
		
	String param = "pageNum=" + pageNum;
	
	if(searchValue!=null && !searchValue.equals("")) {
		param += "&searchKey=" + searchKey;
		param += "&searchValue=" +
				URLEncoder.encode(searchValue, "UTF-8");
	}
				
	return "redirect:/list.action?" + param;
	
	
	}
	
	
}

	
	
	
	
	
	
	
	
	
	
	