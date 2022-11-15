# ForestStory

**Description :** 숲 해설가 해설 듣기  
**Contributor :** `BulTTaeng(BulTTaeng)`,  

### History

<details>
<summary>접기/펼치기</summary><br>

`2022.08.30`  
- Init
- 기본적인 firebase 연동
- 로그아웃 수정
- SignUp

`2022.08.31`  
- Login
- 음악 플레이어(MediaStore)

`2022.09.01`  
- Logout
- Exoplayer

`2022.09.02`  
- PendingIntent
- PlayerNotificationManager

`2022.09.05`  
- 한개만 선택 되는 recyclerView 추가
- 해설 파일 불러오는 코드 추가
- 여러개의 음원 play 추가

`2022.09.06`  
- 지정한 음악 play 및 notification bar , detail page 정보 update
- 뒤로가기로 Exoplayer release

`2022.09.08`  
- Google Login 구현 
- 로그아웃 구현

`2022.09.13`  
- Setting Page recyclerview 구현
- 산 터치 이벤트 적용
- 거리 순 정렬 추가

`2022.09.14`  
- 회원 탈퇴 추가
- repository Live data -> Flow

`2022.09.15`  
- audioPlayer Fragment UI 수정

`2022.09.19`  
- 숲 해설가 페이지 추가
- 숲 해설가 data load
- lifecycle 문제 해결

`2022.09.20`  
- 숲 해설가 프로파일 페이지 추가
- 숲 해설가 프로파일 페이지에서 audioActivity 라우팅 추가
- 숲 해설가의 audio 파일, mountain 만 불러오는 로직 추가(mountain , audio ViewModel)

`2022.09.21`  
- LiveData + Observe -> EventFlow + emit + collect
- commentatorReservationPage 구현

`2022.09.22`  
- 검색 기능 이름 -> hashTag
- popupMenu 추가

`2022.09.23`  
- 해설가 검색 페이지 구현
- hashTag show

`2022.09.26`  
- 숲 해설가 예약페이지 캘린더 구현
- 산 고르기 구현

`2022.09.27`  
- firebase 동기화 제대로 수정

`2022.09.28`  
- 프로필 변경 추가
- 회전에 각 페이지가 대응할 수 있도록 수정

`2022.09.30`  
- 스켈레톤 로딩 화면 추가

`2022.10.06`  
- google analysis 연동
- firebase bom 버전 업데이트

`2022.10.18`  
- 산 및 숲 페이지 구조 , db 구조 수정

`2022.10.19`  
- Firebase DynamicLink handle 코드 추가

`2022.10.20`  
- Detail page , audio play page 수정

`2022.10.21`  
- AnimationX로 animation추가
- db 구조 수정
- Detail Page UI 및 DetailPageLocation data class 수정

`2022.10.24`  
- Reservation page 프로그램 고르기 update
- Reservation , search page UI 수정

`2022.10.25`  
- ProfilePage 추가
- 기존 로직 수정(개별적 오디오 -> 프로그램)
- db field 수정

`2022.11.04`  
- HashTage수정 text Field 추가
- 숲 해설가, 사용자에 따른 프로필 page view 변경

`2022.11.07`  
- 로그인 과정 Figma version으로 업데이트
- MakeProfile 추가
- Login에서 OCP를 위한 interface 추가

`2022.11.08`  
- Profile 페이지에서 Edit page 작성
- mapper Model안으로 위치 변경
- follow page 추가
- 인증 페이지 추가

`2022.11.09`  
- Mountain 추가 페이지 , Program 추가 페이지 구현 완료
- setting viewModel에서 이름 가져오는 method 추가

`2022.11.10`  
- Audio 추가페이지 구현 완료
- Audio Edit 페이지 구현 완료

`2022.11.15`  
- Edit Page에 Audio 삭제, 수정 추가 (ItemTouchHelper)
- 



</details><br>  

--- 

### TODO