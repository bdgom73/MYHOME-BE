package com.myhome.server.Repository.Log;

import com.myhome.server.Entity.Log.LoginLog;
import com.myhome.server.Entity.Member.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoginLogRepository extends JpaRepository<LoginLog,Long> {

    @Query("Select g from LoginLog g where g.member = :member and loginDate between :startDate And :endDate")
    List<LoginLog> findMonthByMember(
            @Param("member") Member member,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
            );

    Slice<LoginLog> findByMember(Member member, Pageable pageable);
}
