document.addEventListener('DOMContentLoaded', () => {
    // 웹 페이지 이동
    document.querySelector('.web-title').onclick = () => {
        window.location.href = '/main_page/index.html';
    };

    document.querySelector('.login-button').onclick = () => {
        window.location.href = '/login/login-page.html';
    };

    document.querySelector('.signup-button').onclick = () => {
        window.location.href = '/join_page/join_page2.html';
    };

    // 게시글 클릭
    function handleClick(movieSeq) {
        const apiUrl = `https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/movies/detail/${movieSeq}`;
        fetch(apiUrl)
            .then((response) => response.json())
            .then((data) => {
                console.log("Movie detail response:", data);
                // 받은 데이터를 활용하여 상세 정보를 표시하거나 다른 작업 수행
            })
            .catch((error) => {
                console.error("Error fetching movie detail:", error);
            });
    }

    // 메인페이지 영화 목록 표시
    const sortOption = document.getElementById('sort-option');
    const prevPageButton = document.getElementById('prevPage');
    const nextPageButton = document.getElementById('nextPage');
    const pageInfo = document.getElementById('pageInfo');

    let currentPage = 1; // 현재 페이지
    const moviesPerPage = 10; // 한 페이지에 표시할 영화 개수

    // 페이지 로드 시 최신순으로 영화 가져오기
    fetchMovies('latest', currentPage);

    // 정렬 옵션 변경 이벤트 리스너
    sortOption.addEventListener('change', (event) => {
        const selectedOption = event.target.value;
        currentPage = 1;
        fetchMovies(selectedOption, currentPage);
    });

    // 페이지네이션 버튼 이벤트 리스너
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
        let apiUrl;

        // 정렬 옵션에 따라 API URL 설정
        if (orderBy === 'latest') {
            apiUrl = `https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/movies/latest`;
        } else if (orderBy === 'comments') {
            apiUrl = `https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/OrderByGradeCountDesc`;
        }

        // API 요청
        fetch(apiUrl)
            .then(response => response.json())
            .then(data => {
                if (data.status === 500) {
                    alert(data.message);
                } else {
                    if (orderBy === 'latest') {
                        displayLatestMovies(data);
                    } else if (orderBy === 'comments') {
                        displayCommentedMovies(data);
                    }
                    updatePageInfo(currentPage, data.totalPages);
                }
            })
            .catch(error => {
                console.error("Error fetching movies:", error);
                alert("영화 데이터를 불러오는 중 오류가 발생했습니다.");
            });

    }

    // 최신순 영화 데이터를 화면에 표시
    function displayLatestMovies(movies) {
        const moviesGrid = document.getElementById("moviesGrid");
        moviesGrid.innerHTML = "";

        const dummyImageUrl = 'https://via.placeholder.com/200x300'; // 더미 이미지 URL

        movies.forEach(movie => {
            const movieCard = document.createElement("div");
            movieCard.classList.add("movie-card");

            // 포스터 이미지 가져오기, 없으면 더미 이미지 사용
            const posterUrl = (movie.postersList && movie.postersList.length > 0) ? movie.postersList[0] : dummyImageUrl;

            movieCard.innerHTML = `
                <img src="${posterUrl}" alt="${movie.title}" class="movie-poster" data-movie-id="${movie.movieSeq}">
                <div class="movie-title" data-movie-id="${movie.movieSeq}">${movie.title}</div>
                <div class="movie-rating" style="margin-top: -10px">★ ${movie.rating}</div>
            `;
            moviesGrid.appendChild(movieCard);
        });

        const movieElements = document.querySelectorAll('.movie-poster, .movie-title');
        movieElements.forEach(element => {
            element.addEventListener('click', () => {
                const movieId = element.getAttribute('data-movie-id');
                handleClick(movieId);
            });
        });
    }

    // 댓글순 영화 데이터를 화면에 표시
    function displayCommentedMovies(movies) {
        const moviesGrid = document.getElementById("moviesGrid");
        moviesGrid.innerHTML = "";

        movies.forEach(movie => {
            const movieCard = document.createElement("div");
            movieCard.classList.add("movie-card");

            movieCard.innerHTML = `
                <img src="${posterUrl}" alt="${movie.title}" class="movie-poster" data-movie-id="${movie.movieSeq}">
                <div class="movie-title" data-movie-id="${movie.movieSeq}">${movie.title}</div>
                <div class="movie-rating" style="margin-top: -10px">★ ${movie.rating}</div>
            `;
            moviesGrid.appendChild(movieCard);
        });

        const movieElements = document.querySelectorAll('.movie-title');
        movieElements.forEach(element => {
            element.addEventListener('click', () => {
                const movieId = element.getAttribute('data-movie-id');
                handleClick(movieId);
            });
        });
    }

    // 페이지 정보를 업데이트하는 함수
    function updatePageInfo(currentPage, totalPages) {
        pageInfo.textContent = `Page ${currentPage} of ${totalPages}`;
        prevPageButton.disabled = currentPage === 1;
        nextPageButton.disabled = currentPage === totalPages;
    }
});
