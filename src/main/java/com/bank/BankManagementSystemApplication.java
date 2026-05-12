package com.bank;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bank.entities.Role;
import com.bank.entities.User;
import com.bank.repositories.RoleRepo;
import com.bank.repositories.UserRepo;
import com.bank.services.AdminService;

@SpringBootApplication
public class BankManagementSystemApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(BankManagementSystemApplication.class, args);
	}


	@Autowired
	private UserRepo userRepo;

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	

	@Override
	public void run(String... args) throws Exception {
		  
		Role role1 = roleRepo.findByName("ROLE_USER").orElse(null);
		if (role1 == null) {
			Role r1 = new Role();
			r1.setName("ROLE_USER");
			Role save = roleRepo.save(r1);
		}

		Role role2 = roleRepo.findByName("ROLE_ADMIN").orElse(null);
		if (role2 == null) {
			Role r2 = new Role();
			r2.setName("ROLE_ADMIN");
			Role save = roleRepo.save(r2);
		}

		User admin = userRepo.findByEmail("kundan@gmail.com").orElse(null);
		if (admin == null) {
			User u1 = new User();
			u1.setName("kundan");
			u1.setEmail("kundan@gmail.com");
			u1.setPassword(passwordEncoder.encode("654321"));
			u1.setPhoneNumber("5854785452");
			u1.setAadhaarNumber("2545854587");
			u1.setDob(LocalDate.parse("2004-09-09"));
			u1.setAddress("Noida");
			u1.setRoles(List.of(role2));
			userRepo.save(u1);
		}

		
	}

}
