package com.snix.gallery.repository;

import com.snix.gallery.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Item > repository, Integer > items.ID
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByIdIn(List<Integer> ids);
}
