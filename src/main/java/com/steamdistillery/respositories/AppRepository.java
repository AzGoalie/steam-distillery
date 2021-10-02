package com.steamdistillery.respositories;

import com.steamdistillery.models.App;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppRepository extends JpaRepository<App, Long> {
  App findByAppid(int appid);

  @Query("select appid from App")
  List<Integer> getAppids();
}
