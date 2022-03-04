package com.example.demo.login.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.login.domain.model.SignupForm;
import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.service.UserService;

@Controller
public class HomeController {
	
	private final UserService userService;
	
	//ラジオボタン用変数
    private Map<String, String> radioMarriage;
    
    @Autowired
    public HomeController(UserService userService) {
		this.userService = userService;
	}
    
    private Map<String, String> initRadioMarrige() {

        Map<String, String> radio = new LinkedHashMap<>();

        // 既婚、未婚をMapに格納
        radio.put("既婚", "true");
        radio.put("未婚", "false");

        return radio;
    }
	
	@GetMapping("/home")
	public String getHome(Model model) {
		model.addAttribute("contents", "login/home :: home_contents");	
		return "/login/homeLayout";
	}
	
	@GetMapping("/userList")
	public String getUserList(Model model) {
		model.addAttribute("contents", "login/userList :: userList_contents");
		
		List<User> userList = userService.selectMany();
		model.addAttribute("userList", userList);
		
		int count = userService.count();
		model.addAttribute("userListCount", count);
		
		return "/login/homeLayout";
	}
	
	@GetMapping("/userDetail/{id:.+}")
	public String getUserDetail(
		Model model,
		@ModelAttribute SignupForm signupForm,
		@PathVariable("id") String userId) 
	{
		model.addAttribute("contents", "login/userDetail :: userDetail_contents");
		
		radioMarriage = initRadioMarrige();
		model.addAttribute("radioMarriage", radioMarriage);
		
		System.out.println("userId:" + userId);
		if(userId != null && userId.length() > 0) {
			User user = userService.selectOne(userId);
			signupForm.setUserId(user.getUserId());
			signupForm.setPassword(user.getPassword());
			signupForm.setUserName(user.getUserName());
			signupForm.setBirthday(user.getBirthday());
			signupForm.setAge(user.getAge());
			signupForm.setMarriage(user.isMarriage());
		}
		return "/login/homeLayout";
	}
	
//	@PostMapping("/userDetail/{id:.+}")
//	public String postUserDetail(
//		Model model,
//		@ModelAttribute @Validated SignupForm signupForm,
//		BindingResult result,
//		@PathVariable("id") String userId)
//	{
//		if(result.hasErrors()) {
//			return getUserDetail(model, signupForm, userId);
//		}
//	}
	
	@PostMapping(value = "/userDetail", params = "update")
    public String postUserDetailUpdate(@ModelAttribute SignupForm form, Model model) {

        System.out.println("更新ボタンの処理");

        //Userインスタンスの生成
        User user = new User();

        //フォームクラスをUserクラスに変換
        user.setUserId(form.getUserId());
        user.setPassword(form.getPassword());
        user.setUserName(form.getUserName());
        user.setBirthday(form.getBirthday());
        user.setAge(form.getAge());
        user.setMarriage(form.isMarriage());

        System.out.println(user);
        
        try {
        	boolean bool = userService.updateOne(user);
        	if (bool == true) {
                model.addAttribute("result", "更新成功");
            } else {
                model.addAttribute("result", "更新失敗");
            }
        } catch(DataAccessException e) {
        	model.addAttribute("result", "更新失敗(トランザクション)");
        }

        //ユーザー一覧画面を表示
        return getUserList(model);
    }
	
	@PostMapping(value = "/userDetail", params = "delete")
    public String postUserDetailDelete(@ModelAttribute SignupForm form, Model model) {
		boolean bool = userService.deletOne(form.getUserId());
		if (bool == true) {
            model.addAttribute("result", "削除成功");
        } else {
            model.addAttribute("result", "削除失敗");
        }
		return getUserList(model);
	}
	
	@PostMapping("/logout")
	public String postLogout() {
		return "redirect:/login";
	}
	
	@GetMapping("/userList/csv")
	public ResponseEntity<byte[]> getUserListCsv(Model model) {
		
		userService.userCsvOut();
		byte[] bytes = null;
		try {
			bytes = userService.getFile("sample.csv");
		} catch(IOException e) {
			e.getStackTrace();
		}
		
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "text/csv; charset=UTF-8");
		header.setContentDispositionFormData("filename", "sample.csv");
		
		return new ResponseEntity<>(bytes, header, HttpStatus.OK);
	}
	
	@GetMapping("/admin")
	public String getAdmin(Model model) {
		model.addAttribute("contents", "login/admin :: admin_contents");
		return "/login/homeLayout";
	}
}
