package com.tui.pilotes.data.repository;

import com.tui.pilotes.data.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {


}