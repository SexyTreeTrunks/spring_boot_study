package org.zerock.vo;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.Getter;

@Getter
public class PageVO {
	private static final int DEFAULT_SIZE = 7;
	private static final int DEFAULT_MAX_SIZE = 50;
	
	private int page;
	private int size;
	
	private String keyword;
	private String type;
	
	public PageVO() {
		this.page = 1;
		this.size = DEFAULT_SIZE;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setPage(int page) {
		this.page = (page < 1)? 1: page;
	}
	
	public void setSize(int size) {
		this.size = (size < 1 || size > DEFAULT_MAX_SIZE)? DEFAULT_SIZE: size;
	}
	
	public Pageable makePageable(int direction, String... props) {
		Sort.Direction dir = direction == 0? Sort.Direction.DESC : Sort.Direction.ASC;
		
		return PageRequest.of(this.page - 1, this.size, dir, props);
	}
}
