create database imgshop_db;

use imgshop_db;

-- 코드 그룹 테이블
CREATE TABLE code_group
(
    group_code CHAR(3)     NOT NULL PRIMARY KEY COMMENT '그룹 코드',
    group_name VARCHAR(30) NOT NULL COMMENT '그룹명',
    use_yn     CHAR(1)     NOT NULL DEFAULT 'Y' COMMENT '사용 여부',
    is_deleted BOOLEAN              DEFAULT FALSE COMMENT '삭제 여부',
    created_at TIMESTAMP            DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'
);

-- 코드 상세 테이블
CREATE TABLE code_detail
(
    group_code CHAR(3)     NOT NULL COMMENT '그룹 코드',
    code_value CHAR(3)     NOT NULL COMMENT '코드값',
    code_name  VARCHAR(30) NOT NULL COMMENT '코드명',
    sort_seq   INTEGER     NOT NULL COMMENT '정렬순서',
    use_yn     CHAR(1)     NOT NULL DEFAULT 'Y' COMMENT '사용 여부',
    is_deleted BOOLEAN              DEFAULT FALSE COMMENT '삭제 여부',
    created_at TIMESTAMP            DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    PRIMARY KEY (group_code, code_value),
    FOREIGN KEY (group_code) REFERENCES code_group (group_code)
);

-- 회원 테이블
CREATE TABLE member
(
    user_no        BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 번호',
    user_id        VARCHAR(50)  NOT NULL COMMENT '사용자 ID',
    user_pw        VARCHAR(100) NOT NULL COMMENT '비밀번호 (암호화)',
    user_name      VARCHAR(100) NOT NULL COMMENT '사용자명',
    job_group_code CHAR(3)      NOT NULL DEFAULT '000' COMMENT '직업그룹코드',
    job            CHAR(3)      NOT NULL DEFAULT '000' COMMENT '직업코드',
    coin           INTEGER               DEFAULT 0 COMMENT '보유 코인',
    status         VARCHAR(10)           DEFAULT 'ACTIVE' COMMENT '상태(ACTIVE/INACTIVE/SUSPENDED)',
    last_login_at  TIMESTAMP    NULL COMMENT '최종 로그인 시간',
    is_deleted     BOOLEAN               DEFAULT FALSE COMMENT '삭제 여부',
    created_at     TIMESTAMP             DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at     TIMESTAMP             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    UNIQUE KEY uk_member_user_id (user_id),
    FOREIGN KEY (job_group_code, job) REFERENCES code_detail (group_code, code_value)
);

-- 회원 권한 테이블
CREATE TABLE member_auth
(
    auth_no    BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '권한 번호',
    user_no    BIGINT      NOT NULL COMMENT '사용자 번호',
    auth       VARCHAR(50) NOT NULL COMMENT '권한',
    is_deleted BOOLEAN   DEFAULT FALSE COMMENT '삭제 여부',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    FOREIGN KEY (user_no) REFERENCES member (user_no)
);

-- 게시판 테이블
CREATE TABLE board
(
    board_no   BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '게시글 번호',
    title      VARCHAR(200) NOT NULL COMMENT '제목',
    content    LONGTEXT COMMENT '내용',
    writer     VARCHAR(50)  NOT NULL COMMENT '작성자',
    view_count INTEGER   DEFAULT 0 COMMENT '조회수',
    is_deleted BOOLEAN   DEFAULT FALSE COMMENT '삭제 여부',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    INDEX idx_board_created_at (created_at)
);

-- 공지사항 테이블
CREATE TABLE notice
(
    notice_no  BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '공지사항 번호',
    title      VARCHAR(2000) NOT NULL COMMENT '제목',
    content    LONGTEXT COMMENT '내용',
    view_count INTEGER   DEFAULT 0 COMMENT '조회수',
    is_deleted BOOLEAN   DEFAULT FALSE COMMENT '삭제 여부',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    INDEX idx_notice_created_at (created_at)
);

-- 상품 테이블
CREATE TABLE item
(
    item_id     BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '상품 ID',
    item_name   VARCHAR(100)   NOT NULL COMMENT '상품명',
    price       DECIMAL(10, 2) NOT NULL COMMENT '가격',
    description TEXT COMMENT '설명',
    picture_url VARCHAR(200) COMMENT '이미지 URL',
    status      VARCHAR(10) DEFAULT 'ACTIVE' COMMENT '상태(ACTIVE/SOLDOUT/INACTIVE)',
    is_deleted  BOOLEAN     DEFAULT FALSE COMMENT '삭제 여부',
    created_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    INDEX idx_item_status (status)
);

-- 자료실 테이블
CREATE TABLE pds
(
    pds_id      BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '자료 ID',
    pds_name    VARCHAR(100) NOT NULL COMMENT '자료명',
    description TEXT COMMENT '설명',
    view_count  INTEGER     DEFAULT 0 COMMENT '조회수',
    status      VARCHAR(10) DEFAULT 'ACTIVE' COMMENT '상태(ACTIVE/INACTIVE)',
    is_deleted  BOOLEAN     DEFAULT FALSE COMMENT '삭제 여부',
    created_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    INDEX idx_pds_status (status)
);

-- 자료실 파일 테이블
CREATE TABLE pds_file
(
    file_id        BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '파일 ID',
    pds_id         BIGINT       NOT NULL COMMENT '자료 ID',
    file_name      VARCHAR(150) NOT NULL COMMENT '파일명',
    file_size      BIGINT       NOT NULL COMMENT '파일크기',
    mime_type      VARCHAR(100) COMMENT 'MIME 타입',
    download_count INTEGER   DEFAULT 0 COMMENT '다운로드 수',
    is_deleted     BOOLEAN   DEFAULT FALSE COMMENT '삭제 여부',
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    FOREIGN KEY (pds_id) REFERENCES pds (pds_id),
    INDEX idx_pds_file_pds_id (pds_id)
);

-- 사용자 구매 상품 테이블
CREATE TABLE user_item
(
    user_item_id   BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 상품 ID',
    user_no        BIGINT         NOT NULL COMMENT '사용자 번호',
    item_id        BIGINT         NOT NULL COMMENT '상품 ID',
    purchase_price DECIMAL(10, 2) NOT NULL COMMENT '구매 시점 가격',
    is_deleted     BOOLEAN   DEFAULT FALSE COMMENT '삭제 여부',
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    FOREIGN KEY (user_no) REFERENCES member (user_no),
    FOREIGN KEY (item_id) REFERENCES item (item_id),
    UNIQUE KEY uk_user_item (user_no, item_id),
    INDEX idx_user_item_user_no (user_no)
);

-- 코인 충전 내역
CREATE TABLE charge_coin_history
(
    history_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '충전 내역 ID',
    user_no    BIGINT  NOT NULL COMMENT '사용자 번호',
    amount     INTEGER NOT NULL COMMENT '충전 금액',
    is_deleted BOOLEAN   DEFAULT FALSE COMMENT '삭제 여부',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    FOREIGN KEY (user_no) REFERENCES member (user_no),
    INDEX idx_charge_user_no (user_no)
);

-- 코인 지급 내역
CREATE TABLE pay_coin_history
(
    history_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '지급 내역 ID',
    user_no    BIGINT  NOT NULL COMMENT '사용자 번호',
    item_id    BIGINT  NOT NULL COMMENT '상품 ID',
    amount     INTEGER NOT NULL COMMENT '지급 금액',
    is_deleted BOOLEAN   DEFAULT FALSE COMMENT '삭제 여부',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    FOREIGN KEY (user_no) REFERENCES member (user_no),
    FOREIGN KEY (item_id) REFERENCES item (item_id),
    INDEX idx_pay_user_no (user_no)
);

-- 접근 로그 테이블 (파티셔닝 적용)
CREATE TABLE access_log
(
    log_id            BIGINT       NOT NULL COMMENT '로그 ID',
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    user_no           BIGINT COMMENT '사용자 번호',
    request_uri       VARCHAR(200) NOT NULL COMMENT '요청 URI',
    class_name        VARCHAR(100) NOT NULL COMMENT '클래스명',
    class_simple_name VARCHAR(50)  NOT NULL COMMENT '단순 클래스명',
    method_name       VARCHAR(100) NOT NULL COMMENT '메서드명',
    remote_addr       VARCHAR(50)  NOT NULL COMMENT '접속 IP',
    PRIMARY KEY (created_at, log_id),
    INDEX idx_access_log_id (log_id)
) PARTITION BY RANGE (UNIX_TIMESTAMP(created_at)) (
    PARTITION p_2024 VALUES LESS THAN (UNIX_TIMESTAMP('2025-01-01 00:00:00')),
    PARTITION p_2025 VALUES LESS THAN (UNIX_TIMESTAMP('2026-01-01 00:00:00')),
    PARTITION p_max VALUES LESS THAN MAXVALUE
    );

-- 성능 로그 테이블 (파티셔닝 적용)
CREATE TABLE performance_log
(
    log_id              BIGINT       NOT NULL COMMENT '로그 ID',
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    signature_name      VARCHAR(50)  NOT NULL COMMENT '시그니처명',
    signature_type_name VARCHAR(100) NOT NULL COMMENT '시그니처 타입명',
    duration_time       BIGINT    DEFAULT 0 COMMENT '소요시간',
    PRIMARY KEY (created_at, log_id),
    INDEX idx_performance_log_id (log_id)
) PARTITION BY RANGE (UNIX_TIMESTAMP(created_at)) (
    PARTITION p_2024 VALUES LESS THAN (UNIX_TIMESTAMP('2025-01-01 00:00:00')),
    PARTITION p_2025 VALUES LESS THAN (UNIX_TIMESTAMP('2026-01-01 00:00:00')),
    PARTITION p_max VALUES LESS THAN MAXVALUE
    );