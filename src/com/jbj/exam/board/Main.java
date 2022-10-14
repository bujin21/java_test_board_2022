package com.jbj.exam.board;

import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
	static void makeTestData(List<Article> articles) {
		
		for ( int i = 0; i < 100; i++ ) {
		      int id = i + 1;
		      articles.add(new Article(id, "제목" + id, "내용" + id));
		    }
	}

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("== 게시판 v 0.1 ==");
		System.out.println("== 프로그램 시작 ==");
		
		int articleLastId = 0;
		
		List<Article> articles = new ArrayList<>();
		
		makeTestData(articles);
		
		if (articles.size() > 0) {
			articleLastId = articles.get(articles.size() - 1).id;
		}

		while (true) {
			
			System.out.printf("명령) ");
			
			String cmd = sc.nextLine();
			
			Rq rq = new Rq(cmd);
			
			Map<String, String> params = rq.getParams();
			
			if (rq.getUrlPath().equals("exit")) {
				break;
				
			} else if (rq.getUrlPath().equals("/usr/article/list")) {
				actionUsrArticleList(rq, articles);
			} else if (rq.getUrlPath().equals("/usr/article/detail")) {
				actionUsrArticleDetail(rq, articles);
			} else if (rq.getUrlPath().equals("/usr/article/write")) {
				actionUsrArticleWrite(rq, sc, articles, articleLastId);
			} else if (rq.getUrlPath().equals("/usr/article/modify")) {
			        actionUsrArticleModify(rq, sc, articles);
			} else if (rq.getUrlPath().equals("/usr/article/delete")) {
		        actionUsrArticleDelete(rq, sc, articles);
			} else {
				System.out.printf("입력 된 명령어 : %s\n", cmd);
			}
		}
		System.out.println("== 프로그램 끝 ==");
		sc.close();
	}

	private static void actionUsrArticleDelete(Rq rq, Scanner sc, List<Article> articles) {
		Map<String , String> params = rq.getParams();

		if (params.containsKey("id") == false) {
			System.out.println("id를 입력해주세요.");
			return;
		}
		
		int id = 0;

		try {
			id = Integer.parseInt(params.get("id"));
			System.out.println(id);
		} catch (NumberFormatException e) {
			System.out.println("id를 정수 형태로 입력해주세요.");
			return;
		}
		if (articles.isEmpty()) {
			
			System.out.println("게시물이 존재하지 않습니다.");
			return;
		}
		
		Article article = articles.get(id - 1);
		
		if (id > articles.size()) {
			System.out.println("게시물이 존재하지 않습니다.");
			return;
		}
		
		articles.remove(article);
		
		System.out.printf("%d번 게시물이 삭제되었습니다.\n", article.id);
		
	}

	private static void actionUsrArticleModify(Rq rq, Scanner sc, List<Article> articles) {
		Map<String , String> params = rq.getParams();
		
		if (params.containsKey("id") == false) {
			System.out.println("id를 입력해주세요.");
			return;
		}
		
		int id = 0;

		try {
			id = Integer.parseInt(params.get("id"));
			System.out.println(id);
		} catch (NumberFormatException e) {
			System.out.println("id를 정수 형태로 입력해주세요.");
			return;
		}
		if (articles.isEmpty()) {
			
			System.out.println("게시물이 존재하지 않습니다.");
			return;
		}
		
		Article article = articles.get(id - 1);
		
		if (id > articles.size()) {
			System.out.println("게시물이 존재하지 않습니다.");
			return;
		}
		
		System.out.printf("새 제목 : ");
		article.title = sc.nextLine();
		System.out.printf("새 내용 : ");
		article.body = sc.nextLine();
		
		System.out.printf("%d번 게시물이 수정되었습니다.\n", id);
		
	}

	private static void actionUsrArticleWrite(Rq rq, Scanner sc, List<Article> articles, int articleLastId) {
		System.out.println("- 게시물 등록 -");
		System.out.printf("제목 : ");
		String title = sc.nextLine();
		System.out.printf("내용 : ");
		String body = sc.nextLine();
		
		int id = ++articleLastId;
		
		Article article = new Article(id, title, body);
		
		articles.add(article);
		
		System.out.println("생성된 게시물 객체 : " + article);
		
		System.out.printf("%d번 게시물이 입력되었습니다.\n", article.id);
		
	}

	private static void actionUsrArticleDetail(Rq rq, List<Article> articles) {
		Map<String , String> params = rq.getParams();
		
		if (params.containsKey("id") == false) {
			System.out.println("id를 입력해주세요.");
			return;
		}
		
		int id = 0;

		try {
			id = Integer.parseInt(params.get("id"));
			System.out.println(id);
		} catch (NumberFormatException e) {
			System.out.println("id를 정수 형태로 입력해주세요.");
			return;
		}
		if (articles.isEmpty()) {
			
			System.out.println("게시물이 존재하지 않습니다.");
			return;
		}
		
		Article article = articles.get(id - 1);
		
		if (id > articles.size()) {
			System.out.println("게시물이 존재하지 않습니다.");
			return;
		}
		
		System.out.println("- 게시물 상세 내용 -");
		System.out.printf("번호 : %d\n", article.id);
		System.out.printf("제목 : %s\n", article.title);
		System.out.printf("내용 : %s\n", article.body);
	}

	private static void actionUsrArticleList(Rq rq, List<Article> articles) {
		System.out.println("- 게시물 리스트 -");
		System.out.printf("------------------\n");
		System.out.printf("번호 / 제목\n");
		System.out.printf("------------------\n");
		
		Map<String , String> params = rq.getParams();

		// 검색 시작
        List<Article> filteredArticles = articles;


        if ( params.containsKey("searchKeyword") ) {
          String searchKeyword = params.get("searchKeyword");

          filteredArticles = new ArrayList<>();

	        for ( Article article : articles ) {
	          boolean matched = article.title.contains(searchKeyword) || article.body.contains(searchKeyword);

	          if ( matched ) {
	            filteredArticles.add(article);
	          }
	        }
        }

        List<Article> sortedArticles = filteredArticles;

		boolean orderByIdDesc = true;
		
		if (params.containsKey("orderBy") && params.get("orderBy").equals("idAsc")) {
			orderByIdDesc = false;
		}

		// 리스트 출력시 정순, 역순 순회
		if (orderByIdDesc) {
			sortedArticles = Util.reverseList(sortedArticles);
		}

		for (Article article : sortedArticles) {
			System.out.printf("%d / %s\n", article.id, article.title);
		}
		
	}
}

class Article {
	
	int id;
	String title;
	String body;
	String 테스트;

	Article(int id_, String title_, String body_) {
		this.id = id_;
		this.title = title_;
		this.body = body_;
	}

	@Override
	public String toString() {
		return String.format("{id : %d, title : \"%s\", body : \"%s\"}", id, title, body);
	}
}

class Rq {
	
	String url;
	Map<String, String> params;
	String urlPath;

	Rq(String url) {
		
		this.url = url;
		params = Util.getParamsFromUrl(this.url);
		urlPath = Util.getUrlPathFromUrl(this.url);
	}

	public Map<String, String> getParams() {
		return params;
	}

	public String getUrlPath() {
		return urlPath;
	}
}

class Util {
	static Map<String, String> getParamsFromUrl(String url) {
		Map<String, String> params = new HashMap<>();
		
		String[] urlBits = url.split("\\?", 2);
		
		if (urlBits.length == 1) {
			return params;
			
		}
		for (String bit : urlBits[1].split("&")) {
			String[] bitBits = bit.split("=", 2);
			if (bitBits.length == 1) {
				continue;
			}
			params.put(bitBits[0], bitBits[1]);
		}
		return params;
	}

	static String getUrlPathFromUrl(String url) {
		return url.split("\\?", 2)[0];
	}

	// 이 함수는 원본리스트를 훼손하지 않고, 새 리스트를 만듭니다. 즉 정렬이 반대인 복사본리스트를 만들어서 반환합니다.
	public static <T> List<T> reverseList(List<T> list) {
		List<T> reverse = new ArrayList<>(list.size());

		for (int i = list.size() - 1; i >= 0; i--) {
			reverse.add(list.get(i));
		}
		return reverse;
	}
}