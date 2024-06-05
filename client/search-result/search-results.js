// 웹 페이지 이동
var webTitle = document.getElementsByClassName('web-title');
if (webTitle.length > 0) {
    webTitle[0].onclick = function () {
        window.location.href = '/main_page/index.html';
    };
}

var loginButton = document.getElementsByClassName('login-button');
if (loginButton.length > 0) {
    loginButton[0].onclick = function () {
        window.location.href = '/login-page/login-page.html';
    };
}

var signupButton = document.getElementsByClassName('signup-button');
if (signupButton.length > 0) {
    signupButton[0].onclick = function () {
        window.location.href = '/join_page/sign-up.html';
    };
}


// 검색 결과
document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const query = urlParams.get('query') || '';
    const option = urlParams.get('option') || 'title';

    const headingElement = document.getElementById('search-result-heading');

    if (query) {
        headingElement.innerHTML = `<span>"${query}"의 검색 결과</span>`;
    } else {
        headingElement.innerHTML = `<span>모든 검색 결과</span>`;
    }

    if (!query) {
        // 검색어가 없는 경우 -> 빈 페이지 띄우기
        fetch('', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                displayResults(data, 1); // 첫 페이지 표시, data는 서버에서 받은 게시글 데이터
                setupPagination(data);
            })
            .catch(error => {
                console.error('Error fetching all movies:', error);
            });
        // query 값이 있는 경우
    } else if (['nation', 'genre', 'actor', 'director'].includes(option)) {
        searchByTag(option, query).then(data => {
            displayResults(data, 1); // 첫 페이지 표시
            setupPagination(data);
        });
    } else {
        searchMovies(option, query).then(data => {
            displayResults(data, 1); // 첫 페이지 표시
            setupPagination(data);
        });
    }
});

function searchMovies(option, query) {
    return fetch(`https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/movie/search/json?option=${option}&query=${encodeURIComponent(query)}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            return data;
        })
        .catch(error => {
            console.error('Error fetching movie data:', error);
        });
}

// 서버로부터 받은 데이터를 화면에 표시
function displayResults(data, page) {
    const resultsContainer = document.getElementById('results-container');
    resultsContainer.innerHTML = '';  // 이전 검색 결과 초기화

    const startIndex = (page - 1) * 12;  // 현재 페이지의 첫 번째 영화의 인덱스
    const endIndex = startIndex + 12;    // 현재 페이지의 마지막 영화의 인덱스
    const pageData = data.slice(startIndex, endIndex);  // 새로운 배열

    pageData.forEach(item => {
        const itemElement = document.createElement('div');  // 개별 영화 정보
        itemElement.classList.add('result-item');
        itemElement.innerHTML = `
            <img src="${item.postersList[0]}" alt="${item.title} 포스터">
            <h3 class="result-title">${item.title}</h3>
            <p class="result-details">
                <span class="result-genre">${item.genre}</span>
                <span class="result-nation">· ${item.nation}</span>
            </p>
        `;
        // 영화 정보, 포스터 클릭 시 상세 페이지로 이동     
        itemElement.addEventListener('click', () => {
            window.location.href = `/detail_page/detail_page.html?id=${item.movieId}`;
        });
        resultsContainer.appendChild(itemElement);
    });
}

// 페이지네이션 생성
function setupPagination(data) {  // data: 검색 결과로 반환된 영화 데이터 배열
    const pagination = document.getElementById('pagination');  // 페이지네이션 버튼
    const totalPages = Math.ceil(data.length / 12); // 한 페이지 당 12개의 결과

    pagination.innerHTML = ''; // 페이지네이션 버튼을 초기화

    for (let i = 1; i <= totalPages; i++) {
        const button = document.createElement('button');
        button.textContent = i;
        button.addEventListener('click', () => {
            displayResults(data, i);
        });
        pagination.appendChild(button);
    }
}