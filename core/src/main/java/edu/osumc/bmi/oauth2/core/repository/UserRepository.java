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

  Optional<User> findByUsername(String username);

  @Query("SELECT new edu.osumc.bmi.oauth2.core.dto.UserInfo(user.username, user.roles) "
  + "FROM User user")
  Page<UserInfo> fetchUserInfo(Pageable pageable);
}
