package org.zerock.persistence;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.zerock.domain.FreeBoard;

//게시글과 댓글은 작성 및 수정이 각자 별도로 이루어지므로 Repository를 각자 구성한다
public interface FreeBoardRepository extends PagingAndSortingRepository<FreeBoard, Long>{
	public List<FreeBoard> findByBnoGreaterThan(Long bno, Pageable page);
	
	@Query("select b.bno, b.title, count(r) from FreeBoard b LEFT OUTER JOIN b.replies r "
			+ "where b.bno > 0 group by b")
	public List<Object[]> getPage(Pageable page);
	
	//Property or field cannot be found on object of type 'org.springframework.data.domain.PageImpl' 에러 뜸. 이렇게는 못하는듯. 검색조건을 항상 같이 넣어주도록 메소드 짜야겠음
	//public List<FreeBoard> findAll(Pageable page);
}
