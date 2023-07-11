package com.funix.prj_321x.asm02.DAO;

import com.funix.prj_321x.asm02.entity.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Integer> {

    List<Recruitment> findAllByTitle(String title);

    Page<Recruitment> findAllByCompanyId(int theId, Pageable pageable);

    Page<Recruitment> findAllByCategoryId(int theId, Pageable pageable);

    @Query(value = "SELECT * FROM recruitment r JOIN " +
            "(SELECT count(recruitment_id) AS count, recruitment_id FROM apply_post GROUP BY recruitment_id) a " +
            "ON r.id = a.recruitment_id " +
            "ORDER BY count DESC, salary DESC, quantity DESC LIMIT 5", nativeQuery = true)
    List<Recruitment> findTopRecruitments();

    @Query(value = "SELECT * FROM recruitment WHERE title LIKE %?1%", nativeQuery = true)
    Page<Recruitment> findAllByTitlePagination(String keySearch, Pageable pageable);

    @Query(value = "SELECT r.id, r.address, created_at, r.description, experience, quantity, salary, " +
            "r.status, title, type, view, deadline, category_id, company_id " +
            "FROM recruitment r JOIN company c ON r.company_id = c.id WHERE company_name LIKE %?1%", nativeQuery = true)
    Page<Recruitment> findAllByCompanyPagination(String keySearch, Pageable pageable);

    @Query(value = "SELECT * FROM recruitment WHERE address LIKE %?1%", nativeQuery = true)
    Page<Recruitment> findAllByAddressPagination(String keySearch, Pageable pageable);

    @Query(value = "SELECT r.id, r.address, created_at, r.description, experience, quantity, salary, " +
            "r.status, title, type, view, deadline, category_id, company_id " +
            "FROM recruitment r JOIN save_job s ON r.id = s.recruitment_id WHERE user_id = ?1",
            nativeQuery = true)
    Page<Recruitment> findRecruitmentsSavedByUserId(int UserId, Pageable pageable);

}
