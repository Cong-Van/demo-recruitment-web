package com.funix.prj_321x.asm02.DAO;

import com.funix.prj_321x.asm02.entity.Cv;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CVRepository extends JpaRepository<Cv, Integer> {

    Cv findCvByUserId(int theId);
}
