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
            console.log(`쿼리: ${query}, 옵션: ${option}`)
            window.location.href = `../search-results/search-results.html?option=${option}&query=${encodeURIComponent(query)}`;
        } else {
            // 검색어가 공백인 경우: 빈 페이지로 이동
            console.log('빈 페이지로 이동합니다.')
            window.location.href = '../search-results/search-results.html';
        }

    });
});