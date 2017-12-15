package com.spacerovka;

import com.spacerovka.entity.Box;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Svetotulichka on 14.12.2017.
 */
public interface BoxDAO extends JpaRepository<Box, Long> {

    public Box save(Box box);
    public Box findOne(Long id);
}
