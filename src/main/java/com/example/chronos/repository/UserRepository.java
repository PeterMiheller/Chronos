package com.example.chronos.repository;

import com.example.chronos.model.User;
import com.example.chronos.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    List<User> findByUserType(UserType userType);
    List<User> findByCompanyId(int companyId);
    List<User> findByAdministratorId(int administratorId);
}