package com.cafe24.mysite.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cafe24.mysite.dto.JSONResult;
import com.cafe24.mysite.service.UserService;
import com.cafe24.mysite.vo.UserVo;

@Controller("userAPIController")
@RequestMapping("/user/api")
public class UserController {
	
	@Autowired
	private UserService userService;
	

	@ResponseBody
	@RequestMapping("/checkemail")
	public JSONResult checkEmail(
			@RequestParam(value="email", required=true, defaultValue = "") String email) {
		
		Boolean isExist = userService.existEmail(email);
		
		
		JSONResult res = JSONResult.success(isExist);
//		res.setResult("");
//		res.setResult("");
//		Map<String,Object> map = new HashMap<String, Object>();
//		map.put("result", "success");
//		map.put("data", isExist);
		//map.put("message", "....."); //error message
		
		return res;
	}
	
	//@ResponseBody
	@PostMapping("/join")
	public ResponseEntity<JSONResult> join(@RequestBody @Valid UserVo userVo,
			BindingResult result){
	
		if(result.hasErrors()) {
			List<ObjectError> errors=result.getAllErrors();
			for(ObjectError error : errors) {
				System.out.println(error.toString());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(JSONResult.fail(error.getDefaultMessage()));
			}
		}
		
		JSONResult res = JSONResult.success(null);
//		res.setResult("");
//		res.setResult("");
//		Map<String,Object> map = new HashMap<String, Object>();
//		map.put("result", "success");
//		map.put("data", isExist);
		//map.put("message", "....."); //error message
		
		return ResponseEntity.status(HttpStatus.OK).body(JSONResult.success(null));
	}
	
	@PostMapping("/login")
	public ResponseEntity<JSONResult> login(@RequestBody UserVo userVo){
	
		Validator validator = 
				Validation.buildDefaultValidatorFactory().getValidator();
		
		Set<ConstraintViolation<UserVo>> validatorResults = 
				validator.validateProperty(userVo, "email");
		
		if(validatorResults.isEmpty() == false) {
			for(ConstraintViolation<UserVo> validatorResult : validatorResults) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(JSONResult.fail(validatorResult.getMessage())); 
			}
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(JSONResult.success(null)); 
	}
	
}
