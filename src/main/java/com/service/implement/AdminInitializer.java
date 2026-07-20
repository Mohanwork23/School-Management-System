/*
 * package com.service.implement; import
 * org.springframework.boot.CommandLineRunner; import
 * org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import
 * org.springframework.stereotype.Component;
 * 
 * import com.entity.users.Admin; import com.entity.enums.Role; import
 * com.repository.UserRepository; import com.util.IdGeneratorUtil;
 * 
 * @Component public class AdminInitializer implements CommandLineRunner {
 * 
 * private final UserRepository userRepository;
 * 
 * public AdminInitializer(UserRepository userRepository) { this.userRepository
 * = userRepository; }
 * 
 * @Override public void run(String... args) throws Exception { Admin admin =
 * new Admin(); admin.setFullName("Default Admin");
 * admin.setEmail("admin@example.com"); admin.setPhone("1234567890");
 * admin.setUsername("admin2025"); admin.setPassword(new
 * BCryptPasswordEncoder().encode("default123")); // Default password
 * admin.setRole(Role.ADMIN);
 * 
 * // Save the admin user userRepository.save(admin);
 * System.out.println("Default admin created with username: " +
 * admin.getUsername());
 * 
 * } }
 */