package com.funix.prj_321x.asm02.DAO;

import com.funix.prj_321x.asm02.entity.ApplyPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApplyPostRepository extends JpaRepository<ApplyPost, Integer> {
    ApplyPost findAllByUserIdAndRecruitmentId(int userId, int recruitmentId);

    @Query(value = "SELECT * FROM apply_posts WHERE user_id = ?1", nativeQuery = true)
    Page<ApplyPost> findAllByUserId(int UserId, Pageable pageable);
}
