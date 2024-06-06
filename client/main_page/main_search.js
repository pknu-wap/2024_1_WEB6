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
            // 검색 API 호출
            fetch(`https://port-0-web6-1pgyr2mlvnqjxex.sel5.cloudtype.app/movies/search/json?option=${option}&query=${encodeURIComponent(query)}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include' // 쿠키 포함
            })
                .then(response => response.json())
                .then(data => {
                    // 데이터를 URL에 포함시켜 페이지 이동
                    sessionStorage.setItem('searchResults', JSON.stringify(data));
                    sessionStorage.setItem('searchQuery', query);
                    sessionStorage.setItem('searchOption', option);
                    // const encodedData = encodeURIComponent(JSON.stringify(data));
                    window.location.href = `http://localhost:5500/search-results/search-results.html?option=${option}&query=${encodeURIComponent(query)}&data=${encodedData}`;
                })
                .catch(errors => {
                    console.error('Error:', errors);
                    alert('서버 오류입니다.')
                });
        } else {
            // 검색어가 공백인 경우: 빈 페이지로 이동
            window.location.href = '../search-results/search-results.html';
        }


        // if (query) {
        //     // 검색어가 있는 경우
        //     console.log(`${query}, ${option}`)
        //     window.location.href = `http://localhost:5500/search-results/search-results.html?option=${option}&query=${encodeURIComponent(query)}`;
        // } else {
        //     // 검색어가 공백인 경우: 빈 페이지로 이동
        //     window.location.href = '../search-results/search-results.html';
        // }

    });
});