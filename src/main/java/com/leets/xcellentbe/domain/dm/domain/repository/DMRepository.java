package com.leets.xcellentbe.domain.dm.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.xcellentbe.domain.dm.domain.DM;

public interface DMRepository extends JpaRepository<DM, UUID> {
}
