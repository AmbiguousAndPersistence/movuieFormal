package com.qy.controller;

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
@Scope("prototype")
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
	public String queryFilmOrderByDoubanDesc(HttpSession session) {
		List<Map<String, Object>> filmList=filmService.queryFilmOrderByDoubanDesc();
		session.setAttribute("filmList", filmList);
		// 查询出来数据用JSONData来封装
		//jsonData.setRows();
		// 返回json数据
		//return JSONObject.toJSONString(jsonData);
		return "redirect:/rate_rank.jsp";
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
		// 查询出来数据用JSONData来封装
		//jsonData.setRows();
		// 返回json数据
		//return JSONObject.toJSONString(jsonData);
		return "redirect:/detail.jsp";
	}
}
