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
    window.location.href = '/join_page/join_page2.html';
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

// 게시글 클릭
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

// 메인페이지 영화 목록 표시
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
                    // "movieSeq": 1,
                    // "title": "Movie Title 1",
                    // "prodYear": "2021",
                    // "posterUrl": "https://via.placeholder.com/200x300",
                    // "rating": 4
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
            // apiUrl = `https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/movie/latest?page=${page}&size=${moviesPerPage}`;
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
            <div class="movie-rating" style="margin-top: -10px">★ ${movie.rating}</div>
            `;
            moviesGrid.appendChild(movieCard);   // 영화 카드를 영화 목록에 추가
        });

        const movieElements = document.querySelectorAll('.movie-poster, .movie-title');
        movieElements.forEach(element => {
            element.addEventListener('click', () => {
                const movieId = element.getAttribute('data-movie-id');
                handleClick(movieId);
            });
        });
    }

    //페이지 정보를 업데이트하는 함수
    function updatePageInfo(totalPages) {
        pageInfo.textContent = `Page ${currentPage} of ${totalPages}`;
        prevPageButton.disabled = currentPage === 1;
        nextPageButton.disabled = currentPage === totalPages;
    }
});



// 로그인 상태 확인
function checkLoginStatus() {
    const loginSection = document.querySelector('.login-section');
    const nickname = sessionStorage.getItem('nickname');

    if (nickname) {
        loginSection.innerHTML = `<span>${nickname}님 환영합니다.</span>
                                  <button id="logout-button">로그아웃</button>`;

        document.getElementById('logout-button').addEventListener('click', function () {
            sessionStorage.removeItem('nickname');
            loginSection.innerHTML = `<button class="login-button" id="login-button">로그인</button>
                                      <button class="signup-button">회원가입</button>`;
            document.getElementById('login-button').addEventListener('click', function () {
                window.location.href = '/login/login-page.html';
            });
        });
    }
}

document.getElementById('login-form').addEventListener('submit', async (event) => {
    event.preventDefault();

    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();

    if (username === '' || password === '') {
        alert('아이디와 비밀번호를 모두 입력해주세요.');
        return;
    }

    const formData = new URLSearchParams();
    formData.append('username', username);
    formData.append('password', password);

    try {
        const response = await fetch('https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/api/members/login-page', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            credentials: 'include',
            body: formData.toString()
        });

        const data = await response.json();
        console.log(data);
        let messageElement = document.getElementById("messageElement");
        messageElement.textContent = data.message;

        if (response.ok && data.message === '로그인 성공') {
            alert(data.message);
            sessionStorage.setItem('nickname', data.nickname); // 서버가 닉네임을 반환한다고 가정
            window.location.href = '/main_page/index.html';
        } else {
            alert(data.message);
        }
    } catch (error) {
        console.error('Error:', error);
        alert('오류가 발생했습니다. 다시 시도해주세요.');
    }
});
