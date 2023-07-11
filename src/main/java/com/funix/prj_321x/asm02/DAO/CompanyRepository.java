package com.funix.prj_321x.asm02.DAO;

import com.funix.prj_321x.asm02.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Company findByUserId(int theId);

    @Query(value = "SELECT * FROM company c JOIN (SELECT sum(count) AS sum, company_id " +
            "FROM recruitment r JOIN " +
            "(SELECT count(recruitment_id) AS count, recruitment_id FROM apply_post GROUP BY recruitment_id) a " +
            "ON r.id = a.recruitment_id GROUP BY company_id ORDER BY sum DESC LIMIT 3) a " +
            "ON c.id = a.company_id ORDER BY sum DESC", nativeQuery = true)
    List<Company> findTopCompanies();

    @Query(value = "SELECT c.id, address, description, email, logo, company_name, phone_number, status, c.user_id " +
            "FROM company c JOIN follow_company f ON c.id = f.company_id WHERE f.user_id = ?1", nativeQuery = true)
    Page<Company> findCompanyByUserId(int theId, Pageable pageable);
}
