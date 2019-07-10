package com.cafe24.mysite.controller.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cafe24.mysite.controller.GuestbookService2;
import com.cafe24.mysite.dto.JSONResult;


@RestController("guestbookAPIController")
@RequestMapping("/api/guestbook")
public class GuestbookController {
	
	@Autowired
	private GuestbookService2 gbs2;

	
	@RequestMapping(value="/list/{no}", method=RequestMethod.GET)
	public JSONResult list(@PathVariable(value="no") int no) {

		return JSONResult.success(gbs2.getContentsList(1));
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public JSONResult add() {

		return JSONResult.success(null);
	}
}