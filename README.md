# JPA1 & 2
김영한님의 JPA 활용 강의를 듣고 복습 및 정리한 내용을 작성하고 있습니다.


22.01.06
- (강의) 도메인 모델과 테이블 설계 수강
- (강의) 엔티티 클래스 개발 및 실제 코드로 구현 수강
- (강의) 엔티티 설계 시 주의 사항 수강. (다대다 관계, 싱글 테이블, Lazy Laoding, 연관관계의 주인, 연관관계의 편의 메서드)

22.01.07
- (강의) 회원 도메인 개발 수강(회원 Repository, 회원 Service, 회원 Domain Test) 
- (강의) 상품 도메인 개발 수강(상품 Repsitory, 상품 Service)

22.01.08
- (강의) 주문, 주문상품 엔티티 개발
- (강의) 주문 리포지토리 개발
- (강의) 주문 서비스 개발
- (강의) 주문 기능 테스트
- (강의) 주문 검색 기능 개발
- (강의) 홈 화면과 레이아웃
- (강의) 회원 등록, 회원 목록 조회
- (강의) 상품 등록, 상품 목록 조회
- (코드) 도메인 클래스 개발(객체 간 연관관계 설정, 테이블간 연관관계 설정)
- (정리) JPA 프로젝트 복습하며 알게 된 내용, 다시 공부한 내용 정리(https://ojt90902.tistory.com/682,  1~13번)

22.01.09
- (정리) 영속성 전이, 연관관계의 주인에 대해 테스트 코드 작성 후 개념 숙지(https://ojt90902.tistory.com/682,  14~20번)
- (코드) 영속성 전이, 연관관계의 주인에 대해 테스트 코드 작성 후 개념 숙지 
- (코드) 도메인 객체 처음부터 끝까지 다시 개발


22.01.10
- (강의) 상품 수정 컨트롤러 개발.
- (강의) 변경 감지와 병합(merge) 차이 및 서비스 계층 개발
- (강의) 상품 주문 컨트롤러 개발
- (강의) 주문 목록, 검색, 취소 컨트롤러 개발
- (정리) @Transaction 어노테이션에 대한 이해 관련 정리 (https://ojt90902.tistory.com/683)
- (코드) MemberRepository, MemberService 개발
- (코드) MemberRepository 테스트 코드 작성. MemberSerivce 테스트 코드 작성.
- (코드) Item 비즈니스 로직 개발(addStock, removeStock), ItemRepository 개발, ItemService 개발
- (코드) ItemService 테스트 코드 작성  

22.01.11
- (코드) Order, OrderRepository, OrderService 개발.(비즈니스 로직, 생성 메서드, 연관관계 편의 메서드 등)
- (코드) Order, OrderRepository, OrderService 개발 재복습
- (코드) OrderRepository 회원 검색 기능 개발
- (코드) ItemRepository 관련 테스트 코드 
- (코드) HomeController, MemberController 개발
- (코드) ItemController, OrderController 개발. 
- (이슈) OrderController의 검색기능 페이지 오동작. 수정 필요. → select 쿼리 중 join 이상한 것으로 확인됨. QueryDSL등으로 수정 보완이 필요함.
- (코드) OrderCancle 컨트롤러 
- (강의) 회원 저장 API 개발 수강
- (강의) 회원 수정 API 개발 수강
- (강의) 회원 조회 API 개발 수강
- (강의) API 개발 고급 소개 + 조회용 샘플 데이터 코드 입력 수강. 

22.01.12
- (강의) 간단한 주문 조회 API 설계 → 엔티티를 직접 응답함.
- (강의) 간단한 주문 조회 API 설계 → 엔티티를 DTO로 변환

22.01.13
- (강의) 간단한 주문 조회 API 설계 → 엔티티를 DTO로 변환 후 Fetch Join으로 N+1 문제 최적화
- (강의) 간단한 주문 조회 API 설게 : JPA에서 엔티티가 아닌 DTO로 바로 조회 
- (강의) 컬렉션 주문 조회 : 엔티티를 직접 노출하는 API 설계 

22.01.14
- (강의) 컬렉션 주문 조회 : DTO로 변환해서 노출하는 API 

22.01.15
- (강의) 컬렉션 엔티티 직접 노출
- (강의) 컬렉션 엔티티로 검색 후, DTO로 변환해서 리턴
- (강의) 컬렉션 엔티티로 검색 후, DTO로 변환했을 때 발생하는 N+1 문제 Fetch Join으로 해결
- (강의) 엔티티를 DTO로 변환 후, 페이징과 환계 돌파
- (강의) JPA에서 DTO로 직접 조회
- (강의) JPA에서 DTO로 직접 조회하는 부분의 컬렉션 조회 최적화
- (강의) JPA에서 DTO로 직접 조회하는 부분을 플랫화 해서 쿼리 횟수 최적화
- (코드) MemberApiController : API 방식의 회원 조회, 저장, 수정 컨트롤러 개발
- (코드) MemberService : Dirty Check를 이용한 Member Update 로직 개발 
- (정리) MemberApiController / MemverService 구현에 관한 정리 (https://ojt90902.tistory.com/688)
- (코드) SimpleOrderApiController 구현 : Order와 xToOne 연관관계의 변수들만 랩핑해서 반환하는 API 구현 
- (정리) SimpleOrderApiController 구현 관련 정리 (https://ojt90902.tistory.com/690)


22.01.16
- (코드) OrderApiController의 Collection 유형 엔티티를 직접 반환하는 Controller 개발
- (코드) OrderApiController의 Collection 유형을 DTO로 반환하는 Controller 개발
- (코드) OrderApiController의 Collection 유형을 DTO로 반환하며 Fetch Join으로 N+1 문제를 해결하는 API Controller 개발 
