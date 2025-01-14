package com.springboot.app.tags;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
//	Optional<Tag> findByTagName(String tagName);

    // find top sortOrder
    @Query("SELECT MAX(sortOrder) FROM Tag")
    Integer findTopSortOrder();

    @Query("SELECT t FROM Tag t WHERE (:label IS NULL OR :label = '' OR t.label LIKE %:label%)")
    Page<Tag> searchByLable(@Param("label") String label, Pageable pageable);

    //count tag by disabled
    Long countByDisabled(Boolean disabled);

    boolean existsByLabel(String label);
}
