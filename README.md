# 🎓 무료 과외 매칭 플랫폼 학사모  

## 📜 학사모 소개  
![프로젝트 소개 이미지](https://github.com/ajounicemedia/HackSaMo/blob/main/%ED%95%99%EC%82%AC%EB%AA%A8%EC%84%A4%EB%AA%85.PNG)
## 🔄 학사모 기획서 및 개발 현황 (2023. 11.25 기준)
- **개발 현황(자세한 사항은 코드를 확인해주시면 감사하겠습니다)** 
  - 기능 구현(채팅, 찾기, 위시리스트, 로그인, 내 정보 등)은 모두 완료되었고 테스트 진행 후(현재 오류 고치거나 코드 리팩토링 진행 중) 유저가 직접사용해도 지장이 없다고 판단되면 플레이스토어에 출시할 예정입니다.
- **기획서**
  - [학사모 기획서](https://github.com/ajounicemedia/HackSaMo/blob/main/%ED%95%99%EC%82%AC%EB%AA%A8%20%EA%B8%B0%ED%9A%8D%EC%84%9C.pdf)

## 🎥 인앱 영상
![인앱 영상](https://github.com/ajounicemedia/HackSaMo/blob/main/%EC%9D%B8%EC%95%B1%20%EC%98%81%EC%83%81.gif)
![인앱 영상2](https://github.com/ajounicemedia/HackSaMo/blob/main/%EC%9D%B8%EC%95%B1%20%EC%98%81%EC%83%812.gif)
## 🚀 멤버 구성 및 개발 기간
- **PM** : 진현준
- **개발** : 진현준
- **기획 총괄** : 백지원  
- **QA** : 박성호
- **개발 기간** : 2023.9.01 ~ 

## 🛠 개발 환경
<details>
<summary>클릭하여 내용 보기/숨기기</summary>
  
- **플랫폼**: Android
- **개발 언어** : Kotlin
- **개발 툴** : Android Studio Giraffe | 2022.3.1
- **SDK 버전** :
  - Min SDK Version: 26
  - Target SDK Version: 34
  - Compile SDK Version: 34
- **사용한 라이브러리**:
  - **lifecycle**: 데이터의 변경을 관찰하고 뷰에 반영하기 위해 사용했습니다.
  - **splashscreen**: 스플래시 화면을 구현하기 위해 사용하였습니다.
  - **firebase**: 백엔드 엔지니어 없이 기능을 혼자 구현하기에 있어서 가격적인 측면에서 가장 합리적이고, 제가 생각하는 기능들을 모두 구현하는데 지장이 없을 것 같아서 채택하였습니다. 
  - **datastore-preferences**: 사용자의 정보(닉네임, 이메일,알람 끄기, 차단 목록 등)를 로컬에 넣어서 파이어베이스 호출을 최대한 줄이기 위해 사용했습니다. 저장해야할 양이 많지 않아 room대신 채택하였습니다. datastore를 사용할 때에는 코루틴을 사용했습니다.
  - **navigation**: Fragment 간의 이동을 편리하게 사용하기 위해 사용했습니다.
  - **univcert**: 대학교 인증을하기 위해 사용하였습니다.
  - **ucrop,glide,PhotoView**: 이미지를 표시, 일정 모양으로 편집, 확대하기 위해 사용했습니다. 채팅방에서 사진을 보내기, 유저의 프로필 사진을 보여주기 위해 사용되었습니다,
  - **Hilt**: 의존성 주입을 위해 사용했습니다. 
  - **paging**: 파이어베이스 호출을 최소화하기 위해 사용했습니다. 선생님, 학원, 학생 찾기를 할 때 30개의 정보를 끊어서 받습니다.(비용을 아끼기 위해서......)
  - **okhttp**: 채팅 알람을 전송하기 위해 사용했습니다.


- **테스트 환경**: Pixel 7 API 34 (에뮬레이터), Samsung Galaxy S22 (실제 디바이스)

</details>

## 🏗 아키텍쳐 패턴
<details>
<summary>클릭하여 내용 보기/숨기기</summary>

### MVVM(Model-View-ViewModel)패턴

재사용하는 함수, 실시간으로 관찰해야하는 값들이 많아서 ViewModel을 
사용하면 효율적이라 판단하여 MVVM 패턴을 채택하였습니다. 

적용한 아키텍쳐는 아래와 같습니다.
  
- model : datastore, di, entity, object, pagingsource, repository 코드 모음
  - datastore : datastore 코드모음
  - di : 의존성 주입 코드모음
  - entity : 객체(data class) 관련 코드모음
  - object : 지역, 과목 리스트, 공통적으로 쓰이는 기능모음 
  - pagingsource : paingsource 모음
  - repository : repository 모음
- view : 액티비티, 프래그먼트 코드 모음
  - auth : 로그인, 회원가입등 인증 관련 액티비티 코드모음
  - nav : 하단 네비게이션 바 관련 프래그먼트 코드모음
     - chat : 채팅 관련 프래그먼트 코드모음
     - find : 찾기 관련 프래그먼트 코드모음
     - myinfo : 내 정보 관련 프래그먼트 코드모음
     - mywishlist : 위시리스트 관련 프래그먼트 코드모음
- viewmodel : viewmodel 코드모음 

</details>



## 🧠 개발하면서 가장 신경쓴 부분
<details>
<summary>클릭하여 내용 보기/숨기기</summary>
수익이 목적이 아닌 사회 공헌 목적으로 앱 출시를 목표로 하고 있어서 비용을 최대한 절감해야 해서 Firebase를 최대한 적게 호출하는 데 초점을 두었습니다.<br/>
이 결과 채팅에서 읽음 처리 등 Firebase 호출이 너무 잦은 기능들은 구현하고도 사용을 못하게 되었습니다.<br/>
구글 기본 광고 수익이 나서 Firebase 지출 수익보다 많이 나오게 된다면 채팅 읽음 처리 기능 등 비용 문제로 구현을 하고도 포기를 한 기능들을 다시 사용할 수 있게끔 할 예정입니다.
</details>

## 📁 코드 설명
<details>
    <summary>클릭하여 내용 보기/숨기기</summary>    
  
  ## [ForHiltApplication](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/ForHiltApplication.kt) : 
  Hilt 사용을 위해 Application Class
  
  ## [model](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/model) : 
  datastore, di, entity, object, pagingsource, repository 패키지들의 상위 패키지입니다.

  ## [model - data class](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/model/dataclass) : 
  data class를 모아둔 패키지 입니다.
  #### [AcademyInfo](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/dataclass/AcademyInfo.kt) : 
  학원 data class입니다.
  #### [ChatListInfo](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/dataclass/ChatListInfo.kt) : 
  채팅리스트 data class입니다.
  #### [ChatMessageInfo](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/dataclass/ChatMessageInfo.kt) : 
  채팅메시지 data class입니다.
  #### [ReportReasonInfo](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/dataclass/ReportReasonInfo.kt) : 
  신고 사유 data class입니다.
  #### [StudentInfo](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/dataclass/StudentInfo.kt) : 
  학생 data class입니다.
  #### [TeacherInfo](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/dataclass/TeacherInfo.kt) : 
  선생님 data class
  #### [UserInfo](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/dataclass/UserInfo.kt) : 
  유저 data class
  
  ## [model - datastore](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/model/datastore) : 
  datastore들을 모아둔 패키지 입니다.<br/>
  유저 자신의 데이터를 로컬에 넣어주면 firebase 호출을 적게할 수 있어서 사용했습니다.<br/>
  유저가 자신의 데이터를 변경할 때 마다 datastore, firebase에 업데이트 해주고 앱내에서 정보를 볼 때는 datastore를 통해 볼 수 있도록 설계했습니다.
  #### [AcademyDatastoreHelper](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/datastore/AcademyDatastoreHelper.kt) : 
  내 학원 정보 datastore<br/>
  유저가 학원을 등록할 때 입력하는 간단소개, 수업대상, 도로명 주소, 상세주소, 우편번호, 검색될 지역, 강의할 과목, 학원 소개(학원 수강료, 강사 소개 등)를 로컬에 저장합니다.  
  #### [StudentDatastoreHelper](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/datastore/StudentDatastoreHelper.kt) : 
  내 학생 정보 datastore<br/>
  유저가 학생을 등록할 때 입력하는 간단소개, 출생년도, 선호하는 수업 방식(대면, 비대면), 수업이 가능한 지역, 듣고 싶은 과목, 성별, 학생 소개(과외 예산, 원하는 요구사항 등)를 로컬에 저장합니다.
  #### [TeacherDataStoreHelper](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/datastore/TeacherDataStoreHelper.kt) : 
  내 선생님 정보 datastore<br/>
  유저가 선생님을 등록할 때 입력하는 간단소개, 출생년도, 출신학교, 출신학교연고지, 전공, 재학 상태(휴학, 재학, 졸업 여부등),<br/> 
  선호하는 수업 방식(대면, 비대면), 활동가능한 지역, 수업하는 과목, 성별, 선생님 소개(과외 비용, 강의 스타일 등)를 로컬에 저장합니다.
  #### [UserDataStoreHelper](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/datastore/UserDataStoreHelper.kt) : 
  내 기본 정보 datastore
  유저의 firebase uid, 닉네임, 이메일, 선생님 등록여부, 학원 등록여부, 학원등록 여부, 알림을 보낼 때 사용되는 서버키, 유저의 현재 상태(선생님, 학원 ,학생 중 택1 또는 null)를 로컬에 저장합니다.

  ## [model - di](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/model/di) : 
  의존성 주입 관련 코드를 모아둔 패키지입니다.
  #### [DatastoreModule](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/di/DatastoreModule.kt) : 
  DatastoreModule
  #### [RepositoryModule](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/di/RepositoryModule.kt) : 
  RepositoryModule
  
  ## [model - object](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/model/object) : 
  긴 배열 같은 데이터, 많은 뷰에 사용되는 기능들을 모아둔 패키지입니다. 중복되는 코드를 줄이기 위해 사용했습니다.
  ### [model - object - data](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/model/object/data) : 
  과목, 전국 지역 object가 포함되어 있는 패키지입니다.
  #### [model - object - data - SubjectData](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/object/data/LocationData.kt) : 
  과목들을 모아둔 object
  #### [model - object - data - SubjectData](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/object/data/SubjectData.kt) : 
  전국 지역들을 모아둔 object
  ### [model - object - util](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/model/object/util) : 
  뷰에서 많이 사용된 기능들을 모아둔 패키지입니다.
  #### [model - object - data - MenuUtil](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/object/util/MenuUtil.kt) : 
  왼쪽 위 뒤로가기 기능이 있는 object
  #### [model - object - data - NavigationUtil](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/object/util/NavigationUtil.kt) : 
  안드로이드 기본 뒤로가기 버튼을 누를 시 찾기로, 등록하기에서 누를시 등록 종료, 키보드가 생성 됐을 때 앱이 종료 되는걸 방지하기, 키보드 숨기는 코드가 구현된 object
  #### [model - object - data - ToolbarUtil](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/object/util/ToolbarUtil.kt) : 
  툴바를 사용할 때 기본 제목 표시를 비활성화 해주고, 뒤로가기는 활성화하고, 제목을 설정하고 제목의 색깔은 검정색으로 설정해주는 기능이 있는 object

  ## [model - pagingsource](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/model/pagingsource) : 
  PagingSource를 모아둔 패키지입니다.
  #### [AcademyPagingSource](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/pagingsource/AcademyPagingSource.kt) : 
  검색한 학원 정보를 firebase에서 30개씩 가져옵니다. 30개씩 가져옴으로써 파이어베이스 호출과 비용을 아낄 수 있습니다.
  #### [StudentPagingSource](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/pagingsource/StudentPagingSource.kt) : 
  검색한 학생 정보를 firebase에서 30개씩 가져옵니다. 마찬가지로 30개씩 가져옴으로써 파이어베이스 호출과 비용을 아낄 수 있습니다.
  #### [TeacherPagingSource](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/pagingsource/TeacherPagingSource.kt) : 
  검색한 선생님 정보를 firebase에서 30개씩 가져옵니다. 마찬가지로 30개씩 가져옴으로써 파이어베이스 호출과 비용을 아낄 수 있습니다.

  ## [model - repository](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/model/repository) : 
  Repository들을 모아둔 패키지입니다. Firebase와 데이터를 주고 받거나 Firebase인증에 관련된 것을 Repository에 구현했습니다.
  #### [AcademyChatRepository](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/repository/AcademyChatRepository.kt) : 
  학원 채팅방 관련된 기능들이 구현된 Repository입니다.<br/>
  학원과의 채팅에서 채팅방 나가기, 채팅 리스트 업데이트(상대, 자신), 새로운 메시지인지 확인, 채팅방 목록 가져오기, 메세지 전송, 채팅 메세지 가져오기, 사진 전송, 정보 가져오기, 알람 보내기가 구현되어있습니다.
  #### [AcademyRepository](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/repository/AcademyRepository.kt) : 
  학원 정보에 대한 기능들이 구현된 Repository입니다.<br/>
  학원의 firebase uid, 한줄 소개, 수업 대상, 프로필 사진, 등록증, 우편번호, 도로명 주소, 상세주소, 검색될 주소, 과목, 소개를 업로드하고 수정(일부는 수정x)합니다. 또한 학원을 등록했는지 확인합니다. 
  #### [ChatRepository](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/repository/ChatRepository.kt) : 
  블랙리스트 추가 삭제, 알람차단목록 추가 삭제, 유저 신고, 신고 사유 업로드가 구현되어있는 Repository입니다.
  #### [FindRepository](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/repository/FindRepository.kt) : 
  찾기에서 사용되는 Repository입니다. 학원, 선생님, 학생님 pagingsource를 호출합니다.
  #### [MyInfoRepository](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/repository/MyInfoRepository.kt) : 
  내 정보와 관련된 Repository입니다. 닉네임 변경, 비밀번호 확인, 비밀번호 변경, 자신의 상태 변경(학생, 학원, 선생 중 택1 아니면 null)이 구현되어 있습니다.
  #### [StudentChatRepository](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/repository/StudentChatRepository.kt) : 
  학생 채팅방 관련된 기능들이 구현된 Repository입니다.<br/>
  학생과의 채팅에서 채팅방 나가기, 채팅 리스트 업데이트(상대, 자신), 새로운 메시지인지 확인, 채팅방 목록 가져오기, 메세지 전송, 채팅 메세지 가져오기, 사진 전송, 정보 가져오기, 알람 보내기가 구현되어있습니다.
  #### [StudentRepository](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/repository/StudentRepository.kt) : 
  학생 정보에 대한 기능들이 구현된 Repository입니다.<br/>
  학생의 firebase uid, 한줄 소개, 출생년도, 성별, 원하는 수업방식, 프로필 사진, 수업을 들을 수 있는 지역, 듣고 싶은 과목, 소개를 업로드하고 수정(일부는 수정x)합니다. 또한 학생을 등록했는지 확인합니다.
  #### [TeacherChatRepository](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/repository/TeacherChatRepository.kt) : 
  선생님 채팅방 관련된 기능들이 구현된 Repository입니다.<br/>
  선생님과의 채팅에서 채팅방 나가기, 채팅 리스트 업데이트(상대, 자신), 새로운 메시지인지 확인, 채팅방 목록 가져오기, 메세지 전송, 채팅 메세지 가져오기, 사진 전송, 정보 가져오기, 알람 보내기가 구현되어있습니다.
  #### [TeacherRepository](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/repository/TeacherRepository.kt) : 
  선생님 정보에 대한 기능들이 구현된 Repository입니다.<br/>
  선생님의 firebase uid, 한줄 소개, 출생년도, 성별, 학교, 학교 위치, 전공, 재학상태, 원하는 수업방식, 프로필 사진, 수업을 할 수 있는 지역, 수업 과목, 소개를 업로드하고 수정(일부는 수정x)합니다. 또한 선생님을 등록했는지 확인합니다.
  #### [WishListRepository](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/model/repository/WishListRepository.kt) : 
  위시리스트에 대한 기능들이 구현된 Repository입니다.<br/>
  내 위시리스트에 선생님, 학원, 학생 추가, 삭제 그리고 내 선생님, 학원, 학생 위시리스트를 불러옵니다.
  
  ## [view](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view) : 
  View들을 모아둔 패키지입니다.
  
  ## [view - auth](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/auth) : 
  로그인, 회원가입, 회원가입 완료,인트로, 비밀번호 찾기 Activity들을 모아둔 패키지 입니다.
  #### [FindPasswordActivity](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/auth/FindPasswordActivity.kt) : 
  입력한 이메일 주소로 비밀번호 찾기 메일을 보냅니다.
  #### [IntroActivity](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/auth/IntroActivity.kt) : 
  스플래시화면을 보여준후 LoginActivity로 이동합니다.
  #### [JoinActivity](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/auth/JoinActivity.kt) : 
  아이디, 비밀번호, 닉네임을 입력받아 회원가입을 진행합니다. 회원가입이 완료되면 JoinCompleteActivity로 이동합니다.
  #### [JoinCompleteActivity](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/auth/JoinCompleteActivity.kt) : 
  회원가입이 완료됐을 때 나오는 Activity입니다. 이후 버튼을 눌러서 HomeActivity로 이동합니다.
  #### [LoginActivity](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/auth/LoginActivity.kt) : 
  회원가입이나 비밀번호 찾기로 이동하거나 아이디와 비밀번호를 입력받아서 로그인하는 Activity입니다.<br/> 
  로그인을 성공했거나 기존에 로그인이 되어있는 경우 HomeActivity로 이동합니다.
  
  ## [nav](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav) : 
  네비게이션바에 연결된 뷰들을 모아놨습니다.
  #### [HomeActivity](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/home/HomeActivity.kt) : 
  홈 Activity입니다. 유저의 캐시가 초기화 됐는지 확인하고 초기화 됐으면 Firebase에서 유저 정보를 다 불러오고 서버키를 갱신합니다.<br/>
  알림을 받을지 유저에게 물어보고 FindFragment을 실행합니다.

  ## [nav - chat](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav/chat) : 
  채팅과 관련된 Fragment, adatper들을 모아둔 패키지입니다.
  #### [FullScreenPhotoActivity](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/chat/FullScreenPhotoActivity.kt) : 
  채팅방에서 이미지를 클릭했을 때 이미지만 보일 수 있도록 하는 Activity입니다. Activity내에서 사진을 확대하거나 축소할 수 있습니다.
  #### [MainChatFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/chat/MainChatFragment.kt) : 
  채팅방에 들어갔을 때 바로 나오는 기본 Fragment입니다. ViewPager를 통해 선생님, 학생, 학원의 채팅방 리스트로 갈 수 있습니다.<br/> 
  선생님 채팅방 리스트가 기본으로 설정되어있습니다.
  #### [MyFirebaseMessagingService](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/chat/MyFirebaseMessagingService.kt) : 
  채팅 알림을 수신하기 위한 MyFirebaseMessagingService입니다.<br/> 
  sharedPreferences에 알림 차단이 되어있는 유저로부터 알림은 받지 않습니다.
  #### [PreferenceUtils](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/chat/PreferenceUtils.kt) : 
  SharedPreferences를 사용하기 위해 만든 class입니다.<br/> 
  블랙리스트, 알림 차단 목록, 신고 목록을 로컬에 저장하기 위해 SharedPreferences을 사용했습니다.<br/>
  Datastore에는 키 값 하나당 하나를 쌍을 가졌는데 하나의 키에 여러개의 쌍을 가지게 하기 위해서는 SharedPreferences가 더 적합하다고 판단했습니다.
  #### [ReportReasonFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/chat/ReportReasonFragment.kt) : 
  다른 유저 신고 사유를 입력받는Fragment입니다.

  ## [nav - chat - adapter](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav/chat/adapter) : 
  채팅에서 사용된 adapter을 모아둔 패키지입니다.
  #### [ChatListAdapter](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/chat/adapter/ChatListAdapter.kt) : 
  채팅 리스트 adapter입니다. 채팅 리스트를 클릭시 해당 채팅방으로 이동합니다.
  #### [ChatListViewPagerAdapter](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/chat/adapter/ChatListViewPagerAdapter.kt) : 
  채팅 리스트 ViewPagerAdapter입니다. MainChatFragment에서 선생님, 학생, 학원 채팅 리스트를 선택하여 이동할 수 있게 해주는 역할을 합니다.
  <br/>물론 선생님, 학생, 학원 각 채팅 리스트끼리 선택하여 이동할 수 있습니다.
  #### [ChatRoomAdapter](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/chat/adapter/ChatRoomAdapter.kt) : 
  채팅 방 메세지 adapter입니다. 유저들의 채팅 목록을 보여줍니다.

  ## [nav - chat - chatlist](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav/chat/chatlist) : 
  채팅리스트 Fragment를 모아둔 패키지입니다.
  #### [TeacherChatListFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/chat/chatlist/TeacherChatListFragment.kt) : 
  선생님 채팅 리스트 Fragment입니다. 어댑터 클릭시 해당 선생님 채팅방으로 이동합니다.
  #### [StudentChatListFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/chat/chatlist/StudentChatListFragment.kt) : 
  학생 채팅 리스트 Fragment입니다. 어댑터 클릭시 해당 학생 채팅방으로 이동합니다.
  #### [AcademyChatListFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/chat/chatlist/AcademyChatListFragment.kt) : 
  학원 채팅 리스트 Fragment입니다. 어댑터 클릭시 해당 학원 채팅방으로 이동합니다.

  ## [nav - chat - chatroom](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav/chat/chatroom) : 
  채팅방, 채팅방내에서 상대정보보기 Fragment를 모아둔 패키지입니다.
  #### [AcademyChatRoomFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/chat/chatroom/AcademyChatRoomFragment.kt) : 
  학원과 채팅 하는 Fragment입니다. 학생이면 찾기에서 찾고 채팅을 보낼 수 있고 학원이면 채팅이오면 이 Fragment가 생깁니다.
  #### [AcademyInfoFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/chat/chatroom/AcademyInfoFragment.kt) : 
  채팅방 내에서 상대가 학원일 때 상세보기를 눌렀을 때 들어가지는 Fragment입니다. 상대 학원 상세 정보가 보입니다.
  #### [TeacherChatRoomFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/chat/chatroom/TeacherChatRoomFragment.kt) : 
  선생님과 채팅 하는 Fragment입니다. 학생이면 찾기에서 찾고 채팅을 보낼 수 있고 선생님이면 채팅이오면 이 Fragment가 생깁니다.
  #### [TeacherInfoFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/chat/chatroom/TeacherInfoFragment.kt) : 
  채팅방 내에서 상대가 선생님일 때 상세보기를 눌렀을 때 들어가지는 Fragment입니다. 상대 선생님 상세 정보가 보입니다.
  #### [StudentChatRoomFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/chat/chatroom/StudentChatRoomFragment.kt) : 
  학생과 채팅 하는 Fragment입니다. 찾기에서 학생을 찾았을 때, 메시지를 받는 사람이 학생일 때 생성됩니다.
  #### [StudentInfoFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/chat/chatroom/StudentInfoFragment.kt) : 
  채팅방 내에서 상대가 학생일 때 들어가지는 Fragment입니다. 채팅 상대가 학생이면 상대 정보가 보입니다.
  
  ## [nav - find](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav/find) : 
  찾기 관련된 Fragment, adapter들을 모아둔 패키지입니다.
  #### [FindFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/find/FindFragment.kt) : 
  메인 찾기 Fragment이자 로그인 됐을 때 처음 보이는 화면입니다. 이곳에서 검색하길 원하는 대상의 과목, 성별, 지역 등을 설정합니다.
  #### [SetupLocalFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/find/SetupLocalFragment.kt) : 
  찾을 대상의 지역을 고르는 Fragment입니다. 지역을 고르면 FindFragment에 반영이 됩니다.
  #### [SetupSubjectFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/find/SetupSubjectFragment.kt) : 칮을 대상의 수업 과목을 필터링합니다.
  찾을 대상의 과목을 고르는 Fragment입니다. 과목을 고르면 FindFragment에 반영이 됩니다.

  ## [nav - find - adapter](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav/find/adapter) : 
  찾기 관련된 adapter들을 패키지입니다.
  #### [AcademyPagingListAdapter](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/find/adapter/AcademyPagingListAdapter.kt) : 
  자신이 원하는 조건의 학원 목록을 보여주는 adapter입니다. 해당 어댑터내의 하트 이미지를 누르면 위시리스트에 추가가 가능하고 어댑터를 누르면 대상의 상세정보를 볼 수 있습니다.
  #### [TeacherPagingListAdapter](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/find/adapter/TeacherPagingListAdapter.kt) : 
  자신이 원하는 조건의 선생님 목록을 보여주는 adapter입니다. 해당 어댑터내의 하트 이미지를 누르면 위시리스트에 추가가 가능하고 어댑터를 누르면 대상의 상세정보를 볼 수 있습니다.
  #### [StudentPagingListAdapter](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/find/adapter/StudentPagingListAdapter.kt) : 
  자신이 원하는 조건의 학생을 보여주는 adapter입니다. 해당 어댑터내의 하트 이미지를 누르면 위시리스트에 추가가 가능하고 어댑터를 누르면 대상의 상세정보를 볼 수 있습니다.

  ## [nav - find - list](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav/find/list) : 
  찾기를 했을 때 나오는 리스트 fragment, 리스트를 클릭했을 때 나오는 상세 정보 fragment를 모아둔 패키지입니다.
  #### [DetailAcademyInfoFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/find/list/DetailAcademyInfoFragment.kt) : 
  자신이 찾은 학원의 상세정보를 보여주는 fragment입니다. fragment내에서 해당 대상을 위시리스트에 추가, 채팅을 할 수 있습니다.
  #### [DetailStudentInfoFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/find/list/DetailStudentInfoFragment.kt) : 
  자신이 찾은 학생의 상세정보를 보여주는 fragment입니다. fragment내에서 해당 대상을 위시리스트에 추가, 채팅을 할 수 있습니다.
  #### [DetailTeacherInfoFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/find/list/DetailTeacherInfoFragment.kt) : 
  자신이 찾은 선생님의 상세정보를 보여주는 fragment입니다. fragment내에서 해당 대상을 위시리스트에 추가, 채팅을 할 수 있습니다.
  #### [ShowAcademyListFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/find/list/ShowAcademyListFragment.kt) : 
  자신이 찾은 학원 목록을 보여주는 fragment입니다. 원하는 대상을 누르면 상세 보기, 위시리스트 추가가 가능합니다.
  #### [ShowStudentListFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/find/list/ShowStudentListFragment.kt) : 
  자신이 찾은 학생 목록을 보여주는 fragment입니다. 원하는 대상을 누르면 상세 보기, 위시리스트 추가가 가능합니다.
  #### [ShowTeacherListFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/find/list/ShowTeacherListFragment.kt) : 
  자신이 찾은 선생님 목록을 보여주는 fragment입니다. 원하는 대상을 누르면 상세 보기, 위시리스트 추가가 가능합니다.
  
  ## [nav - myinfo](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo) : 
  내 정보 관련 fragment들을 모아둔 패키지입니다.
  #### [MyInfoFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/MyInfoFragment.kt) : 
  내 정보를 눌렀을 때 처음 나오는 fragment입니다. 각종 정보를 등록하고, 수정하고 로그아웃, 계정삭제등을 할 수 있습니다.<br/>
  내 정보내에서 fragment를 이동할 때 datastore를 활용해서 해당 fragment로 이동가능한지 확인합니다.

  ## [nav - myinfo - changemyinfo](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/changemyinfo) : 
  내 정보에서 계정 설정을 변경하는 fragment, adapter가 모여있는 패키지입니다.
  #### [BlackListAdapter](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/changemyinfo/BlackListAdapter.kt) : 
  유저의 블랙리스트를 보여주는 adapter입니다. 어댑터내의 해제 버튼을 누르면 해당 유저를 블랙리스트에서 삭제할 수 있습니다.
  #### [ChangeNicknameFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/changemyinfo/ChangeNicknameFragment.kt) : 
  닉네임을 변경하는 fragment입니다.
  #### [ChangePasswordFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/changemyinfo/ChangePasswordFragment.kt) : 
  비밀번호를 입력하는 fragment입니다.
  #### [InputPasswordForDeleteAccount](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/changemyinfo/InputPasswordForDeleteAccount.kt) : 
  계정을 삭제 페이지를 가기전에 비밀번호를 입력 받는 fragment입니다. 비밀번호가 맞으면 계정 삭제 페이지로 이동합니다.
  #### [InputPasswordForNicknameFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/changemyinfo/InputPasswordForNicknameFragment.kt) : 
  닉네임 변경 페이지를 가기전에 비밀번호를 입력 받는 fragment입니다. 비밀번호가 맞으면 닉네임 변경 페이지로 이동합니다.
  #### [InputPasswordForPasswordFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/changemyinfo/InputPasswordForPasswordFragment.kt) : 
  비밀번호 변경 페이지를 가기전에 비밀번호를 입력 받는 fragment입니다. 비밀번호가 맞으면 비밀번호 변경 페이지로 이동합니다.
  #### [ManageBlackListFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/changemyinfo/ManageBlackListFragment.kt) : 
  블랙리스트를 관리하는 fragment입니다. 자신의 블랙리스트를 삭제할 수 있습니다.
  #### [ManageUserInfoFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/changemyinfo/ManageUserInfoFragment.kt) : 
  비밀번호를 변경할지 닉네임을 변경할지 선택하는 fragment입니다. 항목을 선택하면 해당 변경 페이지로 이동합니다.
  #### [ManageUserStatusFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/changemyinfo/ManageUserStatusFragment.kt) : 
  유저의 상태를 변경하는 fragment입니다. 자신이 선생님, 학생, 학원중에 어떤 것으로 활동할지 선택 할 수 있습니다.
  
  ## [nav - myinfo - registermodify](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify) : 
  내 정보에서 선생님, 학생, 학원 정보를 등록, 수정하는 fragment,adpater가 모여있는 패키지입니다.
  #### [RegisterCompleteFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/RegisterCompleteFragment.kt) : 
  선생님, 학원, 학생 등록 완료가 되면 이동하는 fragment입니다. 만약에 자신이 학원을 등록하고 이 페이지에 왔다면 자신의 상태가 학원으로 설정됩니다.

  ## [nav - myinfo - registermodify - academy](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/academy) : 
  내 학원 정보를 등록하거나 수정하는 fragment들을 모아둔 패키지입니다.
  ## [nav - myinfo - registermodify - academy - modify](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/academy/modify) : 
  등록한 학원 정보를 수정하는 fragment들이 모여있는 패키지입니다.
  #### [ModifyAcademyInfoTextFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/academy/modify/ModifyAcademyInfoTextFragment.kt) : 
  editText로 입력 받을 수 있는 학원 정보들을 수정하는 fragment입니다.
  #### [ModifyAcademyIntroduceFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/academy/modify/ModifyAcademyIntroduceFragment.kt) : 
  학원의 상세소개를 수정하는 fragment입니다.
  #### [ModifyAcademyProfileFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/academy/modify/ModifyAcademyProfileFragment.kt) : 
  학원의 어떤 정보를 수정할지 입력 받는 fragment입니다.
  #### [ModifyAcademySearchedLoaclFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/academy/modify/ModifyAcademySearchedLoaclFragment.kt) : 
  학원의 어떤 정보를 수정할지 입력 받는 fragment입니다.
  #### [ModifyAcademySubjectFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/academy/modify/ModifyAcademySubjectFragment.kt) : 
  학원이 수업 가능한 과목을 수정하는 fragment입니다.

  ## [nav - myinfo - registermodify - academy - register](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/academy/register) : 
  학원을 등록하는 fragment들을 모아둔 패키지입니다.
  #### [AcademyIntroduceFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/academy/register/AcademyIntroduceFragment.kt) : 
  학원의 상세 소개글을 입력받는 fragment입니다.
  #### [AcademyLocalFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/academy/register/AcademyLocalFragment.kt) : 
  Daum 우편번호 서비스을 활용하여 학원 상세 정보를 입력받는 fragment입니다.
  #### [AcademySearchedAddressFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/academy/register/AcademySearchedAddressFragment.kt) : 
  학원이 검색될 지역을 입력받는 fragment입니다.
  #### [AcademySubjectFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/academy/register/AcademySubjectFragment.kt) : 
  학원이 수업을 진행하는 과목을 입력받는 fragment입니다.
  #### [AuthAcademyFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/academy/register/AuthAcademyFragment.kt) : 
  학원 사업 등록증을 사진을 받는 fragment입니다.
  #### [RegisterAcademyFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/academy/register/RegisterAcademyFragment.kt) : 
  학원의 한줄 소개, 수업대상, 프로필 사진을 입력받는 fragment입니다.
  


  ## [nav - myinfo - registermodify - student](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/student) : 
  내 학생 정보를 등록하거나 수정하는 fragment들을 모아둔 패키지입니다.
  ## [nav - myinfo - registermodify - student - modify](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/student/modfiy) : 
  등록된 학생정보를 수정하는 fragment를 모아둔 패키지입니다.
  #### [ModifyStudentInfoTextFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/student/modfiy/ModifyStudentInfoTextFragment.kt) : 
  editText로 입력 받을 수 있는 학생 정보들을 수정하는 fragment입니다.
  #### [ModifyStudentIntroduceFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/student/modfiy/ModifyStudentIntroduceFragment.kt) : 
  학생의 상세소개를 수정하는 fragment입니다.
  #### [ModifyStudentLocalFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/student/modfiy/ModifyStudentLocalFragment.kt) : 
  학생의 활동 가능한 지역 수정하는 fragment입니다.
  #### [ModifyStudentProfileFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/student/modfiy/ModifyStudentProfileFragment.kt) : 
  학생의 어떤 정보를 수정할지 선택하는 fragment입니다.
  #### [ModifyStudentSubjectFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/student/modfiy/ModifyStudentSubjectFragment.kt) : 
  학생의 받고 싶은 과외 과목을 수정하는 fragment입니다.
  
  ## [nav - myinfo - registermodify - student - register](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/student/register) : 
  학생 정보를 등록하는 fragment를 모아둔 패키지입니다.
  #### [RegisterStudentFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/student/register/RegisterStudentFragment.kt) : 
  학생의 한줄소개, 출생년도, 성별, 선호하는 수업방식, 프로필 사진을 입력받는 fragment입니다.
  #### [StudentIntroduceFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/student/register/StudentIntroduceFragment.kt) : 
  학생의 상세소개를 입력받는 fragment입니다.
  #### [StudentLocalFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/student/register/StudentLocalFragment.kt) : 
  학생 활동 가능한 지역 선택하는 fragment입니다.
  #### [StudentSubjectFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/student/register/StudentSubjectFragment.kt) : 
  학생이 듣기 원하는 수업 과목을 선택받는 fragment입니다.

  ## [nav - myinfo - registermodify - teacher](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/teacher) : 
  선생님 정보 등록, 수정하는 fragment, adpater들을 모아둔 패키지입니다.
  ## [nav - myinfo - registermodify - teacher - modify](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/teacher/modify) : 
  등록된 선생님 정보를 수정하는 fragment들을 모아둔 패키지입니다.
  #### [ModifyTeacherInfoTextFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/teacher/modify/ModifyTeacherInfoTextFragment.kt) : 
  edittext로 수정할 수 있는 선생님 정보를 수정하는 fragment입니다.
  #### [ModifyTeacherLocalFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/teacher/modify/ModifyTeacherIntroduceFragment.kt) : 
  선생님이 수업가능한 지역을 수정하는 fragment입니다.
  #### [ModifyTeacherProfileFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/teacher/modify/ModifyTeacherLocalFragment.kt) : 
  어떤 선생님 정보를 수정할지 선택하는 fragment입니다.
  #### [ModifyTeacherSubjectFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/teacher/modify/ModifyTeacherProfileFragment.kt) : 
  선생님이 수업 가능한 과목을 수정하는 fragment입니다.
  #### [ModifyTeacherIntroduceFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/teacher/modify/ModifyTeacherSubjectFragment.kt) : 
  선생님의 상세소개를 수정하는 fragment입니다.
  
  ## [nav - myinfo - registermodify - teacher - register](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/teacher/register) : 
  내 정보 - 선생님 등록하기 관련 뷰들을 모아 놨습니다.
  #### [AuthCampusFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/teacher/register/AuthCampusFragment.kt) : 
  대학 인증 api를 활용하여 대학인증을 하는 fragment입니다.
  #### [RegisterTeacherFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/teacher/register/RegisterTeacherFragment.kt) : 
  선생님 한줄소개, 출샌년도, 성별, 대학교, 대학교 지역, 전공, 학적 상태, 선호하는 수업방식을 입력받는 fragment입니다.
  #### [TeacherChoiceSubjectFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/teacher/register/TeacherIntroduceFragment.kt) : 
  선생님의 상세소개를 입력받는 fragment입니다.
  #### [TeacherChoiceLocalFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/teacher/register/TeacherLocalFragment.kt) : 
  선생님이 활동 가능 한 지역을 선택하는 fragment입니다.
  #### [TeacherIntroduceFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/myinfo/registermodify/teacher/register/TeacherSubjectFragment.kt) : 
  과외를 진행할 과목을 선택하는 fragment입니다.

  ## [nav - wishlist](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav/mywishlist) : 
  위시리스트 관련 adapter, fragment를 모아둔 패키지입니다.
  #### [AcademyWishListFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/mywishlist/AcademyWishListFragment.kt) : 
  위시리스트에 추가한 학원을 볼 수 있는 fragment입니다.
  #### [AcademyWishListFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/mywishlist/MainWishListFragment.kt) : 
  메인 위시리스트 fragment입니다. viewPagerAdapter을 통해 선생님, 학생, 학원 위시리스트 중 하나를 선택해서 볼 수 있습니다.<br/> 
  처음 나오는 페이지는 선생님 위시리스트입니다. 
  #### [StudentWishListFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/mywishlist/StudentWishListFragment.kt) : 
  위시리스트에 추가한 학생을 볼 수 있는 fragment입니다.
  #### [TeacherWishListFragment](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/mywishlist/TeacherWishListFragment.kt) : 
  위시리스트에 추가한 선생님을 볼 수 있는 fragment입니다.

  ## [nav - wishlist - adapter](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/view/nav/mywishlist/adapter) : 
  위시리스트 관련 adapter를 모아둔 패키지입니다.
  #### [MyAcademyWishListAdapter](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/mywishlist/adapter/MyAcademyWishListAdapter.kt) : 
  내 학원 위시리스트를 볼 수 있는 adapter입니다. adatper를 누르면 상세 정보를 볼 수 있고 adapter의 특정 버튼을 누르면 위시리스트에서 대상을 삭제할 수 있습니다.
  #### [MyStudentWishListAdapter](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/mywishlist/adapter/MyStudentWishListAdapter.kt) : 
  내 학생 위시리스트를 볼 수 있는 adapter입니다. adatper를 누르면 상세 정보를 볼 수 있고 adapter의 특정 버튼을 누르면 위시리스트에서 대상을 삭제할 수 있습니다.
  #### [MyTeacherWishListAdapter](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/mywishlist/adapter/MyTeacherWishListAdapter.kt) : 
  내 선생님 위시리스트를 볼 수 있는 adapter입니다. adatper를 누르면 상세 정보를 볼 수 있고 adapter의 특정 버튼을 누르면 위시리스트에서 대상을 삭제할 수 있습니다.
  #### [WishListViewPagerAdapter](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/view/nav/mywishlist/adapter/WishListViewPagerAdapter.kt) : 
  위시리스트 ViewPagerAdapter입니다. AcademyWishListFragment에서 선생님, 학생, 학원 위시리스트를 선택하여 이동할 수 있게 해주는 역할을 합니다.
  
  ## [viewmodel](https://github.com/ajounicemedia/HackSaMo/tree/main/src/main/java/com/example/god_of_teaching/viewmodell) : 
  ViewModel을 모아둔 패키지입니다. ViewModel에서는 주로 Repository를 호출하며, 로컬에 데이터를 넣고, Live Data를 반환합니다.
  #### [AcademyChatViewModel](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/viewmodel/AcademyChatViewModel.kt) : 
  AcademyChatRepository을 호출하는 ViewModel입니다.
  #### [AcademyViewModel](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/viewmodel/AcademyViewModel.kt) : 
  AcademyRepository를 호출하며 학원 정보 관련 데이터를 로컬에 넣어주는 ViewModel입니다. 또한 학원 관련 LiveData를 반환합니다.
  #### [AuthViewModel](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/viewmodel/AuthViewModel.kt) : 
  authRepository를 호출하며 유저 정보 관련 데이터를 로컬에 넣어주는 ViewModel입니다. 또한 인증 관련 LiveData를 반환합니다. 
  #### [ChatViewModel](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/viewmodel/ChatViewModel.kt) : 
  ChatRepository를 호출하고 유저의 알림 차단 목록, 블랙리스트를 로컬에 넣어주는 ViewModel입니다.
  #### [FindViewModel](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/viewmodel/FindViewModel.kt) : 
  FindRepository를 호출하는 ViewModel입니다. 또한 찾기 관련 LiveData를 반환합니다
  #### [MyInfoViewModel](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/viewmodel/MyInfoViewModel.kt) : 
  myInfoRepository를 호출하며 내 정보 관련 데이터를 로컬에 넣어주는 ViewModel입니다. 또한 내 정보 관련 LiveData를 반환합니다.
  #### [MyInfoViewModel](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/viewmodel/MyInfoViewModel.kt) : 
  myInfoRepository를 호출하며 내 정보 관련 데이터를 로컬에 넣어주는 ViewModel입니다. 또한 내 정보 관련 LiveData를 반환합니다.
  #### [StudentChatViewModel](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/viewmodel/StudentChatViewModel.kt) : 
  StudentChatRepository을 호출하는 ViewModel입니다.
  #### [StudentViewModel](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/viewmodel/StudentViewModel.kt) : 
  StudentRepository를 호출하며 학생 정보 관련 데이터를 로컬에 넣어주는 ViewModel입니다. 또한 학생 관련 LiveData를 반환합니다.
  #### [TeacherChatViewModel](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/viewmodel/TeacherChatViewModel.kt) : 
  TeacherChatRepository을 호출하는 ViewModel입니다.
  #### [TeacherViewModel](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/viewmodel/TeacherViewModel.kt) : 
  TeacherRepository를 호출하며 선생님 정보 관련 데이터를 로컬에 넣어주는 ViewModel입니다. 또한 선생님 관련 LiveData를 반환합니다.
  #### [WishListViewModel](https://github.com/ajounicemedia/HackSaMo/blob/main/src/main/java/com/example/god_of_teaching/viewmodel/WishListViewModel.kt) : 
  WishListRepository를 호출합니다 또한 위시리스트 관련 LiveData를 반환합니다.

</details>



## 💡 학습 사항 및 기술적 성장
<details>
<summary>클릭하여 내용 보기/숨기기</summary>
  
이 프로젝트를 통해, 안드로이드 개발의 다양한 최신 라이브러리와 기술을 심도 있게 학습하고 적용했습니다. 각 기술의 적용과 학습 과정은 다음과 같습니다:

- **데이터 바인딩과 뷰 바인딩**: 레이아웃과 데이터 모델 간의 연결을 강화하고, 더 깔끔하고 유지보수가 쉬운 코드를 작성하기 위해 데이터 바인딩과 뷰 바인딩을 적극적으로 활용했습니다. 이를 통해 UI 컴포넌트와 데이터 소스 간의 결합을 최소화하고, 더 안정적인 앱 개발을 경험했습니다.

- **LiveData**: UI와 데이터 상태의 일관성을 유지하기 위해 LiveData를 사용했습니다. LiveData를 활용하여 데이터의 변경사항을 UI에 실시간으로 반영할 수 있었으며, 이를 통해 반응형 애플리케이션 개발에 대한 이해를 깊게 했습니다.

- **Navigation 컴포넌트**: 네비게이션 컴포넌트를 통해 프래그먼트 간의 전환을 더욱 효율적으로 관리하고, 복잡한 사용자 인터페이스 흐름을 간소화하는 방법을 배웠습니다.

- **DataStore와 SharedPreferences**: 사용자의 설정 및 앱 설정을 관리하기 위해 DataStore와 SharedPreferences를 사용했습니다. 이를 통해 효과적인 데이터 저장 및 검색 방법에 대해 학습하고, 성능 및 보안 측면에서의 차이점을 이해했습니다.

- **Hilt를 이용한 의존성 주입**: Hilt를 사용하여 앱의 의존성 관리를 간소화했습니다. 이를 통해 코드의 재사용성을 높이고, 모듈 간 결합도를 낮추는 방법을 배웠습니다.

- **코루틴**: 비동기 작업 처리와 백그라운드 작업 관리를 위해 코루틴을 활용했습니다. 코루틴을 통해 네트워크 호출, 데이터베이스 작업 등을 더 효율적으로 관리하는 방법을 배웠습니다.

- **Paging 라이브러리**: 대량의 데이터를 효과적으로 로드하고 표시하기 위해 Paging 라이브러리를 사용했습니다. 이를 통해 사용자 경험을 향상시키면서 리소스 사용을 최적화하는 방법을 학습했습니다.
   
- **뷰모델(ViewModel)**: 앱의 UI 관련 데이터를 관리하기 위해 뷰모델을 사용했습니다. 뷰모델을 사용하여 UI 로직과 비즈니스 로직을 분리하고, 더욱 테스트하기 쉬운 코드를 작성할 수 있었습니다.

- **파이어베이스(Firebase)**: 백엔드 서비스를 구축하는데 파이어베이스를 사용했습니다. 파이어베이스의 다양한 기능 중, 특히 인증(Authentication), 데이터베이스(Firestore), 스토리지(Storage)를 활용하여 사용자 인증, 데이터 저장 및 관리, 파일 저장 등의 기능을 구현했습니다. 파이어베이스를 사용함으로써 서버리스 아키텍처를 경험하고, 백엔드 개발이 대략적으로 어떻게 진행되는지 알 수 있었습니다.

이러한 기술들의 적용은 프로젝트의 안정성과 확장성을 향상시키는 동시에, 저에게 현대적인 앱 개발 방법론과 클라우드 기반 백엔드 서비스에 대한 깊은 이해를 제공했습니다.

이러한 기술들을 프로젝트에 적용함으로써 안드로이드 개발의 현대적인 방법론에 대해 깊이 이해하고, 실제 애플리케이션 개발에서 이를 효과적으로 활용하는 능력을 키울 수 있었습니다.
</details>

## 🤔 프로젝트 진행중 마주 한 문제 및 해결 과정
<details>
<summary>클릭하여 내용 보기/숨기기</summary>
  
### Cloud Messaging API의 사용 중단
- 이전에 알림을 구현할 때 Cloud Messaging API을 사용하였는데, Cloud Messaging API의 지원 중단으로 인해 앱 내 알림 전송에 어려움을 겪었습니다.

##### 해결 과정:
- 문제를 해결하기 위해 먼저 백엔드 엔지니어 없이 대체 가능한 서비스를 조사했습니다. 이 과정에서 Firebase Cloud Messaging API(V1)가 새로운 대안으로 적합하다는 것을 발견했습니다.
- Firebase Cloud Messaging API(V1)로 전환하는 방법을 학습하고, Google OAuth 2.0 Playground에서 토큰을 발급 받아 구현했습니다. 하지만 토큰이 자주 초기화 되는 것을 알고 앱 내에서 사용자별 고유 토큰을 생성하는 로직을 구현했습니다.

##### 결과 및 교훈:
- Firebase Cloud Messaging API(V1)을 사용하여 알림 전송 기능을 성공적으로 구현했습니다. 이를 통해 Cloud Messaging API보다 보안을 더욱 개선할 수 있었습니다.
- 이 경험은 새로운 기술에 빠르게 적응하고, 문제에 대한 해결책을 찾는 능력을 키우는 데 도움이 되었습니다. 또한, 빠르게 변화하는 기술 환경에서 유연하게 대처하는 중요성을 깊이 이해하게 되었습니다.

### 코루틴을 사용한 비동기 처리의 학습 및 도전

이 프로젝트에서 코루틴을 사용하여 비동기적인 작업 처리를 구현하는 과정은 흥미롭지만 동시에 도전적인 경험이었습니다. 

##### 학습 및 적용 과정:
- 코루틴을 사용하여 datastore을 관리 했습니다. datastore는 비동기적으로 수행 되어야했기 때문에 코루틴을 사용하는건 선택이 아닌 필수였습니다.
- 그러나, 코루틴의 복잡한 개념과 동시성 관리에 대한 이해가 부족하여, 초기에는 여러 비동기 처리에서 어려움을 겪었습니다. 특히 캐시삭제, 로그아웃을 하고 재접속 했을 때 파이어베이스에서 데이터를 다시 받아와 datastore에 넣어주거나 연속해서 코루틴을 사용하는 상황에서 코드가 예상과 다르게 작동하는 등의 큰 어려움을 겪었습니다.

##### 현재 진행 상황:
- 현재는 코루틴을 통한 비동기 처리에 대해 지속적으로 학습하고 있으며, 코루틴을 활용한 앱의 기능들을 리팩토링하는 과정에 있습니다. 이 과정에서 코루틴의 다양한 패턴과 최적의 사용 방법을 탐색하고 있습니다.
- 또한, 코드의 가독성과 유지보수성을 향상시키기 위해 코루틴의 스코프 및 디스패처를 적절히 활용하는 방법에 대해 집중적으로 공부하고 있습니다.

##### 앞으로의 계획:
- 코루틴과 관련된 최신 트렌드를 계속해서 탐구하여 프로젝트에 적용할 예정입니다. 이를 통해 더 효율적이고 안정적인 비동기 처리를 구현하고자 합니다.
- 이러한 경험을 통해 안드로이드 앱 개발의 성능 최적화와 비동기 프로그래밍에 대한 깊은 이해를 더욱 발전시키고자 합니다.
- 캐시가 삭제됐을 때 Firebase에서 데이터를 받아온 데이터를 로컬에  넣고 UI 작업을 진행하는 데 어려움을 겪고 있는데(현재는 2번째 접속에 해당 작업이 완료됩니다) 이 오류를 빨리 해결하는 걸 구체적인 계획으로 잡고 있습니다.
</details>

