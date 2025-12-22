package org.ikitadevs.authservice.repository;


import org.ikitadevs.authservice.model.ProcessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProcessLogsRepository extends JpaRepository<ProcessLog, UUID> {

}
