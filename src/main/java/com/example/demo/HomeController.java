package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import javafx.scene.canvas.GraphicsContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
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
    public String processEmp(@Valid @ModelAttribute User employee, @RequestParam("deptId") long id, @RequestParam("file") MultipartFile file, BindingResult result){
        if(file.isEmpty() && (employee.getUrl()==null || employee.getUrl().equals(""))){
            return "redirect:/addEmp";
        }
        if(result.hasErrors()){
            return "addEmp";
        }
        Department department = departmentRepository.findById(id).get();
        employee.setDepartment(department);
        Set<User> users;
        if(department.employees != null){
            users = new HashSet<>(department.employees);
        }
        else{
            users = new HashSet<>();
        }
        if(!file.isEmpty()){
            try {
                Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
                employee.setUrl(uploadResult.get("url").toString());
                userRepository.save(employee);
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/addEmp";
            }
        }
        else{
            userRepository.save(employee);
        }
        users.add(employee);
        department.setEmployees(users);
        departmentRepository.save(department);
        return "redirect:/";
    }
    @PostMapping("/processDept")
    public String processDept(@Valid @ModelAttribute Department department){
        departmentRepository.save(department);
        return "redirect:/";
    }

    @RequestMapping("/updateDept/{id}")
    public String updateDept(@PathVariable("id") long id, Model model){
        model.addAttribute("department", departmentRepository.findById(id));
        return "addDept";
    }

    @RequestMapping("/updateEmp/{id}")
    public String updateEmp(@PathVariable("id") long id, Model model){
        model.addAttribute("employee", userRepository.findById(id).get());
        model.addAttribute("departments", departmentRepository.findAll());
        return "addEmp";
    }

    @RequestMapping("/deleteDept/{id}")
    public String delDept(@PathVariable("id") long id){
        departmentRepository.deleteById(id);
        return "redirect:/";
    }

    @RequestMapping("/deleteEmp/{id}")
    public String delEmp(@PathVariable("id") long id){
        userRepository.deleteById(id);
        return "redirect:/";
    }
}
