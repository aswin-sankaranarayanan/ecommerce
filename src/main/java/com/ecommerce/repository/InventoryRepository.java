package com.ecommerce.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.ecommerce.entity.Inventory;

public interface InventoryRepository extends CrudRepository<Inventory, Long> {
	Page<Inventory> findAll(Pageable pageable);
	
	@Query(value = "SELECT i FROM Inventory i WHERE i.item LIKE CONCAT('%',:filter,'%') or i.description LIKE CONCAT('%',:filter,'%')"
			+ " or i.subCategory.subCategory LIKE CONCAT('%',:filter,'%')")
	Page<Inventory> filterInvetory(@Param("filter") String filter,Pageable pageable);
	
	@Query(value="SELECT O.*, IMAGES.IMAGE_PATH AS imagePath FROM "
			+ "(SELECT INV.id as id,INV.item as item,INV.price as price,INV.description as description,SC.SUB_CATEGORY as subCategory FROM  (SELECT  ID,ITEM,DESCRIPTION,PRICE,SUB_CATEGORY_FK, row_number() over ( partition by sub_category_fk order by id asc )  as ROW_NUM "
			+ " FROM inventory where category_fk=:id) AS INV "
			+ " JOIN SUB_CATEGORY SC ON INV.SUB_CATEGORY_FK=SC.ID WHERE INV.ROW_NUM<=10) O "
			+ " JOIN INVENTORY_IMAGES IMAGES ON O.ID = IMAGES.INVENTORY_FK",nativeQuery = true)
	Collection<InventoryProjection> getInventoryProjection(@Param("id")Long categoryId);

	Optional<Inventory> findByItem(String item);
	
	@Query(value="Select INVENTORY.id,INVENTORY.DESCRIPTION,INVENTORY.ITEM,INVENTORY.PRICE,IMG.IMAGE_PATH as imagePath FROM INVENTORY JOIN ( "
			+ "	 SELECT ID,SUB_CATEGORY FROM SUB_CATEGORY  "
			+ "	 WHERE  SUB_CATEGORY=:SubCategory) SC "
			+ "	 ON INVENTORY.SUB_CATEGORY_FK=SC.ID "
			+ "	 JOIN INVENTORY_IMAGES IMG ON INVENTORY.ID=IMG.INVENTORY_FK ORDER BY INVENTORY.ITEM ASC",
			countProjection = "*",
			nativeQuery=true)
	Page<InventoryProjection> getItemsBySubCategory(@Param("SubCategory") String subCategory,Pageable pageable);
	
	
	@Query(value="WITH  "
			+ "PARENT  AS (SELECT INV.ID,INV.ITEM,INV.DESCRIPTION,INV.PRICE,IMG.IMAGE_PATH FROM INVENTORY INV JOIN INVENTORY_IMAGES IMG ON INV.ID=IMG.INVENTORY_FK WHERE  "
			+ "INV.ITEM LIKE :searchString or upper(INV.DESCRIPTION) LIKE upper(:searchString) ORDER BY INV.ID), "
			+ "CHILD1 AS  "
			+ "( "
			+ "SELECT DISTINCT O1.ID,O1.ITEM,O1.DESCRIPTION,O1.PRICE,IMG.IMAGE_PATH FROM( "
			+ "SELECT INV.* FROM INVENTORY INV   WHERE INV.CATEGORY_FK IN (  "
			+ "SELECT C.ID FROM CATEGORY C "
			+ "WHERE upper(C.CATEGORY_NAME) LIKE upper(:searchString)) "
			+ ") O1 "
			+ "JOIN INVENTORY_IMAGES IMG ON O1.ID=IMG.INVENTORY_FK "
			+ "ORDER BY O1.ID "
			+ "), "
			+ "CHILD2 AS "
			+ "( "
			+ "SELECT O2.ID,O2.ITEM,O2.DESCRIPTION,O2.PRICE,IMG.IMAGE_PATH FROM( "
			+ "SELECT INV.* FROM INVENTORY INV  WHERE INV.SUB_CATEGORY_FK IN "
			+ "(SELECT SC.ID FROM SUB_CATEGORY SC WHERE upper(SC.SUB_CATEGORY) LIKE upper(:searchString)))O2 "
			+ " JOIN INVENTORY_IMAGES IMG ON O2.ID=IMG.INVENTORY_FK "
			+ " ORDER BY O2.ID "
			+ " ) "
			+ " SELECT ID,ITEM,DESCRIPTION,PRICE,IMAGE_PATH as imagePath FROM "
			+ " (SELECT * FROM PARENT UNION  "
			+ " SELECT * FROM CHILD1) O UNION "
			+ " (SELECT * FROM CHILD2)  ORDER BY ID;",nativeQuery = true)
	List<InventoryProjection> searchItem(@Param("searchString") String searchString);
	
}



