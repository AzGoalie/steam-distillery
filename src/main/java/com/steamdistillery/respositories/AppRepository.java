package com.steamdistillery.respositories;

import com.steamdistillery.models.App;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRepository extends JpaRepository<App, Long> {
  App findByAppid(int appid);
}
