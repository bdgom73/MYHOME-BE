package com.myhome.server.Repository.Word;

import com.myhome.server.Entity.Word.FilterText;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FilterTextRepository extends JpaRepository<FilterText, Long> {

    Optional<FilterText> findByKinds(String kinds);
}
