package com.myhome.server.Repository.Member;

import com.myhome.server.Entity.Member.MemberDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberDetailRepository extends JpaRepository<MemberDetail, Long> {

   Optional<MemberDetail> findByEmail(String email);

   @Query("SELECT m FROM Member m where sessionUID = :sessionUID ")
   Optional<MemberDetail> findBySessionUID(@Param("sessionUID") String sessionUID);

   Optional<MemberDetail> findByNickname(String nickname);
}
