package com.qy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.qy.service.FilmService;
import com.qy.util.JSONData;


@Controller
@Scope("session")
@RequestMapping("film")
public class FilmController {
	JSONData jsonData;//jsonData数据
	public FilmController() {
		jsonData = new JSONData();
	}
	@Autowired
	@Qualifier("filmSerivce")
	private FilmService filmService;
	
	/**
	 * 查询电影（根据豆瓣评分降分排序）
	 * @return
	 */
	@RequestMapping("query_ByDouban")
	//@ResponseBody
	// 将方法返回值作为相应数据,而不是返回的页面路径
	public void queryFilmOrderByDoubanDesc(HttpSession session) {
		List<Map<String, Object>> filmList=filmService.queryFilmOrderByDoubanDesc();
		session.setAttribute("filmList", filmList);
		//return "redirect:/rate_rank.jsp";
	}
	/**
	 * 查询电影（根据豆瓣评分降分排序）,分页查询
	 * @return
	 */
	@RequestMapping("query_goodList")
	@ResponseBody
	// 将方法返回值作为相应数据,而不是返回的页面路径
	public String queryGoodFilmList(Integer page,HttpSession session) {
		System.out.println(page);
		//Integer pageInt=Integer.parseInt(page);
		if(page==null) page=1;
		List<Map<String, Object>> filmList=filmService.queryFilmOrderByDoubanByPage(page-1);
		//List<Map<String, Object>> filmList2=filmService.queryFilmOrderByDoubanByPage(page-1);
		//filmList.addAll(filmList2);
		//session.setAttribute("filmList", filmList);
		// 查询出来数据用JSONData来封装
		jsonData.setRows(filmList);
		// 返回json数据
		return JSONObject.toJSONString(jsonData);
		//return "redirect:/more.jsp";
	}
	/**
	 * 首页加载要做的事
	 * @return
	 */
	@RequestMapping("index_query")
	@ResponseBody
	// 将方法返回值作为相应数据,而不是返回的页面路径
	public String queryFilm_Index(HttpSession session) {
		List<Map<String, Object>> filmList=filmService.queryFilmOrderByDoubanDesc();
		//session.setAttribute("filmList", filmList);
		// 查询出来数据用JSONData来封装
		jsonData.setRows(filmList);
		// 返回json数据
		return JSONObject.toJSONString(jsonData);
		//return "redirect:/m.jsp";

	}
	/**
	 * 查询本周推荐电影
	 * @return
	 */
	@RequestMapping("query_recommend")
	@ResponseBody
	// 将方法返回值作为相应数据,而不是返回的页面路径
	public String queryRecommendFilm(HttpSession session) {
		List<Map<String, Object>> filmList=filmService.queryRecommendFilm();
		// 查询出来数据用JSONData来封装
		jsonData.setRows(filmList);
		// 返回json数据
		return JSONObject.toJSONString(jsonData);
		//return "redirect:/m.jsp";

	}
	/**
	 * 查询电影(最近电影前10)
	 * @return
	 */
	@RequestMapping("query_newest")
	//@ResponseBody
	// 将方法返回值作为相应数据,而不是返回的页面路径
	public String queryFilmNewest(HttpSession session) {
			List<Map<String, Object>> filmList=filmService.queryFilmOrderByDoubanDesc();
			session.setAttribute("filmList", filmList);
			return "redirect:/m.jsp";
	
	}
	/**
	 * 根据电影id查询电影
	 * @return
	 */
	@RequestMapping("query_ById")
	//@ResponseBody
	// 将方法返回值作为相应数据,而不是返回的页面路径
	public String queryFilmByFilmId(Integer film_id,HttpSession session) {
		Map<String, Object> film=filmService.queryFilmByFilmId(film_id);
		session.setAttribute("film", film);
		//类似电影推荐（10个）
		String label=(String) film.get("label");
		Integer label1=Integer.parseInt(label.split("/")[0]);
		Integer label2=Integer.parseInt(label.split("/")[1]);
		Map<String, Object> paramMap=new HashMap<String, Object>() ;
		paramMap.put("label", label);
		paramMap.put("label1", label1);
		paramMap.put("label2", label2);
		List<Map<String, Object>> similarFilmList=filmService.querySimilarFilm(paramMap);
		session.setAttribute("similarFilmList", similarFilmList);
		//电影的标签
		String labelQuery="";
		String labels[]=label.split("/");
		for(int i=0;i<labels.length;i++){
			if(i==0) labelQuery=labels[0];
			else
			labelQuery+=","+labels[i];
		}	
		List<Map<String,Object>> labelList=filmService.queryFilmLabel(labelQuery);
		session.setAttribute("labelList", labelList);
		return "redirect:/details.jsp";
	}
	/**
	 *test
	 * @return
	 */
	@RequestMapping("query_test")
	@ResponseBody
	// 将方法返回值作为相应数据,而不是返回的页面路径
	public String Test(Integer film_id,HttpSession session) {
		Map<String, Object> film=filmService.queryFilmByFilmId(film_id);

		jsonData.setRows(film);
		// 返回json数据
		return JSONObject.toJSONString(jsonData);
	}
	/**
	 * 查询电影总条数,得出总页数
	 * @return
	 */
	@RequestMapping("query_count")
	//@ResponseBody
	// 将方法返回值作为相应数据,而不是返回的页面路径
	public String queryFilmCount(HttpSession session) {
		Integer count=filmService.queryFilmCount();
		Integer pageS=0;
		if(count%25!=0){
			pageS=count/25+1;
		}else pageS=count/25;
		session.setAttribute("pageS", pageS);
		return "redirect:/more.jsp";
	}

}
