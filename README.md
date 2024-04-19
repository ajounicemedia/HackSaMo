# 🎓 무료 과외 매칭 플랫폼 학사모  

## 📜 학사모 소개 및 IA
![프로젝트 소개 이미지](https://github.com/ajounicemedia/HackSaMo/blob/main/%ED%95%99%EC%82%AC%EB%AA%A8%EA%B0%84%EB%8B%A8%EC%84%A4%EB%AA%85.png)
![프로젝트 IA](https://github.com/ajounicemedia/HackSaMo/blob/main/%ED%95%99%EC%82%AC%EB%AA%A8%20ia.png)
## 🔄 학사모 기획서 및 플레이스토어 링크
  - [학사모 기획서](https://github.com/ajounicemedia/HackSaMo/blob/main/%ED%95%99%EC%82%AC%EB%AA%A8%20%EA%B8%B0%ED%9A%8D%EC%84%9C.pdf)
  - [플레이스토어 링크](https://play.google.com/store/apps/details?id=god.example.god_of_teaching)

## 🎥 인앱 영상
![인앱 영상](https://github.com/ajounicemedia/HackSaMo/blob/main/%EC%9D%B8%EC%95%B1%20%EC%98%81%EC%83%81.gif)
![인앱 영상2](https://github.com/ajounicemedia/HackSaMo/blob/main/%EC%9D%B8%EC%95%B1%20%EC%98%81%EC%83%812.gif)


## 🏗 아키텍쳐 패턴


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

- ## 💡 학습 사항 

  
이 프로젝트를 통해, 안드로이드 개발의 다양한 최신 라이브러리와 기술을 심도 있게 학습하고 적용했습니다. 각 기술의 적용과 학습 과정은 다음과 같습니다:

- **데이터 바인딩(Data binding)과 뷰 바인딩(View Binding)**: 레이아웃과 데이터 모델 간의 연결을 강화하고, 더 깔끔하고 유지보수가 쉬운 코드를 작성하기 위해 데이터 바인딩과 뷰 바인딩을 적극적으로 활용했습니다. 이를 통해 UI 컴포넌트와 데이터 소스 간의 결합을 최소화하고, 더 안정적인 앱 개발을 경험했습니다.

- **LiveData**: UI와 데이터 상태의 일관성을 유지하기 위해 LiveData를 사용했습니다. LiveData를 활용하여 데이터의 변경사항을 UI에 실시간으로 반영할 수 있었으며, 이를 통해 반응형 애플리케이션 개발에 대한 이해를 깊게 했습니다.

- **Navigation 컴포넌트**: 네비게이션 컴포넌트를 통해 프래그먼트 간의 전환을 더욱 효율적으로 관리하고, 복잡한 사용자 인터페이스 흐름을 간소화하는 방법을 배웠습니다.

- **DataStore와 SharedPreferences**: 사용자의 설정 및 앱 설정을 관리하기 위해 DataStore와 SharedPreferences를 사용했습니다. 이를 통해 효과적인 데이터 저장 및 검색 방법에 대해 학습하고, 성능 및 보안 측면에서의 차이점을 이해했습니다.

- **Hilt를 이용한 의존성 주입**: Hilt를 사용하여 앱의 의존성 관리를 간소화했습니다. 이를 통해 코드의 재사용성을 높이고, 모듈 간 결합도를 낮추는 방법을 배웠습니다.

- **코루틴(Coroutine)**: 비동기 작업 처리와 백그라운드 작업 관리를 위해 코루틴을 활용했습니다. 코루틴을 통해 네트워크 호출, 데이터베이스 작업 등을 더 효율적으로 관리하는 방법을 배웠습니다.

- **Paging 라이브러리**: 대량의 데이터를 효과적으로 로드하고 표시하기 위해 Paging 라이브러리를 사용했습니다. 이를 통해 사용자 경험을 향상시키면서 리소스 사용을 최적화하는 방법을 학습했습니다.
   
- **뷰모델(ViewModel)**: 앱의 UI 관련 데이터를 관리하기 위해 뷰모델을 사용했습니다. 뷰모델을 사용하여 UI 로직과 비즈니스 로직을 분리하고, 더욱 테스트하기 쉬운 코드를 작성할 수 있었습니다.

- **파이어베이스(Firebase)**: 백엔드 서비스를 구축하는데 파이어베이스를 사용했습니다. 파이어베이스의 다양한 기능 중, 특히 인증(Authentication), 데이터베이스(Firestore), 스토리지(Storage), 알림(FCM)를 활용하여 사용자 인증, 데이터 저장 및 관리, 파일 저장, 채팅 알림 등의 기능을 구현했습니다. 파이어베이스를 사용함으로써 서버리스 아키텍처를 경험하고, 백엔드 개발이 대략적으로 어떻게 진행되는지 알 수 있었습니다.








