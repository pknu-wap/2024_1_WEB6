document.addEventListener("DOMContentLoaded", () => {
  // 웹 페이지 이동
  document.querySelector(".web-title").onclick = () => {
    window.location.href = "../main_page/index.html";
  };

  document.querySelector(".login-button").onclick = () => {
    window.location.href = "../login/login-page.html";
  };

  document.querySelector(".signup-button").onclick = () => {
    window.location.href = "../join_page/join_page2.html";
  };

  // 게시글 클릭


  // 메인페이지 영화 목록 표시
  const sortOption = document.getElementById("sort-option");
  const prevPageButton = document.getElementById("prevPage");
  const nextPageButton = document.getElementById("nextPage");
  const pageInfo = document.getElementById("pageInfo");

  let currentPage = 1; // 현재 페이지
  const moviesPerPage = 10; // 한 페이지에 표시할 영화 개수
  let allMovies = []; // 모든 영화 데이터를 저장할 배열
  const maxPages = 5; // 최대 페이지 수 추가

  // 페이지 로드 시 최신순으로 영화 가져오기
  fetchMovies("latest");

  // 정렬 옵션 변경 이벤트 리스너
  sortOption.addEventListener("change", (event) => {
    const selectedOption = event.target.value;
    currentPage = 1;
    fetchMovies(selectedOption);
  });

  // 페이지네이션 버튼 이벤트 리스너
  prevPageButton.addEventListener("click", () => {
    if (currentPage > 1) {
      currentPage--;
      displayMovies(sortOption.value); // orderBy 값을 전달
    }
  });

  nextPageButton.addEventListener("click", () => {
    if (
      currentPage <
      Math.min(Math.ceil(allMovies.length / moviesPerPage), maxPages)
    ) {
      // 수정된 부분: 최대 페이지 수 체크
      currentPage++;
      displayMovies(sortOption.value); // orderBy 값을 전달
    }
  });

  // 영화 데이터 가져오기
  function fetchMovies(orderBy) {
    let apiUrl;

    // 정렬 옵션에 따라 API URL 설정
    if (orderBy === "latest") {
      apiUrl = `https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/movies/latest`;
    } else if (orderBy === "comments") {
      apiUrl = `https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/OrderByGradeCountDesc`;
    }

    // API 요청
    fetch(apiUrl)
      .then((response) => response.json())
      .then((data) => {
        if (data.status === 500) {
          alert(data.message);
        } else {
          allMovies = data;
          console.log(
            `Fetched ${allMovies.length} movies for order: ${orderBy}`
          ); // 디버깅 로그 추가
          displayMovies(orderBy); // 변경된 부분: orderBy를 displayMovies 함수로 전달
        }
      })
      .catch((error) => {
        console.error("Error fetching movies:", error);
        alert("영화 데이터를 불러오는 중 오류가 발생했습니다.");
      });
  }

  // 영화 데이터를 화면에 표시
  function displayMovies(orderBy) {
    const moviesGrid = document.getElementById("moviesGrid");
    moviesGrid.innerHTML = "";

    const filteredMovies = allMovies.filter((movie) => {
      if (orderBy === "latest") {
        return movie.postersList && movie.postersList.length > 0;
      } else if (orderBy === "comments") {
        return movie.poster && movie.poster.trim() !== "";
      }
      return false;
    });

    console.log(`Filtered ${filteredMovies.length} movies for display`); // 디버깅 로그 추가

    const startIndex = (currentPage - 1) * moviesPerPage;
    const endIndex = startIndex + moviesPerPage;
    const moviesToDisplay = filteredMovies.slice(startIndex, endIndex);

    const dummyImageUrl = "https://via.placeholder.com/200x300"; // 더미 이미지 URL

    moviesToDisplay.forEach((movie) => {
      const movieCard = document.createElement("div");
      movieCard.classList.add("movie-card");

      if (orderBy === "latest") {
        const posterUrl =
          movie.postersList && movie.postersList.length > 0
            ? movie.postersList[0]
            : dummyImageUrl;
        movieCard.innerHTML = `
                <img src="${posterUrl}" alt="${movie.title}" class="movie-poster" data-movie-seq="${movie.movieSeq}" data-movie-id="${movie.movieId}">
                <div class="movie-title" data-movie-seq="${movie.movieSeq}" data-movie-id="${movie.movieId}">${movie.title}</div>
                <div class="movie-genre">${movie.genre}</div>
              `;
      } else if (orderBy === "comments") {
        const posterUrl = movie.poster ? movie.poster : dummyImageUrl;
        movieCard.innerHTML = `
                <img src="${posterUrl}" alt="${movie.title}" class="movie-poster" data-movie-seq="${movie.movieSeq}" data-movie-id="${movie.movieId}">
                <div class="movie-title" data-movie-seq="${movie.movieSeq}" data-movie-id="${movie.movieId}">${movie.title}</div>
                <div class="movie-genre">${movie.gradeCount} Reviews</div>
              `;
      }

      moviesGrid.appendChild(movieCard);
    });

    // 영화 카드 클릭 이벤트 리스너 추가
    const movieElements = document.querySelectorAll(".movie-poster, .movie-title");
    movieElements.forEach((element) => {
      element.addEventListener("click", () => {
        const movieSeq = element.getAttribute("data-movie-seq");
        const movieId = element.getAttribute("data-movie-id");
        handleClick(movieSeq, movieId);
      });
    });

    updatePageInfo(
      currentPage,
      Math.min(Math.ceil(filteredMovies.length / moviesPerPage), maxPages) // 수정된 부분: 최대 페이지 수 체크
    );
  }

  // 영화 카드 클릭 시 호출되는 함수
  function handleClick(movieSeq, movieId) {
    // movieSeq와 movieId를 URL 파라미터로 전달하며 이동
    window.location.href = `../detail_page/DetailPageTest.html?movieSeq=${movieSeq}&movieId=${movieId}`;
    console.log(`${movieSeq}, ${movieId}`)
  }

  // 페이지 정보를 업데이트하는 함수
  function updatePageInfo(currentPage, totalPages) {
    pageInfo.textContent = `${currentPage} / ${totalPages}`;
    prevPageButton.disabled = currentPage === 1;
    nextPageButton.disabled = currentPage === totalPages;
  }
});
