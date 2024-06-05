document.addEventListener('DOMContentLoaded', function() {
    // 여기에 호출하고 싶은 JavaScript 메서드를 작성합니다.
    myFunction();
});

function myFunction() {
    fetch('https://backendu.com/post/search/사람/1', {
        credentials: 'include'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json(); // JSON 응답을 JavaScript 객체로 변환
        })
        .then(data => {
            console.log(data); // 변환된 데이터를 처리
            document.getElementById("response").innerHTML = data.hasNext;
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
}