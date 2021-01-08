package com.ecommerce.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseService<E,D> {

	@Autowired
	ModelMapper mapper;
	
	protected E convertToEntity(D dto,Class<E> entityClass) {
		return (E) mapper.map(dto, entityClass);
	}
	
	protected D convertToDTO(E entity,Class<D> dto) {
		return (D) mapper.map(entity,dto);
	}
	
	protected void copyDTOPropertiesToEntity(D src,E dest) {
		BeanUtils.copyProperties(src, dest);
	}
	
}
