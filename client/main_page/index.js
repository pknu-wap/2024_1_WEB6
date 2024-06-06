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
    // function handleClick(movieSeq) {
    //    const apiUrl = `https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/movies/detail/${movieId}/${movieSeq}`;
    //     fetch(apiUrl)
    //         .then((response) => response.json())
    //         .then((data) => {
    //             console.log("Movie detail response:", data);
    //         })
    //         .catch((error) => {
    //             console.error("Error fetching movie detail:", error);
    //         });
    // }

    // 메인페이지 영화 목록 표시
    const sortOption = document.getElementById("sort-option");
    const prevPageButton = document.getElementById("prevPage");
    const nextPageButton = document.getElementById("nextPage");
    const pageInfo = document.getElementById("pageInfo");

    let currentPage = 1; // 현재 페이지
    const moviesPerPage = 10; // 한 페이지에 표시할 영화 개수
    let allMovies = []; // 모든 영화 데이터를 저장할 배열

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
            displayMovies(sortOption.value); // 변경된 부분: orderBy 값을 전달
        }
    });

    nextPageButton.addEventListener("click", () => {
        if (currentPage < Math.ceil(allMovies.length / moviesPerPage)) {
            currentPage++;
            displayMovies(sortOption.value); // 변경된 부분: orderBy 값을 전달
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
        // 변경된 부분: orderBy 매개변수 추가
        const moviesGrid = document.getElementById("moviesGrid");
        moviesGrid.innerHTML = "";

        const startIndex = (currentPage - 1) * moviesPerPage;
        const endIndex = startIndex + moviesPerPage;
        const moviesToDisplay = allMovies.slice(startIndex, endIndex);
        const dummyImageUrl = "https://via.placeholder.com/200x300"; // 더미 이미지 URL

        moviesToDisplay.forEach((movie) => {
            const movieCard = document.createElement("div");
            movieCard.classList.add("movie-card");

            if (orderBy === "latest") {
                // 변경된 부분: orderBy에 따라 조건문 추가
                const posterUrl =
                    movie.postersList && movie.postersList.length > 0
                        ? movie.postersList[0]
                        : dummyImageUrl;
                movieCard.innerHTML = `
              <img src="${posterUrl}" alt="${movie.title}" class="movie-poster" data-movie-id="${movie.movieSeq}">
              <div class="movie-title" data-movie-id="${movie.movieSeq}">${movie.title}</div>
              <div class="movie-genre">${movie.genre}</div>
            `;
            } else if (orderBy === "comments") {
                // 변경된 부분: orderBy에 따라 조건문 추가
                const posterUrl = movie.poster ? movie.poster : dummyImageUrl;
                movieCard.innerHTML = `
              <img src="${posterUrl}" alt="${movie.title}" class="movie-poster" data-movie-id="${movie.movieSeq}">
              <div class="movie-title" data-movie-id="${movie.movieSeq}">${movie.title}</div>
              <div class="movie-genre">${movie.gradeCount} Reviews</div>
            `;
            }

            moviesGrid.appendChild(movieCard);
        });

        const movieElements = document.querySelectorAll(
            ".movie-poster, .movie-title"
        );
        movieElements.forEach((element) => {
            element.addEventListener("click", () => {
                const movieId = element.getAttribute("data-movie-id");
                handleClick(movieId);
            });
        });

        updatePageInfo(currentPage, Math.ceil(allMovies.length / moviesPerPage));
    }

    // 페이지 정보를 업데이트하는 함수
    function updatePageInfo(currentPage, totalPages) {
        pageInfo.textContent = `${currentPage} / ${totalPages}`;
        prevPageButton.disabled = currentPage === 1;
        nextPageButton.disabled = currentPage === totalPages;
    }
});