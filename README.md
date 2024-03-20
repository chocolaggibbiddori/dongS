# Welcome to DongS!

## 현재 버전

**v1.1.1**

## 프로젝트 소개

&nbsp;&nbsp; 이 프로젝트는 중복 일정 체크를 쉽게 해주는 기능을 갖추고 있습니다. 장소 대여와 같이 서로의 일정이 겹치지 않는지 체크할 때 유용하게 사용될 수 있습니다.

## 사용 방법

### 프로젝트 다운로드

   ```shell
   git clone https://github.com/chocolaggibbiddori/dongS.git
   ```

### data.csv 파일 생성

- 경로: `src/main/resources/data.csv`

### data.csv 파일 작성

&nbsp;&nbsp; 모든 일정을 data.csv 파일에 작성한다.  

> **형식**
> 
> 타이틀|일정 시작 날짜|일정 끝 날짜|일정 요일|일정 시작 시각|일정 끝 시각
> - 날짜 형식: yyyy-MM-dd
> - 일정 시작 날짜: 해당 일정이 시작되는 날짜 (ex. 3월 1일부터 5월 30일까지의 일정이라면 3월 1일에 해당함)
> - 일정 끝 날짜: 해당 일정이 끝나는 날짜 (ex. 3월 1일부터 5월 30일까지의 일정이라면 5월 30일에 해당함)
> - 일정 요일: 월, 화, 수, 목, 금, 토, 일 중 하나
> - 시각 형식: hh:mm (hh = 00 ~ 23, mm = 00 ~ 59)
> - 일정 시작 시각: 해당 일정이 시작되는 시각 (ex. 오후 3시부터 8시 30분까지의 일정이라면 3시에 해당함)
> - 일정 끝 시각: 해당 일정이 끝나는 시각 (ex. 오후 3시부터 8시 30분까지의 일정이라면 8시 30분에 해당함)

Example:

   ```csv
   [2청] 김종순 소모임|2024-02-01|2099-12-31|월|20:00|22:00
   [2청] 기초반|2024-03-14|2024-05-16|목|19:30|22:30
   [2청] 큐티반|2024-03-13|2024-05-08|수|10:00|12:30
   [2청] 내가 보는 세상, 세상이 보는 나|2024-03-16|2024-05-25|토|19:30|22:00
   [2청] 안내팀 모임|2024-03-02|2024-03-02|토|14:00|23:59
   [2청] 홀챌|2024-03-19|2024-03-19|화|20:00|21:00
   [2청] 목자모임|2024-04-14|2024-04-14|일|18:10|20:00
   [2청] 우동목장 아웃팅|2024-03-17|2024-03-17|일|18:15|23:59
   ```

### 프로젝트 실행

&nbsp;&nbsp; 프로젝트를 실행한다. 실행 파일은 `src/main/java/com/dongs/Main.java`

### 프로젝트 결과 확인

#### Success

&nbsp;&nbsp; 콘솔에 아무런 에러 메시지 없이 `Finish!` 문구만 출력된다면, 모든 일정이 겹치지 않는다는 뜻이다.

#### Fail

&nbsp;&nbsp; 콘솔에 에러 메시지가 표시된다. 라인 넘버를 확인하여 어느 줄의 데이터에 오류가 발생했는지 확인할 수 있다.

|    에러 유형     |                                                        로그 예                                                        |
|:------------:|:------------------------------------------------------------------------------------------------------------------:|
|  데이터 개수 부족   |                           Illegal data! Please fill in the appropriate data in the form                            |
| 날짜, 시간 형식 오류 | Illegal data! The date or time format is incorrect. The date format is 'yyyy-MM-dd' and the time format is 'hh:mm' |
|   날짜 순서 오류   |                          Illegal startDate and endDate! startDate must be before endDate                           |
|   요일 입력 오류   |                           Illegal dayOfWeek [수4]. Write it correctly in [월,화,수,목,금,토,일].                           |
|  일정 끝 날짜 오류  |                                  Illegal endDate [2024-03-17]. It's already over                                   |
|   시간 순서 오류   |                          Illegal startTime and endTime! startTime must be before endTime                           |