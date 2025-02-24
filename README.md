# Photo-Collector

Unsplash API를 활용해 이미지를 탐색하고, 보관함에 저장하는 안드로이드 앱입니다.

<br><br>
## 🎯 프로젝트 주요 관심사

### [브랜치 관리 전략]

* Git Flow 전략 사용
  
  * 기능별로 Branch를 분리하여 개발을 진행하였습니다.
  * 모든 브랜치는 Pull Request에 **리뷰를 진행한 후 squash merge** 하였습니다.
  * Git 관리는 **Sourcetree** 툴을 사용하였습니다.

<img src="https://github.com/user-attachments/assets/bd8134d5-bc9c-43eb-aff9-a776e4ce5f46" height="300">

<br><br>

* 실제 활용 예시

<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/34f3ac94-a034-46df-9ced-91a60a380756" height="300"> <br>
      Develop branch </td>
    <td><img src="https://github.com/user-attachments/assets/555468e0-d324-452e-b138-62ec7b8ca449" height="300"> <br>
      Feature branch </td>
  </tr>
</table>

<br>

### [Testing]

* CI/CD

  - **Github Actions** 을 활용하여 CI/CD 파이프라인을 구축하여 빌드, 테스트, 배포와 같은 작업을 자동화 하였습니다.
  - 다양한 수준의 자동화된 테스트(단위 및 통합 테스트)를 실행하여 **제대로 working 하도록 검증하는 것** 을 목표로 하였습니다.

* API Test

  - **Okhttp-profiler** 를 이용하여 OkHttp 요청 또는 응답 헤더를 디버깅 하고, api 호출 작업을 tracking 하였습니다.

### [주요 기능]

* 로컬 DB를 활용한 데이터 캐싱

  - `RemoteMediator`를 구현하는 공식 메뉴얼을 따라, 로컬 `Room` DB와 네트워크 호출에서 데이터를 load 하였다.
  - 로컬 DB 캐시에서 UI를 구동하고 더 이상 데이터가 없을 때만 네트워크 요청으로 새로운 데이터를 load 한다.

* `DiffUtil`을 활용한 페이징 처리로 List 렌더링 최적화
  
* apk 사이즈 경량화

  - `R8 build` 를 적용하여 앱 크기 축소화 및 경량화 하여 **약 55.13% 용량 감소**하였습니다.

<table>
  <tr>
    <th>R8 적용 전</th>
  </tr>
  <tr>
    <td>
      <img src="https://github.com/user-attachments/assets/039b45fd-1b0c-4e2b-9a79-4b992f2d664a" width="600">
    </td>
  </tr>
  <tr>
    <th>R8 적용 후</th>
  </tr>
  <tr>
    <td>
      <img src="https://github.com/user-attachments/assets/7313146f-d85d-44de-a071-8cb65d4c249f" width="600">
    </td>
  </tr>
</table>

<br>


## 💥트러블 슈팅

### 1. Async로 API 통신 속도 및 성능 개선

🚨문제 배경

* [Business] 사용자가 검색어를 입력하면 query를 search하여 해당 이미지의 id 리스트를 받아와 id들을 파싱하여 개별 api 호출 후 결과값(이미지)을 받는다.
* [Tech] api 통신 과정에서 여러 개의 개별 호출 개수가 많아짐에 따라 속도가 느려지거나 통신이 불안정하다.

<br>

💭해결 방법

* `delay()`로 api 호출에 대기 시간을 걸고, `okhttp-profiler` 플러그인로 트래킹 해보니, 0.2초 단위로 flat하게 반환 되는 것을 확인하였다.

<img src="https://github.com/user-attachments/assets/7d6eed75-d20a-49ec-ba54-bc42c75be5d7" width="600">

* `Deffered`를 반환하는 `async`의 구동 방식을 알게 되어, 반환값이 있는 비동기 통신 시 Non-Blocking으로 병렬 처리한 `awaitAll()`로 한 번에 채워진 list를 가져올 수 있었다.

<br>

💡배운점

* `Coroutine`으로 네트워크 중첩 구조의 비동기 처리를 Non-blocking으로 해결하여 API 실행 속도 8초에서 2초로 **75% 감소**되었습니다.
* `Deferred`와 `Job`의 차이를 알게 되었고, `CoroutineScope`로 비동기 작업을 수행할 때 목적에 따라 어떻게 사용해야 하는지 알게 되었습니다.
  
<br>

### 2. Debounce 로 타이핑 이벤트 핸들링

🚨문제 배경

* [Business] 사용자가 이미지를 탐색하기 위해 검색 query를 작성하면 자동 검색 활동이 시작되어 이미지를 출력한다.
* [Tech] 타이핑 과정에서 잦은 API 호출이 연속될 시 불필요한 통신이 생길 수 있다.
* [Tech] api 호출 도중에 또 다른 요청이 들어오게 되면 네트워크 통신 과정에서 충돌이 생길 수 있다.

<br>

💭해결 방법

* 이벤트 핸들링에 대해 `Debounce`라는 개념을 알게 되어, query가 작성되는 동안 0.3초 정도 대기하도록 제어할 수 있게 되었다. 타이핑 이벤트가 끝나면 마지막에 API 호출로 이어지게 하여 **잦은 호출을 방지**하였다.
* `CoroutineScope` 내부적으로 `Job`을 가지고 있어, 이를 통해 실행 중인 `Coroutine`을 추적하고 제어할 수 있다는 것을 알게 되었다.
* 따라서 api 호출이 겹치게 되면 요청을 `cancel()`하고 마지막 이벤트의 결과값만 반환하는 게 가능해졌다.

<br>

💡배운점

* 단순 타이핑으로 이미지를 바로 보여주는 편리 기능을 수행하면서도, 타이핑 활동에 있어 **안정성 있는 이벤트 핸들링**이 가능해졌다.
* 이벤트 핸들링을 고려해 보면서, `Debounce`와 `Throttle`의 차이를 알게 되었고, `CoroutineScope`를 행하는 `Job`의 세부 동작 원리를 알게 되었다.

<br><br>

## 🏗 Architecture & Modularization
* **Clean Architecture**를 기반으로 **MVVM 패턴**에 따라 모듈화 하였습니다.

<img src="https://github.com/user-attachments/assets/263cb50d-8030-418c-ac6e-26526e97255f" height="400">

<br><br>

## 📕 Application Feature
* 메인 화면에서는 매일 새로운 list의 image를 보여줍니다.
* 이미지를 클릭하면 해당 이미지에 대한 상세 정보를 확인할 수 있습니다.
  
  * 내용 (description)
  * 사용자 (user name)
  * 좋아요수 (likes)
  * 업로드 날짜 (created at)

* 이미지의 하트버튼을 누르면 하트 아이콘의 색깔이 채워지고, 해당 이미지가 내 보관함에 저장됩니다.
* 다시 클릭하면 하트 아이콘의 색깔이 비워잠과 동시에 보관함에서 제거됩니다.
* 저장된 이미지는 북마크 페이지에서 저장했던 이미지의 최신순으로 보이고,   
  보관한 이미지들은 앱 재시작 후에도 다시 확인할 수 있습니다.
* 북마크 기능은 홈 화면에서, 상세 페이지에서, 보관함에서 모두 적용됩니다.
* 검색창에 문자를 입력하면 자동으로 search 활동이 시작되고, 키워드에 해당하는 이미지들이 홈화면에 대체되어 출력됩니다.
* 검색어를 입력하지 않으면, 초기에 보였던 이미지들이 기본적으로 보여집니다.

<br><br>

## 🛠 Skill Set

| 구분 | skill |
|---|------|
| Language | `Koltin` |
| Architecture | `MVVM` |
| DI | `Hilt` |
| Networking | `Retrofit` `Okhttp` |
| Asynchronous | `Coroutine` `Flow` |
| Jetpack | `AAC` `LiveData` `ViewModel` `Databinding` |
| Local DB | `Room` `Pager` |
| Image | `Glide` `Cardview` |
| Collaboration | `Slack` `Sourcetree` |
| ETC | `Paging3` `DiffUtil` `Timber` |
