// 웹 페이지 이동
var webTitle = document.getElementsByClassName('web-title');
webTitle[0].onclick = function () {
    window.location.href = '/main_page/index.html';
};

var loginButton = document.getElementsByClassName('login-button');
loginButton[0].onclick = function () {
    window.location.href = '/login/login-page.html';
};

var signupButton = document.getElementsByClassName('signup-button');
signupButton[0].onclick = function () {
    window.location.href = '/join_page/join_page.html';
};

// 검색
document.addEventListener('DOMContentLoaded', () => {
    const searchForm = document.getElementById('search-form');
    const searchInput = document.getElementById('search-input');
    const searchOption = document.getElementById('search-option');

    searchForm.addEventListener('submit', (event) => {
        event.preventDefault(); // 폼 제출 기본 동작을 막음

        // 검색어 옵션 가져오기
        const query = searchInput.value.trim();
        const option = searchOption.value;

        if (query) {
            // 검색어가 있는 경우
            window.location.href = `https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/movies/search/json-tag?option=${option}&query=${encodeURIComponent(query)}`;
        } else {
            // 검색어가 공백인 경우
            window.location.href = 'https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/movies/search/json-tag';
        }
    });
});

function handleClick(movieSeq) {
    const apiUrl = `https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/movies/detail/${movieSeq}`;
    const options = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ movieSeq }), // movieSeq를 JSON 형식으로 변환하여 서버로 전송
    };

    fetch(apiUrl, options)
        .then((response) => response.json())
        .then((data) => {
            console.log("Movie detail response:", data); // 서버에서 받은 데이터를 콘솔에 출력
            // 받은 데이터를 활용하여 상세 정보를 표시하거나 다른 작업 수행
        })
        .catch((error) => {
            console.error("Error fetching movie detail:", error);
        });
}

// 영화 표시
document.addEventListener('DOMContentLoaded', () => {
    const sortOption = document.getElementById('sort-option');
    const searchForm = document.getElementById('search-form');
    const searchInput = document.getElementById('search-input');
    const searchOption = document.getElementById('search-option');
    const prevPageButton = document.getElementById('prevPage');
    const nextPageButton = document.getElementById('nextPage');
    const pageInfo = document.getElementById('pageInfo');


    let currentPage = 1;   // 현재 페이지
    const moviesPerPage = 10;   // 한 페이지에 표시할 영화 개수

    // 페이지 로드 시 최신순으로 영화 가져오기
    fetchMovies('latest', currentPage);

    // 정렬 옵션이 변경될 때 이벤트 리스너 추가
    sortOption.addEventListener('change', (event) => {
        const selectedOption = event.target.value;
        currentPage = 1;
        fetchMovies(selectedOption, currentPage);
    });

    // 검색 폼 제출 이벤트 리스너 추가
    searchForm.addEventListener('submit', (event) => {
        event.preventDefault(); // 폼 제출 기본 동작을 막음

        // 검색어 옵션 가져오기
        const query = searchInput.value.trim();
        const option = searchOption.value;

        if (query) {
            // 검색어가 있는 경우
            window.location.href = `https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/movies/search/json-tag?option=${option}&query=${encodeURIComponent(query)}`;
        } else {
            // 검색어가 공백인 경우
            window.location.href = 'https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/movies/findAll';
        }
    });

    // 페이지네이션 버튼 이벤트 리스너 추가
    prevPageButton.addEventListener('click', () => {
        if (currentPage > 1) {
            currentPage--;
            fetchMovies(sortOption.value, currentPage);
        }
    });

    nextPageButton.addEventListener('click', () => {
        currentPage++;
        fetchMovies(sortOption.value, currentPage);
    });

    // 영화 데이터 가져오기
    function fetchMovies(orderBy, page) {
        // 더미 데이터
        const dummyData = {
            "movies": [
                {
                    "movieSeq": 1,
                    "title": "Movie Title 1",
                    "prodYear": "2021",
                    "posterUrl": "https://via.placeholder.com/200x300",
                    "rating": 4
                },

            ],
            "totalPages": 1
        };
        const data = dummyData; // 더미 데이터를 사용합니다.
        // 데이터를 화면에 표시합니다.
        displayMovies(data.movies);
        updatePageInfo(data.totalPages);


        let apiUrl;

        // 정렬 옵션에 따라 API URL 설정
        if (orderBy === 'latest') {
            apiUrl = `https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/movie/latest?page=${page}&size=${moviesPerPage}`;
        } else if (orderBy === 'comments') {
            apiUrl = `https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/Top5OrderByGradeCountDesc?page=${page}&size=${moviesPerPage}`;
        }

        // API 요청
        fetch(apiUrl)
            .then(response => response.json())
            .then(data => {
                displayMovies(data.movies);
                updatePageInfo(data.totalPages);
            })
            .catch(error => console.error("Error fetching movies:", error));
    }

    // 영화 데이터를 화면에 표시
    function displayMovies(movies) {
        const moviesGrid = document.getElementById("moviesGrid");
        moviesGrid.innerHTML = "";

        movies.forEach(movie => {
            const movieCard = document.createElement("div");
            movieCard.classList.add("movie-card");  // 영화 카드 스타일
            movieCard.innerHTML = `
            <img src="${movie.posterUrl}" alt="${movie.title}">
            <div class="movie-title">${movie.title} (${movie.commentCount})</div>
            <div class="movie-rating">★ ${movie.rating}</div>
            `;
            moviesGrid.appendChild(movieCard);   // 영화 카드를 영화 목록에 추가
        });
    }

    //페이지 정보를 업데이트하는 함수
    function updatePageInfo(totalPages) {
        pageInfo.textContent = `Page ${currentPage} of ${totalPages}`;
        prevPageButton.disabled = currentPage === 1;
        nextPageButton.disabled = currentPage === totalPages;
    }
});


//
document.getElementById('login-button').addEventListener('click', function () {
    // 로그인 로직 구현 후 성공 시 닉네임 설정
    const nickname = '닉네임'; // 실제 로그인 로직으로부터 닉네임 받아오기
    const loginSection = document.getElementById('login-section');

    loginSection.innerHTML = `<span>${nickname}님 환영합니다.</span>
                              <button id="logout-button">로그아웃</button>`;

    document.getElementById('logout-button').addEventListener('click', function () {
        // 로그아웃 로직 구현
        loginSection.innerHTML = `<button class="login-button" id="login-button">로그인</button>
                                  <button class="signup-button">회원가입</button>`;
        // 로그인 버튼 이벤트 리스너 다시 설정
        document.getElementById('login-button').addEventListener('click', arguments.callee);
    });
});
