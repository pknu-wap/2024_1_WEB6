
// 제목 클릭 시 메인 페이지로 이동
var elements = document.getElementsByClassName('web-title');
elements[0].onclick = function () {
    window.location.href = 'https://www.google.com';
};

// 댓글 작성 버튼 클릭 이벤트 핸들러
document.getElementById("submitComment").addEventListener("click", function () {
    const commentText = document.getElementById("commentInput").value;
    if (commentText.trim() !== "") {
        const newCommentDiv = document.createElement("div");
        newCommentDiv.classList.add("comment");
        newCommentDiv.textContent = commentText;
        document.getElementById("comments").appendChild(newCommentDiv);
        document.getElementById("commentInput").value = ""; // 댓글 입력란 초기화
    } else {
        alert("댓글을 작성해주세요.");
    }
});




// 영화 정보 설정 (예시 데이터)
document.getElementById("title").textContent = "영화 제목";
document.getElementById("year").textContent = "제작년도: 2024";
document.getElementById("actors").textContent = "배우: 배우1, 배우2";
document.getElementById("country").textContent = "국가: 한국";
document.getElementById("summary").textContent = "줄거리: 이 영화는...";
document.getElementById("runtime").textContent = "런타임: 120분";
document.getElementById("rating").textContent = "관람가: 12세 이상";
document.getElementById("genre").textContent = "장르: 드라마";
//,,
