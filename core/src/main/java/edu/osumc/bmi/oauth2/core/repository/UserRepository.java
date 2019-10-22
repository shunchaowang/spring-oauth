package edu.osumc.bmi.oauth2.core.repository;

import edu.osumc.bmi.oauth2.core.domain.User;
import edu.osumc.bmi.oauth2.core.dto.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

  String findUserWithRoleByUsernameQuery =
      "select u.username as username, r as roles from User u join u.roles r where u.username = ?1";
  String findUserWithRoleQuery =
      "select u.username as username, r as roles from User u join u.roles r";
  String findUserCountQuery = "select count(u) from User u";

  Optional<User> findByUsername(String username);

  @Query(findUserWithRoleByUsernameQuery)
  UserInfo fetchByUsername(String username);

  @Query(value = findUserWithRoleQuery, countQuery = findUserCountQuery)
  Page<UserInfo> fetchAll(Pageable pageable);
}
