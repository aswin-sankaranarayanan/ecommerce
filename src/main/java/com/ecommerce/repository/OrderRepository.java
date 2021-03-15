package com.ecommerce.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.User;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long>{
	
	List<Order>findAllByUserAndStatusContaining(User user, String status,Sort sort);
	Page<Order> findAllByStatusContaining(String status,Pageable pageable);
	
	@Query("select totalPrice from Order where id=:orderId")
	Long findTotalPriceById(@Param("orderId") Long id);
	
	@Modifying @Query(value="update Order set status=:status where id=:orderId")
	void upadateOrderStatusToPlaces(@Param("orderId") Long orderId,@Param("status") String status);

	@Query(value="with parent as  "
			+ "(select o.id, "
			+ "o.created_date, "
			+ "od.item,od.price from orders o inner join order_details od on o.id=od.order_fk where user_fk=:userId and o.status='PLACED'), "
			+ "child as "
			+ "(select inv.item, images.image_path from inventory inv inner join  inventory_images images on inv.id = images.inventory_fk) "
			+ "select parent.*,date_format(parent.created_date,'%d-%m-%Y %H:%i') createdDate,child.image_path as imagePath from parent join child on parent.item = child.item ORDER BY created_date DESC",nativeQuery = true)
	public List<OrderProjection> getOrdersForUser(@Param("userId")Long userId);
	
	
	@Query(value="select o.id,inv.item,o.price,date_format(o.created_date,'%d-%m-%Y %H:%i') createdDate,image_path as imagePath from "
			+" (select o.id, o.created_date, od.item,od.price from orders o join order_details od on o.id=od.order_fk where o.id=:orderId) o "
			+" join inventory inv on inv.item=o.item "
			+" join inventory_images image on inv.id=image.inventory_fk",nativeQuery=true)
	public List<OrderProjection> getOrderDetails(@Param("orderId") Long orderId);
}
