package pl.lodz.p.it.ssbd2024.ssbd03;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HelloRepo extends JpaRepository<HelloEntity, Long> {
}
