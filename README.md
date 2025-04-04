# 💬 Java TCP/IP 기반 에코 채팅 애플리케이션
<br>

## 📖 프로젝트 소개

이 프로젝트는 **TCP/IP 소켓 통신**을 이용한 **에코 채팅 애플리케이션**입니다.  
클라이언트는 사용자 이름, 에코 옵션, 메시지를 입력하여 서버로 전송하고, 서버는 옵션에 따라 메시지를 가공해 다시 클라이언트로 응답합니다.  
**JSON 포맷 기반 패킷 통신**을 설계하고, 에러 처리 또한 정형화된 구조로 응답하여 견고한 통신 구조를 갖춘 것이 특징입니다.

> 💡 `Java`, `Socket`, `JSON`, `Gradle` 기반으로 구현된 간단하지만 학습에 적합한 네트워크 응용 프로그램입니다.

---
<br>

## ⚙️ 개발 환경

| 항목 | 내용 |
|------|------|
| Language | Java 21 |
| IDE | IntelliJ IDEA |
| Build Tool | Gradle |
| Encoding | UTF-8 |
| 실행 방식 | CLI 실행 또는 `gradlew run` |

---
<br>

## 🛠️ 사용한 기술 스택

| 구분 | 기술 |
|------|------|
| 네트워크 통신 | Java `Socket`, `DataInputStream`, `DataOutputStream` |
| JSON 처리 | `Jackson ObjectMapper` (`jackson-databind`) |
| 패킷 구조화 | DTO 설계 (`Request`, `Response`, `Error`) |
| 빌드 및 실행 | Gradle + `application` plugin |

---
<br>

## 🗂 프로젝트 구조
```
WebSocketProject/
├── .gitignore
├── build.gradle
├── gradlew
├── gradlew.bat
├── settings.gradle
│
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── me/
│   │   │       └── juhye0k/
│   │   │           ├── TcpIpClient.java             # 클라이언트 메인 실행 파일
│   │   │           ├── TcpIpServer.java             # 서버 메인 실행 파일
│   │   │           │
│   │   │           ├── dto/
│   │   │           │   ├── MessagePacketRequest.java     # 요청 메시지 DTO
│   │   │           │   ├── MessagePacketResponse.java    # 응답 메시지 DTO
│   │   │           │   └── ErrorResponse.java            # 오류 응답 DTO
│   │   │           │
│   │   │           └── error/
│   │   │               └── ErrorCode.java                # 오류 코드 Enum 정의
│   │   │
│   │   └── resources/
│   │      
│
└── test/
    

```

---
<br>

## 🧩 주요 기능

### ✅ 에코 채팅 기능
- 메시지를 입력하면 서버에서 옵션에 따라 가공하여 응답함

| 옵션 번호 | 설명             |
|-----------|------------------|
| 1         | 일반 에코         |
| 2         | 대문자로 변환     |
| 3         | 소문자로 변환     |
| 4         | 종료 요청 (접속 종료) |

### ✅ 예외 처리 기능
- 사용자가 잘못된 옵션을 입력한 경우 서버에서 에러 메시지 반환
- 에러는 `errorCode`와 `errorMessage`로 구성된 JSON 형식으로 전송됨

---
<br>

## 🧑‍💻 사용 방법

### 1. 프로젝트 실행

#### ✅ IntelliJ IDEA에서 실행하기

1. IntelliJ 실행 → `Open`을 클릭하여 프로젝트 디렉토리 열기
2. Gradle 의존성 자동 로드 확인
3. `TcpIpServer.java` 실행 → 서버 대기 상태 진입
4. `TcpIpClient.java` 실행 → 사용자 입력 시작
5. 클라이언트 콘솔창에서 옵션 및 메시지 입력

#### ✅ CLI에서 실행하기
```
bash
# Gradle 빌드 후 실행
./gradlew run        # Unix/macOS
gradlew run          # Windows

# 또는 jar로 실행
./gradlew build
java -jar build/libs/프로젝트이름-1.0-SNAPSHOT.jar
```
<br>

## 🖥️ 실행 화면

### ✅ 클라이언트 실행 예시
![image](https://github.com/user-attachments/assets/8d5b49b1-7257-4813-96cf-45bf88d83e83)


### ✅ 서버 실행 예시
![image](https://github.com/user-attachments/assets/ad8083e7-4221-480e-ab52-d2c5c8382ac4)

<br>

## ⚠️ 예외 상황 코드 정리

| 에러 코드 | 설명                          | 발생 조건 예시                     |
|-----------|-------------------------------|------------------------------------|
| `401`     | 0 이하의 정수는 입력 불가      | `echoOption <= 0` (예: 입력: 0)     |
| `402`     | 정수가 아닌 실수는 입력 불가   | 실수 입력 (예: 입력: 1.1)           |
| `403`     | 5 이상의 정수는 입력 불가      | `echoOption >= 5` (예: 입력: 5)     |
| `404`     | 문자는 입력할 수 없습니다       | 문자열 입력 (예: 입력: hello)       |
| `500`     | 서버 처리 중 예외 발생         | 서버에서 예외(Exception) 발생 시    |

