package com.example.demo.login.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.login.domain.model.GroupOrder;
import com.example.demo.login.domain.model.SignupForm;
import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.service.UserService;

@Controller
public class SignupController {
	
	private final UserService userService;

    //ラジオボタン用変数
    private Map<String, String> radioMarriage;
    
    @Autowired
    public SignupController(UserService userService) {
    	this.userService = userService;
    }
    
    private Map<String, String> initRadioMarrige() {

        Map<String, String> radio = new LinkedHashMap<>();

        // 既婚、未婚をMapに格納
        radio.put("既婚", "true");
        radio.put("未婚", "false");

        return radio;
    }

    /**
     * ユーザー登録画面のGETメソッド用処理.
     */
    @GetMapping("/signup")
    public String getSignUp(Model model, @ModelAttribute SignupForm signupForm) {

        // ラジオボタンの初期化メソッド呼び出し
        radioMarriage = initRadioMarrige();

        // ラジオボタン用のMapをModelに登録
        model.addAttribute("radioMarriage", radioMarriage);

        // signup.htmlに画面遷移
        return "login/signup";
    }

    /**
     * ユーザー登録画面のPOSTメソッド用処理.
     */
    @PostMapping("/signup")
    public String postSignUp(
		Model model,
		@ModelAttribute @Validated(GroupOrder.class) SignupForm signupForm,
		BindingResult result)
    {
    	if(result.hasErrors()) {
    		System.out.println(result);
    		return getSignUp(model, signupForm);
    	}
    	System.out.println(signupForm);
    	
    	var user = new User();
    	user.setUserId(signupForm.getUserId());
    	user.setPassword(signupForm.getPassword());
    	user.setUserName(signupForm.getUserName());
    	user.setBirthday(signupForm.getBirthday());
    	user.setAge(signupForm.getAge());
    	user.setMarriage(signupForm.isMarriage());
    	user.setRole("ROLLE_GENERAL");
    	boolean bool = userService.insert(user);
    	if(bool == true) {
    		System.out.println("insert成功");
    	} else {
    		System.out.println("insert失敗");
    	}
    	
        // login.htmlにリダイレクト
        return "redirect:/login";
    }
}