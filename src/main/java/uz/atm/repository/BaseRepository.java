package uz.atm.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import uz.atm.entity.BaseEntity;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/18/22 11:01 AM
 **/

public interface BaseRepository<E extends BaseEntity> extends ReactiveCrudRepository<E, Long> {
}
