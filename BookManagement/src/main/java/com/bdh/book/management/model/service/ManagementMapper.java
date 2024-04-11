package com.bdh.book.management.model.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bdh.book.management.model.dto.Book;

@Mapper
public interface ManagementMapper {

	/** 책 전체 조회
	 * @return bookList
	 */
	List<Book> selectAll();

	/** 책 등록
	 * @param inputBook
	 * @return result
	 */
	int bookInsert(Book inputBook);

	/** 책 검색
	 * @param inputTitle
	 * @return bookList
	 */
	List<Book> bookSearch(String inputTitle);

	/** 책 가격 수정
	 * @param book
	 * @return result
	 */
	int bookUpdatePrice(Book book);

	/** 책 삭제
	 * @param bookNo
	 * @return result
	 */
	int bookDelete(int bookNo);

}
