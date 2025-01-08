이미지 빌드
docker build --platform linux/arm64 -t image-shop/mysql .

데이터베이스 컨테이너 생성
docker run -d --name imgshop_db -p 13316:3306 -e MYSQL_ROOT_PASSWORD=mypw123 -v imgshop_db:/var/lib/mysql image-shop/mysql