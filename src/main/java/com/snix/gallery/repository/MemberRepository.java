package com.snix.gallery.repository;

import com.snix.gallery.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Item > repository, Integer > items.ID
@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

    Member findByEmail(String email);


}
