package com.example.demo;

import javafx.scene.canvas.GraphicsContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    CloudinaryConfig cloudc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @RequestMapping("/")
    public String list(Model model){
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("employees", userRepository.findAll());
        return "index";
    }
//    @GetMapping("/register")
//    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
//        model.addAttribute("user", user);
//        if(result.hasErrors()){
//            return "registration";
//        }
//        else {
//            userService.saveUser(user);
//            model.addAttribute("message", "User Account Created");
//        }
//        return "index";
//    }
    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/search")
    public String search(Model model,  @RequestParam("search") String s) {
        model.addAttribute("departments", departmentRepository.findByNameContainingIgnoreCase(s));
        return "index";
    }
    @GetMapping("/addEmp")
    public String addEmployee(Model model){
        model.addAttribute("employee", new User());
        model.addAttribute("departments", departmentRepository.findAll());
        return "addEmp";
    }
    @GetMapping("/addDept")
    public String addDept(Model model){
        model.addAttribute("department", new Department());
        return "addDept";
    }

    @PostMapping("/processEmp")
    public String processEmp(@Valid @ModelAttribute User employee, @RequestParam("deptId") long id){
        Department department = departmentRepository.findById(id).get();
        employee.setDepartment(department);
        Set<User> users;
        if(department.employees != null){
            users = new HashSet<>(department.employees);
        }
        else{
            users = new HashSet<>();
        }
        users.add(employee);
        department.setEmployees(users);
        userRepository.save(employee);
        departmentRepository.save(department);
        return "redirect:/";
    }
    @PostMapping("/processDept")
    public String processDept(@Valid @ModelAttribute Department department){
        departmentRepository.save(department);
        return "redirect:/";
    }

    @RequestMapping("/update/{id}")
    public String updateEmp(@PathVariable("id") long id, Model model){
        model.addAttribute("");
        return "add";
    }

    @RequestMapping("/delete/{id}")
    public String delEmp(@PathVariable("id") long id){
        userRepository.deleteById(id);
        return "redirect:/";
    }
}
