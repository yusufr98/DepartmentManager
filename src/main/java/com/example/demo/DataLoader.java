package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired UserRepository userRepository;
    @Autowired DepartmentRepository departmentRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired UserService userService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String ... strings) throws Exception {

        Department department = new Department();
        department.setName("Front-end");
        Set<User> users = new HashSet<>();
        departmentRepository.save(department);

        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("ADMIN"));

        User user = new User("yusuf@gmail.com", "password", "Yusuf", "Reyazuddin", "yusuf", true, department,"http://img.fixthephoto.com/blog/UserFiles/Image/222/40-best-headshot-poses_1242x1242.jpg");
        userService.saveUser(user);
        users.add(user);

        user = new User("pierz@gmail.com","password","Pierz","Barry","pierz",true, department,"https://static.livebooks.com/b18c364831dd4d4ca0794ece1769bb78/i/d2685474f6f84b8988dd5123cb4e67ce/1/4SoifmQp45JMgBnHp7ed2/Maryland-Headshot-Photographer_004%20copy.jpg");
        userService.saveUser(user);
        users.add(user);

        department.setEmployees(users);
        departmentRepository.save(department);

        //ADMIN
        user = new User("admin@admin.com","password","Admin","istrator","admin",true);
        userService.saveAdmin(user);

        department = new Department();
        department.setName("Back-end");
        users = new HashSet<>();
        departmentRepository.save(department);

        user = new User("tony@gmail.com","password","Tony","Franks","tony",true, department,"https://www.lensrentals.com/blog/media/2016/02/Cinematic-Headshots-1.jpg");
        userService.saveUser(user);
        users.add(user);

        user = new User("victor@gmail.com","password","Victor","Phi","victor",true, department,"https://solarisstudios.com/wp-content/uploads/2019/07/SOL_1195_400.jpg");
        userService.saveUser(user);
        users.add(user);

        department.setEmployees(users);
        departmentRepository.save(department);
    }
}
