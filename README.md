# Photo-Collector

Unsplash API를 활용해 이미지를 탐색하고, 보관함에 저장하는 안드로이드 앱입니다.

<br><br>

## 🏗 Architecture & Modularization
* **Clean Architecture**를 기반으로 **MVVM 패턴**에 따라 모듈화 하였습니다.

<img src="https://github.com/user-attachments/assets/263cb50d-8030-418c-ac6e-26526e97255f" height="400">

<br><br>
## 🎯 프로젝트 주요 관심사

<h3>[브랜치 관리 전략]</h3>

* Git Flow 전략 사용
  
  * 기능별로 Branch를 분리하여 개발을 진행하였습니다.
  * 모든 브랜치는 Pull Request에 **리뷰를 진행한 후 merge** 하였습니다.
  * Git 관리는 **Sourcetree** 툴을 사용하였습니다.

<img src="https://github.com/user-attachments/assets/bd8134d5-bc9c-43eb-aff9-a776e4ce5f46" height="300">

<br><br>

* 실제 활용된 모습

<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/34f3ac94-a034-46df-9ced-91a60a380756" height="400"> <br>
      Develop branch </td>
    <td><img src="https://github.com/user-attachments/assets/555468e0-d324-452e-b138-62ec7b8ca449" height="400"> <br>
      Feature branch </td>
  </tr>
</table>

<br>

<h3>[Testing]</h3>

* CI/CD

  * **Github Actions** 을 활용하여 CI/CD 파이프라인을 구축하여 빌드, 테스트, 배포와 같은 작업을 자동화하였습니다.
  * 다양한 수준의 자동화된 테스트(단위 및 통합 테스트)를 실행하여 **제대로 working 하도록 검증하는 것** 을 목표로 하였습니다.

* API Test

  * **Okhttp-profiler** 를 이용하여 OkHttp 요청 또는 응답 헤더를 디버깅 하고, api 호출 작업을 tracking 하였습니다.

<br>

<h3>[Resource 관리]</h3>

* **사용자 경험을 향상**시키는 것을 목적으로 `Job` 및 `Debounce`를 활용하여 **효율적인 api 호출 작업**을 유도하였습니다.

* `DiffUtil`로 변경 부분만 감지하여 다시 그리도록 하여 **UI 업데이트 작업을 최소화** 하였습니다.

* `Room DB` 캐시에서 UI를 구동하도록 하고 새로운 데이터 필요시에만 네트워크 호출을 하도록 구현하여 **오프라인 지원** 및 **불필요한 네트워크 통신**을 줄였습니다.

* `R8 build` 를 적용하여 앱(apk) 크기 축소화 및 경량화 하여 **성능 개선 및 최적화**하였습니다.

<table>
  <tr><td>적용 전<td></tr>
  <tr>
    <td></td>
  </tr>
  <tr><td>적용 후<td></tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/039b45fd-1b0c-4e2b-9a79-4b992f2d664a" width="style=max-width"></td>
  </tr>
</table>

<br><br>

## 💥 TroubleShooting

프로젝트를 진행하며 겪은 기술적 문제를 해결해 가는 과정과 이를 통해 배운 점을 정리하여 기록하였습니다. <br>
자세한 내용은 해당 Wiki 페이지에서 확인하실 수 있습니다.

- [Debounce로 타이핑 이벤트 핸들링](https://github.com/f-lab-edu/Photo-Collector/wiki/Debounce-%EB%A1%9C-%ED%83%80%EC%9D%B4%ED%95%91-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%ED%95%B8%EB%93%A4%EB%A7%81)

- [Async로 api 통신 성능 개선](https://github.com/f-lab-edu/Photo-Collector/wiki/aync%EB%A1%9C-api-%ED%86%B5%EC%8B%A0-%EC%84%B1%EB%8A%A5-%EA%B0%9C%EC%84%A0)

- [Network 및 Database의 페이징 처리](https://github.com/f-lab-edu/Photo-Collector/wiki/Network-%EB%B0%8F-Database%EC%9D%98-Pagination)

<br><br>

## 📕 Application Feature
* 메인 화면에서는 매일 새로운 list의 image를 보여줍니다.

* 이미지를 클릭하면 해당 이미지에 대한 상세 정보를 확인할 수 있습니다.
  
  * 제목 (description)
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

