package com.ejlchina.project.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlatformEntity extends JpaRepository<Platform, Long> {

    Platform findByName(String name);

    List<Platform> findAllByIdIn(List<Long> ids);

}
