package com.tui.proof.data.repository;

import com.tui.proof.data.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {


}